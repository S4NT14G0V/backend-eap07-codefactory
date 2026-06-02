package com.codefactory.appstripe.transactions.application.query;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PaymentStatusDistribution(
        LocalDate from,
        LocalDate to,
        long totalFinalized,
        BigDecimal approvalRate,
        List<PaymentStatusDistributionItem> distribution
) {
}