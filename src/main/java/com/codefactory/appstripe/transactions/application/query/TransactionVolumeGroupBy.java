package com.codefactory.appstripe.transactions.application.query;

public enum TransactionVolumeGroupBy {
    DAY,
    MONTH;

    public static TransactionVolumeGroupBy from(String value) {
        if (value == null || value.isBlank()) {
            return DAY;
        }

        try {
            return TransactionVolumeGroupBy.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("groupBy debe ser DAY o MONTH");
        }
    }
}