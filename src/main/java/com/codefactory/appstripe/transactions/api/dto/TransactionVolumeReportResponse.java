package com.codefactory.appstripe.transactions.api.dto;

import com.codefactory.appstripe.transactions.application.query.TransactionVolumeReport;
import com.codefactory.appstripe.transactions.application.query.TransactionVolumeReportItem;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class TransactionVolumeReportResponse {
    LocalDate from;
    LocalDate to;
    String groupBy;
    List<TransactionVolumeReportItemResponse> items;

    public static TransactionVolumeReportResponse fromApplication(TransactionVolumeReport report) {
        return TransactionVolumeReportResponse.builder()
                .from(report.from())
                .to(report.to())
                .groupBy(report.groupBy().name())
                .items(report.items()
                        .stream()
                        .map(TransactionVolumeReportItemResponse::fromApplication)
                        .toList())
                .build();
    }

    @Value
    @Builder
    public static class TransactionVolumeReportItemResponse {
        String period;
        long transactionCount;
        BigDecimal totalAmount;
        long approvedCount;
        long rejectedCount;
        long failedCount;

        public static TransactionVolumeReportItemResponse fromApplication(TransactionVolumeReportItem item) {
            return TransactionVolumeReportItemResponse.builder()
                    .period(item.period())
                    .transactionCount(item.transactionCount())
                    .totalAmount(item.totalAmount())
                    .approvedCount(item.approvedCount())
                    .rejectedCount(item.rejectedCount())
                    .failedCount(item.failedCount())
                    .build();
        }
    }
}