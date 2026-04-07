package com.codefactory.appstripe.transactions.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {

    @NotBlank(message = "merchantId es obligatorio")
    private String merchantId;

    @NotNull(message = "amount es obligatorio")
    @DecimalMin(value = "0.01", message = "amount debe ser mayor a 0")
    private BigDecimal amount;
}
