package com.codefactory.appstripe.serenity.steps;

import io.restassured.response.Response;

/**
 * Contexto compartido entre steps de Serenity/Cucumber.
 * <p>
 * Implementado como singleton para evitar dependencia de Spring DI.
 * Cada escenario debe invocar {@link #reset()} en el {@code @Before}
 * para garantizar aislamiento entre escenarios.
 */
public class SharedContext {

    private static final SharedContext INSTANCE = new SharedContext();

    private String merchantId;

    private SharedContext() {
        // singleton
    }

    public static SharedContext getInstance() {
        return INSTANCE;
    }
    private String publicId;
    private String secret;
    private String adminToken;
    private String csrfToken;
    private String csrfHeaderName;
    private String csrfCookie;
    private String transactionId;
    private Response lastResponse;
    private long timestamp = System.currentTimeMillis();

    // Getters y Setters
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public String getAdminToken() { return adminToken; }
    public void setAdminToken(String adminToken) { this.adminToken = adminToken; }

    public String getCsrfToken() { return csrfToken; }
    public void setCsrfToken(String csrfToken) { this.csrfToken = csrfToken; }

    public String getCsrfHeaderName() { return csrfHeaderName; }
    public void setCsrfHeaderName(String csrfHeaderName) { this.csrfHeaderName = csrfHeaderName; }

    public String getCsrfCookie() { return csrfCookie; }
    public void setCsrfCookie(String csrfCookie) { this.csrfCookie = csrfCookie; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Response getLastResponse() { return lastResponse; }
    public void setLastResponse(Response lastResponse) { this.lastResponse = lastResponse; }

    public long getTimestamp() { return timestamp; }
    public String getUniqueEmail(String prefix) {
        return prefix + "." + timestamp + "@test.local";
    }

    /**
     * Resetea el contexto para cada escenario.
     */
    public void reset() {
        merchantId = null;
        publicId = null;
        secret = null;
        adminToken = null;
        csrfToken = null;
        csrfHeaderName = null;
        csrfCookie = null;
        transactionId = null;
        lastResponse = null;
        timestamp = System.currentTimeMillis();
    }
}
