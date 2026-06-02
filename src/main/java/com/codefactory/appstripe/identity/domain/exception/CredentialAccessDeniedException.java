package com.codefactory.appstripe.identity.domain.exception;

public class CredentialAccessDeniedException extends RuntimeException {
    public CredentialAccessDeniedException(String message) {
        super(message);
    }
}