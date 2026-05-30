package com.codefactory.appstripe.serenity.steps;

import com.codefactory.appstripe.AppstripeApplication;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Steps comunes compartidos por todos los features.
 * Configuración global del actor, Background de features y estado compartido.
 */
public class CommonSteps {

    private static ConfigurableApplicationContext springContext;

    private static final ThreadLocal<TestContext> contextHolder = new ThreadLocal<>();

    /**
     * Contexto compartido entre steps dentro de un mismo escenario.
     * Almacena IDs, tokens, credenciales y última respuesta.
     */
    public static class TestContext {
        private String merchantId;
        private String publicId;
        private String secret;
        private String adminToken;
        private String csrfToken;
        private String csrfHeaderName;
        private String csrfCookie;
        private Response lastResponse;
        private long timestamp = System.currentTimeMillis();

        // Getters y setters
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

        public Response getLastResponse() { return lastResponse; }
        public void setLastResponse(Response lastResponse) { this.lastResponse = lastResponse; }

        public long getTimestamp() { return timestamp; }
        public String getUniqueEmail(String prefix) { return prefix + "." + timestamp + "@test.local"; }
    }

    public static TestContext context() {
        if (contextHolder.get() == null) {
            contextHolder.set(new TestContext());
        }
        return contextHolder.get();
    }

    public static void resetContext() {
        contextHolder.remove();
    }

    @BeforeAll
    public static void startSpringBoot() {
        if (springContext == null || !springContext.isRunning()) {
            springContext = SpringApplication.run(AppstripeApplication.class,
                    "--server.port=8083",
                    "--spring.profiles.active=test");
        }
        RestAssured.port = 8083;
    }

    @Before(order = 0)
    public void setTheStage() {
        // Configurar puerto del servidor embebido
        RestAssured.port = 8083;
        SharedContext.getInstance().reset();
        resetContext();
    }

    @ParameterType(".*")
    public String text(String value) {
        return value;
    }

    @Given("el sistema de pagos está listo para recibir peticiones")
    public void elSistemaEstaListo() {
        // Spring Boot ya fue iniciado por @SpringBootTest en el runner
        SerenityRest.head("/api/v1/security/csrf")
                .then().statusCode(200);
    }
}
