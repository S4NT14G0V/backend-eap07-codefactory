package com.codefactory.appstripe.transactions.api.dto;

import lombok.Value;

@Value
public class CompleteTransactionRequest {
    String result; // APPROVED | REJECTED | FAILED
    String authorizationCode; // optional for approved
    String rejectionReason; // optional for rejected
}
