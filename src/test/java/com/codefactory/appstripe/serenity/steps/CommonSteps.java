package com.codefactory.appstripe.serenity.steps;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.codefactory.appstripe.AppstripeApplication;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Data;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
    @Data
    public static class TestContext {
        private String merchantId;
        private String publicId;
        private String secret;
        private String adminToken;
        private String transactionId;
        private Response lastResponse;
        private long timestamp = System.currentTimeMillis();

        public String getUniqueEmail(String prefix) {
            return prefix + "." + timestamp + "@test.local";
        }
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

    public static ConfigurableApplicationContext springContext() {
        return springContext;
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
        resetContext();
    }

    @ParameterType(".*")
    public String text(String value) {
        return value;
    }

    @Given("el sistema de pagos está listo para recibir peticiones")
    public void elSistemaEstaListo() {
        // Verificar que la app está operativa mediante el health endpoint público
        SerenityRest.get("/actuator/health")
                .then().statusCode(200);
    }

    // ========================================================================
    // Métodos auxiliares compartidos (setup de pruebas)
    // ========================================================================

    /**
     * Obtiene token JWT de admin haciendo login directo (sin CSRF).
     * El endpoint /api/v1/auth/login es público (permitAll en SecurityConfig).
     */
    public static void loginAdmin() {
        if (context().getAdminToken() != null) {
            return;
        }
        Map<String, Object> login = new HashMap<>();
        login.put("email", "admin@paycore.com");
        login.put("password", "admin123");
        Response loginResp = SerenityRest.given()
                .contentType("application/json").body(login).when()
                .post("/api/v1/auth/login")
                .then().statusCode(200).extract().response();
        context().setAdminToken(loginResp.jsonPath().getString("token"));
        assertNotNull(context().getAdminToken(), "Token de admin debe ser retornado");
    }

    /**
     * Crea un comercio de prueba usando el token JWT de admin.
     * El comercio se crea en estado PENDING_VERIFICATION y luego se aprueba
     * para que pueda generar credenciales.
     */
    public static void crearComercio() {
        if (context().getMerchantId() != null) {
            return;
        }
        loginAdmin();
        Map<String, Object> merchant = new HashMap<>();
        String ts = String.valueOf(context().getTimestamp());
        merchant.put("businessName", "Comercio Test " + ts);
        merchant.put("businessId", "biz_test_" + ts);
        merchant.put("email", context().getUniqueEmail("test"));
        merchant.put("businessType", "RETAIL");
        Response resp = SerenityRest.given()
                .header("Authorization", "Bearer " + context().getAdminToken())
                .contentType("application/json").body(merchant).when()
                .post("/api/v1/admin/merchants")
                .then().statusCode(201).extract().response();
        context().setMerchantId(resp.jsonPath().getString("id"));
        assertNotNull(context().getMerchantId(), "merchantId no debe ser nulo");

        // Aprobar el comercio para que pueda generar credenciales
        SerenityRest.given()
                .header("Authorization", "Bearer " + context().getAdminToken())
                .when()
                .patch("/api/v1/admin/merchants/{merchantId}/approve",
                        context().getMerchantId())
                .then().statusCode(200);
    }

    /**
     * Genera credenciales API para el comercio actual usando el token JWT de admin.
     */
    public static void generarCredenciales() {
        if (context().getPublicId() != null && context().getSecret() != null) {
            return;
        }
        crearComercio();
        Map<String, Object> gen = new HashMap<>();
        gen.put("merchantId", context().getMerchantId());
        Response genResp = SerenityRest.given()
                .header("Authorization", "Bearer " + context().getAdminToken())
                .contentType("application/json").body(gen).when()
                .post("/api/v1/admin/credentials/generate")
                .then().statusCode(201).extract().response();
        context().setPublicId(genResp.jsonPath().getString("publicId"));
        context().setSecret(genResp.jsonPath().getString("secret"));
        assertNotNull(context().getPublicId(), "publicId no debe ser nulo");
        assertNotNull(context().getSecret(), "secret no debe ser nulo");
    }

    /**
     * Asegura que existan credenciales activas en el contexto.
     * Es el método único de setup que deben usar todos los Steps.
     */
    public static void asegurarCredenciales() {
        if (context().getPublicId() != null && context().getSecret() != null) {
            return;
        }
        generarCredenciales();
    }
}
