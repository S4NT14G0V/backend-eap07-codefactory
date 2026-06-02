package com.codefactory.appstripe.transactions.application.query;

import com.codefactory.appstripe.transactions.domain.TransactionStatus;

public record TransactionStatusCount(
        TransactionStatus status,
        long count
) {
}
