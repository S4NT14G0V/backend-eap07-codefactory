// src/main/java/com/codefactory/appstripe/transactions/api/dto/RefundRequest.java
package com.codefactory.appstripe.transactions.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class RefundRequest {

    // Para reembolso parcial: monto a devolver.
    // Para reembolso total: se ignora (se usa null).
    @DecimalMin(value = "0.01", message = "El monto del reembolso debe ser mayor a cero")
    private BigDecimal amount;

    private String reason; // motivo de la devolución (opcional)
}