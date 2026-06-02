package com.codefactory.appstripe.transactions.application;

import com.codefactory.appstripe.transactions.domain.Transaction;
import com.codefactory.appstripe.transactions.domain.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCsvGeneratorTest {

    // ── Escenario 1: Encabezados correctos ────────────────────────────────

    @Test
    @DisplayName("El CSV generado contiene los encabezados correctos")
    void shouldGenerateCsvWithCorrectHeaders() throws IOException {
        // Arrange
        List<Transaction> transactions = List.of();

        // Act
        byte[] csvBytes = TransactionCsvGenerator.generateCsvBytes(transactions);
        String csv = new String(csvBytes, "UTF-8");

        // Assert – el encabezado debe estar presente (después del BOM)
        assertTrue(csv.contains("transactionId,date,amount,currency,status,authorizationCode"),
                "El CSV debe contener los encabezados correctos");
    }

    // ── Escenario 2: AuthorizationCode para transacciones aprobadas ──────

    @Test
    @DisplayName("El authorizationCode aparece solo en transacciones APPROVED")
    void shouldIncludeAuthorizationCodeForApprovedTransactions() throws IOException {
        // Arrange
        Transaction approved = new Transaction("tx-1", "mch-1",
                new BigDecimal("100.00"), TransactionStatus.APPROVED,
                BigDecimal.ZERO, "USD", "AUTH-XYZ-123",
                LocalDateTime.of(2026, 6, 15, 10, 30));

        Transaction rejected = new Transaction("tx-2", "mch-1",
                new BigDecimal("50.00"), TransactionStatus.REJECTED,
                BigDecimal.ZERO, "USD", null,
                LocalDateTime.of(2026, 6, 16, 11, 0));

        // Act
        byte[] csvBytes = TransactionCsvGenerator.generateCsvBytes(List.of(approved, rejected));
        String csv = new String(csvBytes, "UTF-8");

        // Assert
        assertTrue(csv.contains("AUTH-XYZ-123"),
                "El CSV debe incluir el authorizationCode para la transacción APPROVED");
        // La fila de la transacción rechazada no debería tener authCode
        String[] lines = csv.split("\r\n");
        // Línea 0 = encabezado, Línea 1 = approved, Línea 2 = rejected
        assertTrue(lines[1].contains("AUTH-XYZ-123"),
                "La fila APPROVED debe contener el auth code");
        assertFalse(lines[2].contains("AUTH-XYZ-123"),
                "La fila REJECTED no debe contener el auth code de otra transacción");
    }

    // ── Escenario 3: AuthorizationCode null se maneja correctamente ──────

    @Test
    @DisplayName("El authorizationCode null se maneja como campo vacío")
    void shouldHandleEmptyAuthorizationCodeGracefully() throws IOException {
        // Arrange
        Transaction tx = new Transaction("tx-1", "mch-1",
                new BigDecimal("75.50"), TransactionStatus.PROCESSING,
                BigDecimal.ZERO, "COP", null,
                LocalDateTime.of(2026, 6, 10, 8, 0));

        // Act
        byte[] csvBytes = TransactionCsvGenerator.generateCsvBytes(List.of(tx));
        String csv = new String(csvBytes, "UTF-8");

        // Assert – la fila debe terminar sin auth code (campo vacío al final)
        String[] lines = csv.split("\r\n");
        String dataLine = lines[1]; // Primera fila de datos
        assertTrue(dataLine.contains("tx-1"), "La fila debe contener el ID");
        assertTrue(dataLine.contains("75.50"), "La fila debe contener el monto");
        assertTrue(dataLine.contains("COP"), "La fila debe contener la moneda");
        assertTrue(dataLine.endsWith("PROCESSING,"),
                "La fila debe terminar con el status y campo auth vacío");
    }
}
