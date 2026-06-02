package com.codefactory.appstripe.transactions.application.query;

import java.math.BigDecimal;

public record TransactionVolumeReportItem(
        String period,
        long transactionCount,
        BigDecimal totalAmount,
        long approvedCount,
        long rejectedCount,
        long failedCount
) {
}