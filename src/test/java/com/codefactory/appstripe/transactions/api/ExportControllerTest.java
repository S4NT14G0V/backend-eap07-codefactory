package com.codefactory.appstripe.transactions.api;

import com.codefactory.appstripe.transactions.application.TransactionExportService;
import com.codefactory.appstripe.transactions.domain.ExportRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportControllerTest {

    @Mock
    private TransactionExportService transactionExportService;

    @InjectMocks
    private ExportController exportController;

    // ── Escenario 1: Exportación síncrona retorna 200 con CSV ─────────────

    @Test
    @DisplayName("Retorna 200 OK con archivo CSV cuando la exportación es síncrona")
    void shouldReturn200WithCsv_WhenSyncExport() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn("mch_123");

        byte[] csvBytes = "transactionId,date,amount,currency,status,authorizationCode\r\ntx-1,2026-06-15,100.00,USD,APPROVED,AUTH-001\r\n".getBytes();

        when(transactionExportService.exportTransactions(eq("mch_123"), any(), any()))
                .thenReturn(TransactionExportService.ExportResult.sync(csvBytes));

        com.codefactory.appstripe.transactions.api.dto.ExportTransactionsRequest request =
                new com.codefactory.appstripe.transactions.api.dto.ExportTransactionsRequest(
                        LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        // Act
        ResponseEntity<?> response = exportController.exportTransactions(auth, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
        assertTrue(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("attachment"));
    }

    // ── Escenario 2: Exportación asíncrona retorna 202 Accepted ───────────

    @Test
    @DisplayName("Retorna 202 Accepted cuando la exportación es asíncrona")
    void shouldReturn202_WhenAsyncExport() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.getCredentials()).thenReturn("mch_123");

        ExportRequest exportRequest = new ExportRequest("mch_123",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30));

        when(transactionExportService.exportTransactions(eq("mch_123"), any(), any()))
                .thenReturn(TransactionExportService.ExportResult.async(exportRequest));

        com.codefactory.appstripe.transactions.api.dto.ExportTransactionsRequest request =
                new com.codefactory.appstripe.transactions.api.dto.ExportTransactionsRequest(
                        LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30));

        // Act
        ResponseEntity<?> response = exportController.exportTransactions(auth, request);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // ── Escenario 3: Token expirado retorna excepción ─────────────────────

    @Test
    @DisplayName("Lanza excepción cuando el token de descarga ha expirado")
    void shouldThrowException_WhenDownloadTokenExpired() {
        // Arrange
        String expiredToken = "expired-token-uuid";

        when(transactionExportService.downloadExport(eq(expiredToken)))
                .thenThrow(new IllegalStateException("El enlace de descarga ha expirado"));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> exportController.downloadExport(expiredToken));
    }

    // ── Escenario 4: Auth nulo lanza excepción ────────────────────────────

    @Test
    @DisplayName("Lanza excepción cuando el authentication es nulo")
    void shouldThrowException_WhenAuthenticationIsNull() {
        // Arrange
        com.codefactory.appstripe.transactions.api.dto.ExportTransactionsRequest request =
                new com.codefactory.appstripe.transactions.api.dto.ExportTransactionsRequest(
                        LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> exportController.exportTransactions(null, request));
    }
}
