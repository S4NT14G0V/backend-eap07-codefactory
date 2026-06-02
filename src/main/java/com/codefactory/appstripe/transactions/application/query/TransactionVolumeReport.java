package com.codefactory.appstripe.transactions.application.query;

import java.time.LocalDate;
import java.util.List;

public record TransactionVolumeReport(
        LocalDate from,
        LocalDate to,
        TransactionVolumeGroupBy groupBy,
        List<TransactionVolumeReportItem> items
) {
}