package com.codefactory.appstripe.transactions.domain;

public enum TransactionStatus {
    // GUARDAR LOS ESTADOS PERMITIDOS, enum para el type safety
    CREATED, // Estado inicial
    PROCESSING, // Se cambia a este estado
    APPROVED, // Estado final
    REJECTED,// Estado final
    FAILED // Estado final
}