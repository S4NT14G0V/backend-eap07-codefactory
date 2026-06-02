package com.codefactory.appstripe.transactions.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.appstripe.transactions.api.dto.CreateTransactionRequest;
import com.codefactory.appstripe.transactions.api.dto.TransactionResponse;
import com.codefactory.appstripe.transactions.application.TransactionApplicationService;
import com.codefactory.appstripe.transactions.domain.Transaction;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionApplicationService transactionApplicationService;

    public TransactionController(TransactionApplicationService transactionApplicationService) {
        this.transactionApplicationService = transactionApplicationService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest request) {
        Transaction transaction = new Transaction(null, request.getMerchantId(), request.getAmount());
        Transaction saved = transactionApplicationService.assignInitialStatusCreated(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponse.fromDomain(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable String id) {
        Transaction transaction = transactionApplicationService.getById(id);
        return ResponseEntity.ok(TransactionResponse.fromDomain(transaction));
    }

    @PatchMapping("/{transactionId}/complete")
    public ResponseEntity<TransactionResponse> complete(@PathVariable String transactionId, @RequestBody com.codefactory.appstripe.transactions.api.dto.CompleteTransactionRequest request) {
        Transaction updated = transactionApplicationService.completeTransaction(transactionId, request.getResult(), request.getAuthorizationCode(), request.getRejectionReason());
        return ResponseEntity.ok(TransactionResponse.fromDomain(updated));
    }
}
