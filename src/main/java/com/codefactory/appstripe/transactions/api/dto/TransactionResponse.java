package com.codefactory.appstripe.transactions.api.dto;

import com.codefactory.appstripe.transactions.domain.Transaction;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class TransactionResponse {
    String id;
    String merchantId;
    BigDecimal amount;
    String status;

    public static TransactionResponse fromDomain(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .merchantId(transaction.getMerchantId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus().name())
                .build();
    }
}
