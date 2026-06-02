package com.codefactory.appstripe.transactions.api.dto;

import java.math.BigDecimal;

import com.codefactory.appstripe.transactions.domain.Transaction;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionResponse {
    String id;
    String merchantId;
    BigDecimal amount;
    String status;
    String result;

    public static TransactionResponse fromDomain(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .merchantId(transaction.getMerchantId())
                .amount(transaction.getAmount())
                .status((transaction.getStatus() == com.codefactory.appstripe.transactions.domain.TransactionStatus.APPROVED || transaction.getStatus() == com.codefactory.appstripe.transactions.domain.TransactionStatus.REJECTED || transaction.getStatus() == com.codefactory.appstripe.transactions.domain.TransactionStatus.FAILED) ? "COMPLETED" : transaction.getStatus().name())
                .result(transaction.getStatus() == com.codefactory.appstripe.transactions.domain.TransactionStatus.APPROVED ? "APPROVED" :
                    transaction.getStatus() == com.codefactory.appstripe.transactions.domain.TransactionStatus.REJECTED ? "REJECTED" : null)
                .build();
    }
}
