package com.codefactory.appstripe.transactions.api;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IApiKeyGeneratorPort;  // ADD THIS
import com.codefactory.appstripe.transactions.application.TransactionApplicationService;
import com.codefactory.appstripe.transactions.domain.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionApplicationService transactionApplicationService;
    
    @MockBean
    private IApiCredentialRepositoryPort apiCredentialRepositoryPort;
    
    @MockBean
    private IApiKeyGeneratorPort apiKeyGeneratorPort;  // ADD THIS

    @Test
    @DisplayName("Debe crear transacción y retornar CREATED")
    void shouldCreateTransaction() throws Exception {
        Transaction saved = new Transaction("trx-001", "mch-001", new BigDecimal("150.00"));
        when(transactionApplicationService.assignInitialStatusCreated(any(Transaction.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "merchantId": "mch-001",
                          "amount": 150.00
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("trx-001"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @DisplayName("Debe rechazar monto inválido")
    void shouldRejectInvalidAmount() throws Exception {
        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "merchantId": "mch-001",
                          "amount": 0
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}