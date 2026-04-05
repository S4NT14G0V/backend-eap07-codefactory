package com.codefactory.appstripe.transactions.domain.exception;

public class InvalidTransactionStateException extends RuntimeException{
    // hereda de RuntimeException, para que en ejecucion no se hagan cambios en la base de datos si el estado no es el correcto
    public InvalidTransactionStateException(String message) {
        super(message);
    }
}
