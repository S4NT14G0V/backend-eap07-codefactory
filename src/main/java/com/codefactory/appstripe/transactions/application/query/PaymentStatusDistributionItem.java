package com.codefactory.appstripe.transactions.application.query;

import com.codefactory.appstripe.transactions.domain.TransactionStatus;

import java.math.BigDecimal;

public record PaymentStatusDistributionItem(
        TransactionStatus status,
        long count,
        BigDecimal percentage
) {
}