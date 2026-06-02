package com.codefactory.appstripe.transactions.api.dto;


import com.codefactory.appstripe.transactions.application.query.PaymentStatusDistribution;
import com.codefactory.appstripe.transactions.application.query.PaymentStatusDistributionItem;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class PaymentStatusDistributionResponse {
    LocalDate from;
    LocalDate to;
    long totalFinalized;
    BigDecimal approvalRate;
    List<StatusDistributionItemResponse> distribution;

    public static PaymentStatusDistributionResponse fromApplication(PaymentStatusDistribution dashboard) {
        return PaymentStatusDistributionResponse.builder()
                .from(dashboard.from())
                .to(dashboard.to())
                .totalFinalized(dashboard.totalFinalized())
                .approvalRate(dashboard.approvalRate())
                .distribution(
                        dashboard.distribution()
                                .stream()
                                .map(StatusDistributionItemResponse::fromApplication)
                                .toList()
                )
                .build();
    }

    @Value
    @Builder
    public static class StatusDistributionItemResponse {
        String status;
        long count;
        BigDecimal percentage;

        public static StatusDistributionItemResponse fromApplication(PaymentStatusDistributionItem item) {
            return StatusDistributionItemResponse.builder()
                    .status(item.status().name())
                    .count(item.count())
                    .percentage(item.percentage())
                    .build();
        }
    }
}