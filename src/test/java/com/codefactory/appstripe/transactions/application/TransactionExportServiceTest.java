package com.codefactory.appstripe.transactions.application;

import com.codefactory.appstripe.transactions.application.port.IExportNotifierPort;
import com.codefactory.appstripe.transactions.application.port.IExportRequestRepositoryPort;
import com.codefactory.appstripe.transactions.application.port.ITransactionRepositoryPort;
import com.codefactory.appstripe.transactions.domain.ExportRequest;
import com.codefactory.appstripe.transactions.domain.Transaction;
import com.codefactory.appstripe.transactions.domain.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionExportServiceTest {

    @Mock
    private ITransactionRepositoryPort transactionRepositoryPort;

    @Mock
    private IExportRequestRepositoryPort exportRequestRepositoryPort;

    @Mock
    private IExportNotifierPort exportNotifierPort;

    @InjectMocks
    private TransactionExportService exportService;

    // ── Escenario 1: Exportación síncrona exitosa ─────────────────────────

    @Test
    @DisplayName("Exportación síncrona: genera CSV inmediato cuando count ≤ umbral")
    void shouldExportSynchronously_WhenCountBelowThreshold() throws Exception {
        // Arrange
        String merchantId = "mch_123";
        LocalDate from = LocalDate.of(2026, 6, 1);
        LocalDate to = LocalDate.of(2026, 6, 30);
        LocalDateTime fromInclusive = from.atStartOfDay();
        LocalDateTime toExclusive = to.plusDays(1).atStartOfDay();

        // Configurar umbral bajo para forzar flujo síncrono
        setSyncThreshold(5000);

        when(transactionRepositoryPort.countByMerchantIdAndCreatedAtBetween(
                eq(merchantId), eq(fromInclusive), eq(toExclusive)
        )).thenReturn(3L);

        List<Transaction> transactions = List.of(
                new Transaction("tx-1", merchantId, new BigDecimal("100.00"),
                        TransactionStatus.APPROVED, BigDecimal.ZERO,
                        "USD", "AUTH-001", LocalDateTime.of(2026, 6, 15, 10, 30)),
                new Transaction("tx-2", merchantId, new BigDecimal("50.00"),
                        TransactionStatus.REJECTED, BigDecimal.ZERO,
                        "USD", null, LocalDateTime.of(2026, 6, 20, 14, 0))
        );

        when(transactionRepositoryPort.findByMerchantIdAndCreatedAtBetween(
                eq(merchantId), eq(fromInclusive), eq(toExclusive)
        )).thenReturn(transactions);

        // Act
        TransactionExportService.ExportResult result = exportService.exportTransactions(merchantId, from, to);

        // Assert
        assertTrue(result.synchronous(), "Debería ser exportación síncrona");
        assertNotNull(result.csvBytes(), "Debería tener bytes del CSV");
        assertNull(result.exportRequest(), "No debería tener ExportRequest para flujo síncrono");

        // Verificar que el CSV contiene el encabezado y datos
        String csv = new String(result.csvBytes(), "UTF-8");
        assertTrue(csv.contains("transactionId,date,amount,currency,status,authorizationCode"));
        assertTrue(csv.contains("tx-1"));
        assertTrue(csv.contains("AUTH-001"));

        // Verificar que NO se creó ExportRequest
        verify(exportRequestRepositoryPort, never()).save(any());
    }

    // ── Escenario 2: Exportación asíncrona ────────────────────────────────

    @Test
    @DisplayName("Exportación asíncrona: crea ExportRequest PENDING cuando count > umbral")
    void shouldExportAsynchronously_WhenCountAboveThreshold() throws Exception {
        // Arrange
        String merchantId = "mch_123";
        LocalDate from = LocalDate.of(2026, 1, 1);
        LocalDate to = LocalDate.of(2026, 6, 30);

        // Configurar umbral bajo para forzar flujo asíncrono
        setSyncThreshold(100);

        // Configurar storageDir para usar un directorio temporal
        setFieldValue("storageDir", "./target/test-exports");
        setFieldValue("tokenExpiryHours", 24);
        setFieldValue("baseDownloadUrl", "http://localhost:8080/api/v1/transactions/export/download");

        when(transactionRepositoryPort.countByMerchantIdAndCreatedAtBetween(
                eq(merchantId), any(), any()
        )).thenReturn(5000L);

        // Simular que save() devuelve un ExportRequest con ID (como lo haría la BD)
        ExportRequest savedRequest = new ExportRequest(
                "export-uuid-123", merchantId,
                com.codefactory.appstripe.transactions.domain.ExportStatus.PENDING,
                from, to, null, null, null,
                LocalDateTime.now(), null, 0
        );
        when(exportRequestRepositoryPort.save(any(ExportRequest.class))).thenReturn(savedRequest);

        // @Async no funciona en unit tests, así que generateExportAsync se ejecuta síncrono.
        // Necesitamos mockear findById para que la cadena completa funcione.
        when(exportRequestRepositoryPort.findById("export-uuid-123"))
                .thenReturn(java.util.Optional.of(savedRequest));

        // Mockear transacciones para la generación del CSV
        when(transactionRepositoryPort.findByMerchantIdAndCreatedAtBetween(
                eq(merchantId), any(), any()
        )).thenReturn(List.of(
                new Transaction("tx-1", merchantId, new BigDecimal("100.00"),
                        TransactionStatus.APPROVED, BigDecimal.ZERO,
                        "USD", "AUTH-001", LocalDateTime.of(2026, 3, 15, 10, 0))
        ));

        // Act
        TransactionExportService.ExportResult result = exportService.exportTransactions(merchantId, from, to);

        // Assert
        assertFalse(result.synchronous(), "Debería ser exportación asíncrona");
        assertNull(result.csvBytes(), "No debería tener bytes CSV para flujo asíncrono");
        assertNotNull(result.exportRequest(), "Debería tener ExportRequest");

        // Verificar que se guardó el ExportRequest (al menos 2 veces: creación + procesamiento)
        verify(exportRequestRepositoryPort, atLeast(2)).save(any(ExportRequest.class));
        // Verificar que se notificó al comercio
        verify(exportNotifierPort).notifyExportReady(eq(merchantId), anyString(), any());
    }

    // ── Escenario 3: Sin transacciones en el período ──────────────────────

    @Test
    @DisplayName("Lanza excepción cuando no hay transacciones en el período")
    void shouldThrowException_WhenNoTransactionsInPeriod() throws Exception {
        // Arrange
        String merchantId = "mch_123";
        LocalDate from = LocalDate.of(2026, 1, 1);
        LocalDate to = LocalDate.of(2026, 1, 31);

        setSyncThreshold(1000);

        when(transactionRepositoryPort.countByMerchantIdAndCreatedAtBetween(
                eq(merchantId), any(), any()
        )).thenReturn(0L);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> exportService.exportTransactions(merchantId, from, to));
        assertTrue(ex.getMessage().contains("No se encontraron transacciones"));
    }

    // ── Escenario 4: Rango de fechas inválido ─────────────────────────────

    @Test
    @DisplayName("Lanza excepción cuando la fecha inicial es posterior a la fecha final")
    void shouldThrowException_WhenFromDateAfterToDate() {
        // Arrange
        String merchantId = "mch_123";
        LocalDate from = LocalDate.of(2026, 6, 30);
        LocalDate to = LocalDate.of(2026, 6, 1);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> exportService.exportTransactions(merchantId, from, to));
        assertTrue(ex.getMessage().contains("no puede ser posterior"));
    }

    // ── Escenario 5: MerchantId vacío ─────────────────────────────────────

    @Test
    @DisplayName("Lanza excepción cuando el merchantId está vacío")
    void shouldThrowException_WhenMerchantIdIsBlank() {
        // Arrange
        LocalDate from = LocalDate.of(2026, 6, 1);
        LocalDate to = LocalDate.of(2026, 6, 30);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> exportService.exportTransactions("", from, to));
        assertThrows(IllegalStateException.class,
                () -> exportService.exportTransactions(null, from, to));
    }

    /**
     * Helper para configurar el umbral síncrono vía reflexión
     * (necesario porque @Value no se resuelve en tests con @Mock)
     */
    private void setSyncThreshold(int threshold) throws Exception {
        Field field = TransactionExportService.class.getDeclaredField("syncThreshold");
        field.setAccessible(true);
        field.setInt(exportService, threshold);
    }

    /**
     * Helper genérico para configurar campos @Value vía reflexión.
     */
    private void setFieldValue(String fieldName, Object value) throws Exception {
        Field field = TransactionExportService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(exportService, value);
    }
}
