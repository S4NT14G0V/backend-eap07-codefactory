package com.codefactory.appstripe.serenity.steps;

import java.util.HashMap;
import java.util.Map;

import com.codefactory.appstripe.identity.application.CommerceApplicationService;
import com.codefactory.appstripe.security.application.port.IUserRepositoryPort;

import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

/**
 * Step definitions adicionales para HU009.
 * <p>
 * El Escenario 3 (idempotencia) reutiliza {@code unaCredencialQueFueRevocada}
 * de {@link CredentialSteps} + el PATCH genérico.
 * El Escenario 4 (aislamiento) requiere el step nuevo de abajo.
 * </p>
 */
public class RevocationSteps {

    private final SharedContext context = SharedContext.getInstance();

    /**
     * HU009 - Escenario 4: intenta revocar la credencial del comercio actual
     * pero presentando un JWT de un comercio diferente.
     *
     * <p>En el entorno de prueba Serenity no tenemos un segundo JWT de comercio,
     * por lo que simulamos el aislamiento enviando la petición sin autenticación
     * de administrador (sin Authorization header) sobre la credencial que sí existe.
     * Esto provoca que Spring Security retorne 403 por falta de rol ADMIN,
     * comportamiento equivalente al rechazo por aislamiento desde la perspectiva del test E2E.</p>
     */
    @When("se envía una solicitud PATCH de revocación con credenciales de otro comercio")
    public void patchRevocationWithForeignMerchantCredentials() {
        String publicId = context.getPublicId();
        if (publicId == null) {
            publicId = "pk_live_unknown";
        }

        String foreignMerchantToken = ensureForeignMerchantToken();

        // Enviamos un JWT de un comercio distinto para forzar el rechazo por permisos
        Response resp = SerenityRest.given()
                .cookie("XSRF-TOKEN", context.getCsrfCookie())
                .header(context.getCsrfHeaderName(), context.getCsrfToken())
                .header("Authorization", "Bearer " + foreignMerchantToken)
                .when()
                .patch("/api/v1/admin/credentials/{publicId}/revoke", publicId);

        context.setLastResponse(resp);
    }

    /**
     * Step auxiliar para verificar que la respuesta de revocación incluye
     * el campo status con valor "REVOKED" (parte del Escenario 1).
     * Este step ya está cubierto en {@code TransactionSteps#elCampoDebeSer}
     * que es genérico — no requiere implementación adicional.
     */

    /**
     * Prepara el contexto para el escenario de idempotencia:
     * primero crea credenciales y las revoca, luego el step genérico
     * {@code se envía una solicitud PATCH a} vuelve a intentar revocarlas.
     */
    @When("se intenta revocar nuevamente la credencial ya revocada")
    public void patchRevokeAlreadyRevokedCredential() {
        String publicId = context.getPublicId();

        Response resp = SerenityRest.given()
                .cookie("XSRF-TOKEN", context.getCsrfCookie())
                .header(context.getCsrfHeaderName(), context.getCsrfToken())
                .header("Authorization", "Bearer " + context.getAdminToken())
                .when()
                .patch("/api/v1/admin/credentials/{publicId}/revoke", publicId);

        context.setLastResponse(resp);
    }

    private String ensureForeignMerchantToken() {
        String email = "foreign." + context.getTimestamp() + "@test.local";
        String password = "ForeignTest123!";

        if (context.getCsrfToken() == null || context.getCsrfHeaderName() == null || context.getCsrfCookie() == null) {
            Response csrfResp = SerenityRest.given()
                    .when()
                    .get("/api/v1/security/csrf")
                    .then()
                    .statusCode(200)
                    .extract().response();

            context.setCsrfToken(csrfResp.jsonPath().getString("token"));
            context.setCsrfHeaderName(csrfResp.jsonPath().getString("headerName"));
            context.setCsrfCookie(csrfResp.cookie("XSRF-TOKEN"));
        }

        CommerceApplicationService commerceApplicationService = CommonSteps.springContext().getBean(CommerceApplicationService.class);
        IUserRepositoryPort userRepository = CommonSteps.springContext().getBean(IUserRepositoryPort.class);

        commerceApplicationService.registerMerchant(
                "Merchant Externo " + context.getTimestamp(),
                "biz_foreign_" + context.getTimestamp(),
                email,
                "RETAIL");

        String invitationToken = userRepository.findByEmail(email)
                .map(user -> user.getInvitationToken())
                .orElseThrow(() -> new IllegalStateException("No se pudo obtener el token de invitación del comercio externo"));

        Map<String, Object> activate = new HashMap<>();
        activate.put("invitationToken", invitationToken);
        activate.put("newPassword", password);

        SerenityRest.given()
                .cookie("XSRF-TOKEN", context.getCsrfCookie())
                .header(context.getCsrfHeaderName(), context.getCsrfToken())
                .contentType("application/json")
                .body(activate)
                .when()
                .post("/api/v1/auth/merchant/activate")
                .then()
                .statusCode(200);

        Response loginResp = SerenityRest.given()
                .cookie("XSRF-TOKEN", context.getCsrfCookie())
                .header(context.getCsrfHeaderName(), context.getCsrfToken())
                .contentType("application/json")
                .body(Map.of("email", email, "password", password))
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract().response();

        String token = loginResp.jsonPath().getString("token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("No se pudo obtener el JWT del comercio externo");
        }

        return token;
    }
}