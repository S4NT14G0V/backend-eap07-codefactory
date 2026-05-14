package com.codefactory.appstripe.identity.api;

import com.codefactory.appstripe.identity.application.CredentialApplicationService;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.security.infrastructure.filter.CredentialValidationFilter;
import com.codefactory.appstripe.security.infrastructure.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CredentialController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CredentialValidationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
class CredentialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CredentialApplicationService credentialApplicationService;

    @Test
    @DisplayName("CP-S1-005: genera credenciales para comercio verificado")
    void shouldGenerateCredentials() throws Exception {
        ApiCredential generated = ApiCredential.builder()
                .publicId("pk_live_123")
                .plainSecret("sk_live_456")
                .build();

        when(credentialApplicationService.generateCredentials("mch_123")).thenReturn(generated);

        mockMvc.perform(post("/api/v1/admin/credentials/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "merchantId": "mch_123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.publicId").value("pk_live_123"))
                .andExpect(jsonPath("$.secret").value("sk_live_456"));
    }

    @Test
    @DisplayName("CP-S2-010: revoca credencial activa")
    void shouldRevokeCredential() throws Exception {
        ApiCredential revoked = ApiCredential.builder()
                .publicId("pk_live_123")
                .active(false)
                .build();

        when(credentialApplicationService.revokeCredential("pk_live_123")).thenReturn(revoked);

        mockMvc.perform(patch("/api/v1/admin/credentials/pk_live_123/revoke"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").value("pk_live_123"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    @DisplayName("Debe retornar 404 al revocar credencial inexistente")
    void shouldReturnNotFoundWhenCredentialDoesNotExist() throws Exception {
        when(credentialApplicationService.revokeCredential(anyString()))
                .thenThrow(new NoSuchElementException("Credencial no encontrada: pk_missing"));

        mockMvc.perform(patch("/api/v1/admin/credentials/pk_missing/revoke"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }
}
