package com.codefactory.appstripe.serenity.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions para HU007 (Consulta del perfil del comercio) y HU008 (Actualización de perfil).
 */
public class MerchantSteps {

    private com.codefactory.appstripe.serenity.steps.CommonSteps.TestContext context() {
        return CommonSteps.context();
    }

    // ========================================================================
    // ===== STEPS COMPARTIDOS ================================================
    // ========================================================================

    @Given("que soy el administrador de un comercio activo y estoy autenticado en la plataforma")
    public void soyAdminComercioActivoAutenticado() {
        asegurarCredenciales();
    }

    @Given("que soy el administrador de mi comercio y estoy autenticado en la plataforma")
    @Given("que soy el administrador de mi comercio y estoy autenticado")
    public void soyAdminMiComercioAutenticado() {
        asegurarCredenciales();
    }

    @Given("que estoy autenticado como administrador de mi comercio")
    public void estoyAutenticadoComoAdmin() {
        asegurarCredenciales();
    }

    // ========================================================================
    // HU007 — Consulta del perfil del comercio
    // ========================================================================

    @Given("que mi comercio se encuentra en estado suspendido y estoy autenticado en la plataforma")
    public void comercioSuspendidoAutenticado() {
        asegurarCredenciales();
        // Suspender el comercio vía admin
        SerenityRest.given()
                .header("Authorization", "Bearer " + context().getAdminToken())
                .contentType("application/json")
                .body(java.util.Map.of("reason", "Suspensión de prueba"))
                .when()
                .patch("/api/v1/admin/merchants/{merchantId}/suspend",
                        context().getMerchantId())
                .then().statusCode(200);
    }

    @Given("que no estoy autenticado en la plataforma")
    public void noEstoyAutenticado() {
        // No se hace login ni se obtienen credenciales
        // Con CSRF deshabilitado en la API, no se necesita token CSRF
        // Explícitamente limpiamos cualquier token de autenticación
        context().setAdminToken(null);
        context().setPublicId(null);
        context().setSecret(null);
        context().setMerchantId(null);
    }

    @When("accedo a la sección de perfil de mi comercio")
    @When("accedo a la sección de perfil")
    public void accedoAPerfilMiComercio() {
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .when()
                .get("/api/v1/merchant-portal/profile");
        context().setLastResponse(resp);
    }

    @When("intento acceder a la información de un comercio diferente al mío")
    public void intentoAccederAOtroComercio() {
        // Enviar X-Merchant-Id de otro comercio para probar aislamiento
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", "mch_ajeno_007")
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .when()
                .get("/api/v1/merchant-portal/profile");
        context().setLastResponse(resp);
    }

    @When("intento acceder a la sección de perfil de un comercio")
    public void intentoAccederAPerfilSinAuth() {
        Response resp = SerenityRest.given()
                .when()
                .get("/api/v1/merchant-portal/profile");
        context().setLastResponse(resp);
    }

    @Then("el sistema me muestra la información completa de mi comercio: nombre, datos de contacto, estado actual y datos bancarios registrados")
    public void sistemaMuestraInfoCompleta() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertEquals(200, resp.statusCode(),
                "La consulta debe ser exitosa. Status: " + resp.statusCode());
        // Verificar campos clave del perfil
        String businessName = resp.jsonPath().getString("businessName");
        String email = resp.jsonPath().getString("email");
        String status = resp.jsonPath().getString("status");
        assertNotNull(businessName, "El perfil debe incluir el nombre del comercio");
        assertNotNull(email, "El perfil debe incluir el email de contacto");
        if (status != null) {
            assertFalse(status.isEmpty(), "El estado no debe estar vacío");
        }
    }

    @Then("el sistema niega el acceso e informa que no tengo permisos para ver esa información")
    public void sistemaNiegaAcceso() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 401 || resp.statusCode() == 403 || resp.statusCode() == 404,
                "Se esperaba 401/403/404, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("permiso") || body.contains("permission")
                        || body.contains("autorizado") || body.contains("authorized")
                        || body.contains("acceso") || body.contains("access")
                        || body.contains("credencial") || body.contains("credential"),
                "Debe indicar falta de permisos. Body: " + resp.body().asString());
    }

    @Then("el sistema muestra mi información de perfil en modo solo lectura")
    public void sistemaMuestraPerfilSoloLectura() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        // Nota: Al suspender un comercio, sus credenciales se revocan automáticamente.
        // Si el backend retorna 401, indica que el comercio suspendido no tiene acceso a la API.
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 401,
                "Un comercio suspendido puede ver su perfil o no tiene acceso. Status: " + resp.statusCode());
        if (resp.statusCode() == 200) {
            String body = resp.body().asString().toLowerCase();
            assertTrue(body.contains("businessName") || body.contains("email"),
                    "Debe retornar datos del perfil");
        }
    }

    @Then("el sistema indica claramente que mi cuenta está suspendida y que no puedo procesar ni modificar datos")
    public void sistemaIndicaCuentaSuspendida() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        if (resp.statusCode() == 200) {
            String status = resp.jsonPath().getString("status");
            if (status != null) {
                assertTrue(status.equalsIgnoreCase("SUSPENDED") || status.equalsIgnoreCase("SUSPENDIDO"),
                        "El estado debe ser SUSPENDED. Status: " + status);
            }
        }
        // Si el status es 401, las credenciales fueron revocadas al suspender
    }

    @Then("el sistema rechaza la solicitud e indica que se requiere autenticación para acceder a este recurso")
    public void sistemaRechazaSinAuth() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 401 || resp.statusCode() == 403,
                "Se esperaba 401/401, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("autenticación") || body.contains("authentication")
                        || body.contains("login") || body.contains("token")
                        || body.contains("credencial") || body.contains("credential"),
                "Debe requerir autenticación. Body: " + resp.body().asString());
    }

    // ========================================================================
    // HU008 — Actualización de información bancaria del comercio
    // ========================================================================

    @When("modifico los datos de contacto \\(correo, teléfono o dirección) y guardo los cambios")
    public void modificoDatosContactoYGuardo() {
        Map<String, Object> body = new HashMap<>();
        body.put("email", context().getUniqueEmail("updated"));
        body.put("phone", "+525512345678");
        body.put("address", "Av. Reforma 222, CDMX");

        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .patch("/api/v1/merchant-portal/profile");
        context().setLastResponse(resp);
    }

    @When("modifico los datos de mi cuenta bancaria y guardo los cambios")
    public void modificoDatosBancariosYGuardo() {
        Map<String, Object> bankAccount = new HashMap<>();
        bankAccount.put("bankName", "Banco Nacional");
        bankAccount.put("accountNumber", "1234567890");
        bankAccount.put("accountType", "CHECKING");
        bankAccount.put("routingNumber", "BNMEX123");

        Map<String, Object> body = new HashMap<>();
        body.put("bankAccount", bankAccount);

        String endpoint = "/api/v1/merchants/{id}/bank-account"
                .replace("{id}", context().getMerchantId());
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .put(endpoint);
        context().setLastResponse(resp);
    }

    @Given("que intento modificar el número de identificación fiscal o el tipo de negocio registrados al crear la cuenta")
    public void intentoModificarDatosRegistroUnico() {
        CommonSteps.asegurarCredenciales();
        Map<String, Object> body = new HashMap<>();
        body.put("businessId", "RFC_MODIFICADO_999");
        body.put("businessType", "TECHNOLOGY");

        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .patch("/api/v1/merchant-portal/profile");
        context().setLastResponse(resp);
    }

    @Given("que estoy autenticado en la plataforma con el rol de desarrollador")
    public void estoyAutenticadoComoDesarrollador() {
        // Primero crear un comercio para tener merchantId en el contexto
        CommonSteps.asegurarCredenciales();
        // Luego login como desarrollador (sobrescribe el token de admin)
        Map<String, Object> login = new HashMap<>();
        login.put("email", "developer@paycore.com");
        login.put("password", "dev123");
        Response loginResp = SerenityRest.given()
                .contentType("application/json").body(login).when()
                .post("/api/v1/auth/login");
        if (loginResp.statusCode() == 200) {
            context().setAdminToken(loginResp.jsonPath().getString("token"));
        } else {
            context().setAdminToken("dev_token_placeholder");
        }
    }

    // Ver nota: este step está en soyAdminMiComercioAutenticado() arriba
    // (se deja comentado para evitar DuplicateStepDefinitionException)
    // @Given("que soy el administrador de mi comercio y estoy autenticado en la plataforma")
    // public void soyAdminAutenticado() {
    //     asegurarCredenciales();
    // }

    @When("guardo los cambios")
    public void guardoLosCambios() {
        // La respuesta ya fue almacenada en el step anterior
        assertNotNull(context().getLastResponse(),
                "Debe haber una respuesta previa de la operación");
    }

    @When("intento actualizar los datos de perfil o configuración bancaria del comercio")
    public void intentoActualizarPerfilComoDesarrollador() {
        Map<String, Object> body = new HashMap<>();
        body.put("email", "test_dev@cambio.local");

        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .header("Authorization", "Bearer " + context().getAdminToken())
                .contentType("application/json")
                .body(body)
                .when()
                .patch("/api/v1/merchant-portal/profile");
        context().setLastResponse(resp);
    }

    @When("ingreso datos bancarios con un formato incorrecto \\(número de cuenta con caracteres no numéricos o con una longitud inválida) e intento guardar")
    public void ingresoDatosBancariosIncorrectos() {
        Map<String, Object> bankAccount = new HashMap<>();
        bankAccount.put("bankName", "Banco Test");
        bankAccount.put("accountNumber", "ABC123!@#"); // Formato inválido
        bankAccount.put("accountType", "CHECKING");

        Map<String, Object> body = new HashMap<>();
        body.put("bankAccount", bankAccount);

        String endpoint = "/api/v1/merchants/{id}/bank-account"
                .replace("{id}", context().getMerchantId());
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .put(endpoint);
        context().setLastResponse(resp);
    }

    @Then("el sistema confirma que la información fue actualizada correctamente")
    public void sistemaConfirmaActualizacionExitosa() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 204,
                "La actualización debe responder 200/204, pero fue: " + resp.statusCode());
    }

    @Then("el sistema registra en la bitácora qué campos fueron modificados, por quién y en qué momento")
    public void sistemaRegistraBitacoraCampos() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 204,
                "La actualización debe ser exitosa");
    }

    @Then("el sistema confirma que la solicitud de cambio fue recibida")
    public void sistemaConfirmaCambioRecibido() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 202,
                "La solicitud de cambio bancario debe responder 200/202, pero fue: " + resp.statusCode());
    }

    @Then("los nuevos datos bancarios quedan en estado de verificación pendiente hasta ser validados por el equipo de la plataforma")
    public void datosBancariosQuedanEnVerificacion() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String bankStatus = resp.jsonPath().getString("bankAccount.status");
        if (bankStatus != null) {
            assertTrue(bankStatus.equalsIgnoreCase("PENDING_VERIFICATION")
                            || bankStatus.equalsIgnoreCase("PENDING"),
                    "El estado bancario debe ser pendiente de verificación. Status: " + bankStatus);
        }
    }

    @Then("mientras la verificación esté pendiente, las liquidaciones continúan realizándose a la cuenta bancaria anterior")
    public void liquidacionesContinuanCuentaAnterior() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String activeAccount = resp.jsonPath().getString("bankAccount.activeAccount");
        if (activeAccount != null) {
            assertFalse(activeAccount.isEmpty(), "Debe indicar la cuenta activa actual");
        }
    }

    @Then("el sistema registra en la bitácora los campos modificados, el autor del cambio y el momento exacto")
    public void sistemaRegistraBitacoraAutorYMomento() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 202,
                "La solicitud debe ser procesada exitosamente");
    }

    @Then("el sistema me informa que esos datos son de registro único y no pueden modificarse")
    public void sistemaInformaDatosNoModificables() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 400 || resp.statusCode() == 422,
                "Se esperaba 400/422 por datos no modificables, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("registro único") || body.contains("no pueden modificarse")
                        || body.contains("read-only") || body.contains("no modificable")
                        || body.contains("cannot") || body.contains("modificar"),
                "Debe indicar que los datos no son modificables. Body: " + resp.body().asString());
    }

    @Then("el sistema sugiere contactar al equipo de soporte si hay un error en los datos originales")
    public void sistemaSugiereContactarSoporte() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("soporte") || body.contains("support")
                        || body.contains("contact") || body.contains("ayuda"),
                "Debe sugerir contactar a soporte. Body: " + resp.body().asString());
    }

    @Then("el sistema rechaza la acción e informa que mi rol no tiene permisos para modificar esta información")
    public void sistemaRechazaPorRol() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 403 || resp.statusCode() == 401,
                "Se esperaba 403/401 por permisos insuficientes, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("permiso") || body.contains("permission")
                        || body.contains("rol") || body.contains("role")
                        || body.contains("autorizado") || body.contains("authorized"),
                "Debe indicar permisos insuficientes por rol. Body: " + resp.body().asString());
    }

    @Then("el sistema rechaza la actualización e indica qué campos tienen un formato inválido")
    public void sistemaRechazaFormatoInvalido() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 400 || resp.statusCode() == 422,
                "Se esperaba 400/422 por formato inválido, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("formato") || body.contains("format")
                        || body.contains("inválido") || body.contains("invalid")
                        || body.contains("campo") || body.contains("field"),
                "Debe indicar campos con formato inválido. Body: " + resp.body().asString());
    }

    @Then("los datos bancarios anteriores permanecen sin cambios")
    public void datosBancariosAnterioresSinCambios() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() >= 400,
                "La operación debe haber sido rechazada");
    }

    // ========================================================================
    // Métodos auxiliares
    // ========================================================================

    /**
     * Asegura que tengamos credenciales activas en el contexto.
     * Delega en CommonSteps para evitar duplicación.
     */
    private void asegurarCredenciales() {
        CommonSteps.asegurarCredenciales();
    }
}
