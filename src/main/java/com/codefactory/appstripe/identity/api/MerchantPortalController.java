package com.codefactory.appstripe.identity.api;

import com.codefactory.appstripe.identity.api.dto.MerchantResponse;
import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.transactions.application.port.ITransactionRepositoryPort;
import com.codefactory.appstripe.transactions.domain.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant-portal")
public class MerchantPortalController {

    private final ICommerceRepositoryPort commerceRepository;
    private final IApiCredentialRepositoryPort credentialRepository;
    private final ITransactionRepositoryPort transactionRepository;

    public MerchantPortalController(ICommerceRepositoryPort commerceRepository, 
                                    IApiCredentialRepositoryPort credentialRepository,
                                    ITransactionRepositoryPort transactionRepository) {
        this.commerceRepository = commerceRepository;
        this.credentialRepository = credentialRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<MerchantResponse> getProfile(Authentication authentication) {
        String merchantId = authentication.getCredentials().toString(); // El merchantId se guarda en los credentials del token
        Merchant merchant = commerceRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado"));
        return ResponseEntity.ok(MerchantResponse.fromDomain(merchant));
    }

    @GetMapping("/credentials")
    public ResponseEntity<List<ApiCredential>> getCredentials(Authentication authentication) {
        String merchantId = authentication.getCredentials().toString();
        List<ApiCredential> credentials = credentialRepository.findByMerchantIdAndActiveTrue(merchantId);
        // Excluimos secretHash de la respuesta
        return ResponseEntity.ok(credentials);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(Authentication authentication) {
        String merchantId = authentication.getCredentials().toString();
        // Nota: asumiendo que findByMerchantId existe. Si no, lo agregamos al puerto.
        // Vamos a agregarlo a ITransactionRepositoryPort ahora.
        List<Transaction> transactions = transactionRepository.findByMerchantId(merchantId);
        return ResponseEntity.ok(transactions);
    }
}
