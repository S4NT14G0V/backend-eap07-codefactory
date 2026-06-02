package com.codefactory.appstripe.transactions.application;

import com.codefactory.appstripe.transactions.domain.Transaction;
import com.codefactory.appstripe.transactions.domain.TransactionStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Clase utilitaria que genera archivos CSV con el detalle de transacciones.
 * Formato: UTF-8 con BOM para compatibilidad con Excel.
 * Columnas: transactionId, date, amount, currency, status, authorizationCode
 */
public class TransactionCsvGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final String CSV_HEADER = "transactionId,date,amount,currency,status,authorizationCode";
    private static final char SEPARATOR = ',';
    private static final String LINE_END = "\r\n";

    // BOM (Byte Order Mark) para UTF-8, necesario para que Excel reconozca la codificación
    private static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    private TransactionCsvGenerator() {
        // Clase utilitaria, no se instancia
    }

    /**
     * Genera el contenido CSV en memoria como arreglo de bytes.
     *
     * @param transactions lista de transacciones a exportar
     * @return bytes del archivo CSV listo para descarga o escritura a disco
     * @throws IOException si ocurre un error durante la escritura
     */
    public static byte[] generateCsvBytes(List<Transaction> transactions) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Escribir BOM para compatibilidad con Excel
        outputStream.write(UTF8_BOM);

        try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            // Escribir encabezado
            writer.write(CSV_HEADER);
            writer.write(LINE_END);

            // Escribir cada transacción como una fila
            for (Transaction tx : transactions) {
                writer.write(buildCsvRow(tx));
                writer.write(LINE_END);
            }

            writer.flush();
        }

        return outputStream.toByteArray();
    }

    /**
     * Construye una fila CSV para una transacción individual.
     * Si el authorizationCode es null (transacción no aprobada), se deja vacío.
     */
    static String buildCsvRow(Transaction tx) {
        StringBuilder row = new StringBuilder();

        row.append(escapeCsvField(tx.getId()));
        row.append(SEPARATOR);
        row.append(tx.getCreatedAt() != null ? tx.getCreatedAt().format(DATE_FORMAT) : "");
        row.append(SEPARATOR);
        row.append(tx.getAmount() != null ? tx.getAmount().toPlainString() : "0");
        row.append(SEPARATOR);
        row.append(escapeCsvField(tx.getCurrency()));
        row.append(SEPARATOR);
        row.append(tx.getStatus() != null ? tx.getStatus().name() : "");
        row.append(SEPARATOR);

        // El código de autorización solo aplica para transacciones APPROVED
        if (tx.getStatus() == TransactionStatus.APPROVED && tx.getAuthorizationCode() != null) {
            row.append(escapeCsvField(tx.getAuthorizationCode()));
        }

        return row.toString();
    }

    /**
     * Escapa un campo CSV: si contiene comas, comillas o saltos de línea,
     * lo envuelve en comillas dobles y escapa las comillas internas.
     */
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
