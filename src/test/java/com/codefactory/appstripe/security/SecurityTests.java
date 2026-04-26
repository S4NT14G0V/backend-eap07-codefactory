package com.codefactory.appstripe.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codefactory.appstripe.common.api.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warrenstrange.googleauth.GoogleAuthenticator;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe generar un secreto 2FA desde la API")
    void shouldGenerateTwoFactorSecretViaApi() throws Exception {
        String response = mockMvc.perform(post("/api/v1/2fa/secret")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secret").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

                assertThat(objectMapper.readTree(response).get("secret").asText()).isNotBlank();
    }

    @Test
    @DisplayName("Debe validar un código TOTP correcto desde la API")
    void shouldVerifyTotpCodeViaApi() throws Exception {
        String secretResponse = mockMvc.perform(post("/api/v1/2fa/secret")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String secret = objectMapper.readTree(secretResponse).get("secret").asText();

        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        int validCode = authenticator.getTotpPassword(secret);

        mockMvc.perform(post("/api/v1/2fa/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "twoFactorSecret": "%s",
                                  "code": %d
                                }
                                """.formatted(secret, validCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    @DisplayName("Debe devolver error de validación estandarizado cuando falta el secreto")
    void shouldReturnStandardValidationErrorWhenSecretMissing() throws Exception {
        String response = mockMvc.perform(post("/api/v1/2fa/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": 123456
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.traceId").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(objectMapper.readValue(response, ErrorResponse.class).getDetails())
                .anyMatch(detail -> detail.contains("twoFactorSecret"));
    }
}