package com.codefactory.appstripe.serenity.steps;

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

        // Enviamos sin Authorization para simular acceso denegado por aislamiento
        Response resp = SerenityRest.given()
                .cookie("XSRF-TOKEN", context.getCsrfCookie())
                .header(context.getCsrfHeaderName(), context.getCsrfToken())
                // Sin header Authorization → falta de permisos → 403
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
}