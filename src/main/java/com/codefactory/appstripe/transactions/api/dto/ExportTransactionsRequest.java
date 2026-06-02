package com.codefactory.appstripe.transactions.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;

/**
 * DTO de entrada para solicitar la exportación de transacciones.
 * El comercio especifica el rango de fechas del período a exportar.
 */
@Value
public class ExportTransactionsRequest {

    @NotNull(message = "La fecha de inicio (from) es obligatoria")
    LocalDate from;

    @NotNull(message = "La fecha de fin (to) es obligatoria")
    LocalDate to;
}
