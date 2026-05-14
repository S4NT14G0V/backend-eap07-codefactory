package com.codefactory.appstripe.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationApiTest {

    @LocalServerPort
    int port;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void postmanFlow_like_integration_test() {
        // 1) Login as admin (seeded by DataSeeder)
        Map<String, Object> login = new HashMap<>();
        login.put("email", "admin@paycore.com");
        login.put("password", "admin123");

        String token = given()
                .contentType("application/json")
                .body(login)
            .when().post("/api/v1/auth/login")
            .then().statusCode(200)
                .extract().path("token");

        Assertions.assertNotNull(token, "admin token should be returned");

        // 2) Create merchant
        long ts = System.currentTimeMillis();
        Map<String, Object> merchant = new HashMap<>();
        merchant.put("businessType", "RETAIL");
        merchant.put("email", "qa." + ts + "@mitienda.local");
        merchant.put("businessId", "biz_" + ts);
        merchant.put("businessName", "Mi Tienda SAS " + ts);

        String merchantId = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(merchant)
            .when().post("/api/v1/admin/merchants")
            .then().statusCode(201)
                .extract().path("id");

        Assertions.assertNotNull(merchantId);

        // 3) Generate credentials
        Map<String, Object> gen = new HashMap<>();
        gen.put("merchantId", merchantId);

        Response genResp = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(gen)
            .when().post("/api/v1/admin/credentials/generate")
            .then().statusCode(201)
                .extract().response();

        String publicId = genResp.jsonPath().getString("publicId");
        String secret = genResp.jsonPath().getString("secret");

        Assertions.assertNotNull(publicId);
        Assertions.assertNotNull(secret);

        // 4) Create transaction
        Map<String, Object> tx = new HashMap<>();
        tx.put("amount", 10000);
        tx.put("merchantId", merchantId);
        tx.put("currency", "COP");
        tx.put("paymentToken", "tok_visa_approved");

        String paymentId = given()
                .header("X-Merchant-Id", merchantId)
                .header("X-Public-Id", publicId)
                .header("X-Secret", secret)
                .contentType("application/json")
                .body(tx)
            .when().post("/api/v1/transactions")
            .then().statusCode(201)
                .extract().path("id");

        Assertions.assertNotNull(paymentId);

        // 5) Verify transaction GET
        given()
                .header("X-Merchant-Id", merchantId)
                .header("X-Public-Id", publicId)
                .header("X-Secret", secret)
            .when().get("/api/v1/transactions/{id}", paymentId)
            .then().statusCode(200)
                .body("id", equalTo(paymentId));
    }
}
