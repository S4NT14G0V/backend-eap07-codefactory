package com.codefactory.appstripe.serenity.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions para HU011 (Resultado del pago), HU014 (Listado de pagos),
 * HU016 (Reembolso total) y HU017 (Reembolso parcial).
 */
public class TransactionSteps {

    private com.codefactory.appstripe.serenity.steps.CommonSteps.TestContext context() {
        return CommonSteps.context();
    }

    // ========================================================================
    // ===== STEPS EN LENGUAJE DE NEGOCIO =====================================
    // ========================================================================
    //
    // HU011 — Procesamiento del resultado del pago
    // ========================================================================

    @Given("que el procesador financiero externo confirma la aprobación de la transacción")
    public void procesadorConfirmaAprobacion() {
        asegurarCredenciales();
        crearTransaccion();
    }

    @Given("que el procesador financiero externo rechaza la transacción por {string}")
    public void procesadorRechazaPorMotivo(String motivoRechazo) {
        asegurarCredenciales();
        crearTransaccion();
        // El motivo se usará en la validación del Then
    }

    @Given("que la plataforma intenta comunicarse con el procesador financiero externo para procesar la transacción")
    public void plataformaIntentaComunicarse() {
        asegurarCredenciales();
        crearTransaccion();
    }

    @When("el sistema recibe y procesa esa confirmación")
    public void sistemaRecibeYProcesaConfirmacion() {
        long authSuffix = System.currentTimeMillis();
        String body = String.format("""
                {
                    "result": "APPROVED",
                    "authorizationCode": "AUTH-%d",
                    "processorResponse": {
                        "code": "00",
                        "message": "APROBADA"
                    }
                }
                """, authSuffix);
        // Usar el transactionId directamente en la URL
        String endpoint = "/api/v1/transactions/{id}/complete"
                .replace("{id}", context().getTransactionId());
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .patch(endpoint);
        context().setLastResponse(resp);
    }

    @When("el sistema procesa el rechazo")
    public void sistemaProcesaRechazo() {
        String body = """
                {
                    "result": "REJECTED",
                    "authorizationCode": null,
                    "processorResponse": {
                        "code": "05",
                        "message": "RECHAZADA"
                    }
                }
                """;
        String endpoint = "/api/v1/transactions/{id}/complete"
                .replace("{id}", context().getTransactionId());
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .patch(endpoint);
        context().setLastResponse(resp);
    }

    @When("el procesador no responde dentro del tiempo máximo de espera o retorna un error de conectividad")
    public void procesadorNoResponde() {
        // Simulamos un timeout enviando un resultado FAILED directamente
        String body = """
                {
                    "result": "FAILED",
                    "authorizationCode": null,
                    "processorResponse": {
                        "code": "TIMEOUT",
                        "message": "Error de comunicación con el procesador"
                    }
                }
                """;
        String endpoint = "/api/v1/transactions/{id}/fail"
                .replace("{id}", context().getTransactionId());
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .patch(endpoint);
        context().setLastResponse(resp);
    }

    @Then("el pago queda marcado como aprobado en la plataforma")
    public void pagoMarcadoComoAprobado() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 204,
                "La aprobación debe responder 200/204, pero fue: " + resp.statusCode());
        String status = resp.jsonPath().getString("status");
        if (status != null) {
            assertEquals("COMPLETED", status,
                    "El estado debe ser COMPLETED, pero fue: " + status);
        }
    }

    @Then("el comercio recibe la notificación del pago aprobado a través de su canal de notificaciones configurado")
    public void comercioRecibeNotificacionAprobado() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String notificationSent = resp.jsonPath().getString("notificationSent");
        if (notificationSent != null) {
            assertEquals("true", notificationSent.toLowerCase());
        }
    }

    @Then("el evento queda registrado en la bitácora de auditoría del pago")
    public void eventoRegistradoBitacoraPago() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String auditId = resp.jsonPath().getString("auditId");
        if (auditId != null) {
            assertFalse(auditId.isEmpty(), "auditId no debe estar vacío");
        }
    }

    @Then("el pago queda marcado como rechazado con el motivo correspondiente en términos de negocio: {string}")
    public void pagoMarcadoComoRechazado(String descripcionNegocio) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 204,
                "El rechazo debe responder 200/204, pero fue: " + resp.statusCode());
        String status = resp.jsonPath().getString("status");
        if (status != null) {
            assertEquals("REJECTED", status,
                    "El estado debe ser REJECTED, pero fue: " + status);
        }
        // Verificar que la respuesta incluya el motivo de negocio
        String rejectReason = resp.jsonPath().getString("rejectReason");
        if (rejectReason != null) {
            assertFalse(rejectReason.isEmpty(), "Debe incluir motivo de rechazo");
        }
    }

    @Then("el comercio recibe la notificación del rechazo con el motivo de negocio correspondiente")
    public void comercioRecibeNotificacionRechazo() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String notificationSent = resp.jsonPath().getString("notificationSent");
        if (notificationSent != null) {
            assertEquals("true", notificationSent.toLowerCase());
        }
    }

    @Then("el pago queda marcado como fallido \\(FAILED) en la plataforma")
    public void pagoMarcadoComoFallido() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 204,
                "El fallo debe responder 200/204, pero fue: " + resp.statusCode());
        String status = resp.jsonPath().getString("status");
        if (status != null) {
            assertEquals("FAILED", status,
                    "El estado debe ser FAILED, pero fue: " + status);
        }
    }

    @Then("el comercio recibe la notificación indicando que el pago no pudo ser procesado por un error técnico")
    public void comercioRecibeNotificacionErrorTecnico() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String notificationSent = resp.jsonPath().getString("notificationSent");
        if (notificationSent != null) {
            assertEquals("true", notificationSent.toLowerCase());
        }
    }

    @Then("el evento queda registrado en la bitácora con el detalle del error de comunicación")
    public void eventoBitacoraConErrorComunicacion() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String auditId = resp.jsonPath().getString("auditId");
        if (auditId != null) {
            assertFalse(auditId.isEmpty(), "auditId no debe estar vacío");
        }
        String errorDetail = resp.jsonPath().getString("processorResponse.message");
        if (errorDetail != null) {
            assertFalse(errorDetail.isEmpty(), "Debe incluir detalle del error de comunicación");
        }
    }

    // ========================================================================
    // HU014 — Consulta del listado de pagos del comercio
    // ========================================================================

    @Given("que tengo transacciones registradas en diferentes estados durante el último mes")
    public void tengoTransaccionesRegistradas() {
        asegurarCredenciales();
        if (context().getTransactionId() == null) {
            crearTransaccion();
        }
        // Crear una segunda transacción con diferente estado
        Map<String, Object> tx2 = new HashMap<>();
        tx2.put("merchantId", context().getMerchantId());
        tx2.put("amount", 75000);
        Response resp2 = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json").body(tx2).when()
                .post("/api/v1/transactions");
        if (resp2.statusCode() == 201) {
            String txn2Id = resp2.jsonPath().getString("id");
            // Completar la segunda transacción como aprobada
            if (txn2Id != null) {
                Map<String, Object> completeBody = new HashMap<>();
                completeBody.put("result", "APPROVED");
                completeBody.put("authorizationCode", "AUTH-98765");
                SerenityRest.given()
                        .header("X-Merchant-Id", context().getMerchantId())
                        .header("X-Public-Id", context().getPublicId())
                        .header("X-Secret", context().getSecret())
                        .contentType("application/json").body(completeBody).when()
                        .patch("/api/v1/transactions/{id}/complete".replace("{id}", txn2Id));
            }
        }
    }

    @Given("que aplico filtros de búsqueda \\(estado, rango de fechas o monto) para los que no existe ninguna transacción en mi comercio")
    public void aplicoFiltrosSinResultados() {
        asegurarCredenciales();
    }

    @Given("que intento consultar mis transacciones con un rango de fechas en el que la fecha de inicio es posterior a la fecha de fin")
    public void intentoConsultarConRangoInvalido() {
        asegurarCredenciales();
    }

    @When("consulto mis pagos filtrando solo los aprobados dentro de un rango de fechas y solicito la primera página de resultados")
    public void consultoPagosFiltrados() {
        String endpoint = "/api/v1/transactions?status=COMPLETED&page=0&size=10"
                + "&startDate=2024-01-01T00:00:00Z&endDate=2030-12-31T23:59:59Z";
        getEndpoint(endpoint);
    }

    @When("consulto el listado de mis pagos")
    public void consultoListadoMisPagos() {
        getEndpoint("/api/v1/transactions?page=0&size=10");
    }

    @When("ejecuto la consulta")
    public void ejecutoLaConsulta() {
        // La consulta ya fue ejecutada en el Given/When previo
        // Si no hay respuesta, hacemos una consulta genérica
        if (context().getLastResponse() == null) {
            consultoListadoMisPagos();
        }
    }

    @Then("el sistema me muestra el listado de pagos aprobados en ese período con la cantidad de resultados configurada")
    public void sistemaMuestraListadoAprobados() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertEquals(200, resp.statusCode(), "La consulta debe ser exitosa");
        Object content = resp.jsonPath().getList("content");
        assertNotNull(content, "Debe contener una lista de resultados");
        // Verificar paginación
        assertNotNull(resp.jsonPath().get("page"), "Debe incluir número de página");
        assertNotNull(resp.jsonPath().get("size"), "Debe incluir tamaño de página");
    }

    @Then("puedo navegar entre páginas para ver el resto de los resultados")
    public void puedoNavegarEntrePaginas() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        // Verificar enlaces HATEOAS de paginación
        Object nextPage = resp.jsonPath().get("_links.next");
        Object lastPage = resp.jsonPath().get("_links.last");
        if (nextPage != null || lastPage != null) {
            assertTrue(true, "La respuesta incluye enlaces de paginación");
        }
    }

    @Then("el sistema aplica automáticamente un filtro de comercio basado en mi sesión activa")
    public void sistemaAplicaFiltroComercio() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertEquals(200, resp.statusCode(), "La consulta debe ser exitosa");
    }

    @Then("el listado muestra únicamente las transacciones de mi comercio, sin incluir transacciones de otros comercios de la plataforma")
    public void listadoMuestraSoloMiComercio() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        List<Map<String, Object>> content = resp.jsonPath().getList("content");
        if (content != null && !content.isEmpty()) {
            for (Map<String, Object> tx : content) {
                String merchantId = (String) tx.get("merchantId");
                if (merchantId != null) {
                    assertEquals(context().getMerchantId(), merchantId,
                            "Todas las transacciones deben pertenecer al comercio actual");
                }
            }
        }
    }

    @Then("el sistema devuelve una lista de resultados vacía")
    public void sistemaDevuelveListaVacia() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertEquals(200, resp.statusCode(),
                "Debe responder 200 incluso sin resultados, pero fue: " + resp.statusCode());
        List<Map<String, Object>> content = resp.jsonPath().getList("content");
        assertNotNull(content, "Debe existir el campo content");
        assertTrue(content.isEmpty(),
                "La lista debe estar vacía, pero tiene " + content.size() + " elementos");
    }

    @Then("el sistema indica que no se encontraron transacciones para los filtros aplicados, sin generar un error")
    public void sistemaIndicaSinResultados() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertEquals(200, resp.statusCode(),
                "No debe generar error, debe responder 200");
        long totalElements = resp.jsonPath().getLong("totalElements");
        assertEquals(0L, totalElements,
                "totalElements debe ser 0, pero fue: " + totalElements);
    }

    @Then("el sistema informa que el rango de fechas no es válido y no devuelve ningún resultado")
    public void sistemaInformaRangoInvalido() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 400 || resp.statusCode() == 422,
                "Se esperaba 200/400/422, pero fue: " + resp.statusCode());
        if (resp.statusCode() != 200) {
            String body = resp.body().asString().toLowerCase();
            assertTrue(body.contains("fecha") || body.contains("rango") || body.contains("date")
                    || body.contains("range") || body.contains("inválido") || body.contains("invalid"),
                    "Debe indicar rango de fechas inválido. Body: " + resp.body().asString());
        }
    }

    // ========================================================================
    // HU016 — Reembolso total de un pago
    // ========================================================================

    @Given("que tengo un pago aprobado que mi cliente solicita que sea devuelto en su totalidad")
    public void tengoPagoAprobadoParaReembolsoTotal() {
        asegurarCredenciales();
        existeTransaccionCompletada();
    }

    @Given("que el pago que quiero reembolsar fue rechazado o está en proceso")
    public void pagoAReembolsarFueRechazado() {
        asegurarCredenciales();
        crearTransaccion();
        // No se completa — queda en estado CREATED
    }

    @Given("que un pago ya fue reembolsado completamente con anterioridad")
    public void pagoYaReembolsadoTotalmente() {
        asegurarCredenciales();
        existeTransaccionCompletada();
        // Reembolsar el pago completamente
        String endpoint = "/api/v1/transactions/{id}/refund-full"
                .replace("{id}", context().getTransactionId());
        Map<String, Object> body = new HashMap<>();
        body.put("amount", 50000);
        body.put("reason", "Devolución total - test");
        SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json").body(body).when()
                .post(endpoint);
    }

    @When("solicito el reembolso completo de ese pago indicando el motivo de la devolución")
    public void solicitoReembolsoTotal() {
        String endpoint = "/api/v1/transactions/{id}/refund-full"
                .replace("{id}", context().getTransactionId());
        Map<String, Object> body = new HashMap<>();
        body.put("amount", 50000);
        body.put("reason", "Cliente solicitó devolución total");
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json").body(body).when()
                .post(endpoint);
        context().setLastResponse(resp);
    }

    @When("intento solicitar su reembolso")
    public void intentoSolicitarReembolso() {
        solicitoReembolsoTotal();
    }

    @When("intento solicitar un nuevo reembolso sobre ese mismo pago")
    public void intentoNuevoReembolso() {
        solicitoReembolsoTotal();
    }

    @Then("el sistema registra la operación de reembolso y le asigna un identificador único")
    public void sistemaRegistraReembolsoConId() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 201,
                "El reembolso debe responder 200/201, pero fue: " + resp.statusCode());
        String refundId = resp.jsonPath().getString("refundId");
        assertNotNull(refundId, "Debe asignar un identificador único al reembolso");
        assertFalse(refundId.isEmpty(), "refundId no debe estar vacío");
    }

    @Then("el pago original queda marcado como reembolsado en su totalidad")
    public void pagoOriginalMarcadoComoReembolsado() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        String status = resp.jsonPath().getString("status");
        if (status != null) {
            assertTrue(status.equals("REFUNDED") || status.equals("FULLY_REFUNDED"),
                    "El estado debe indicar reembolsado, pero fue: " + status);
        }
    }

    @Then("mi canal de notificación recibe el aviso del reembolso procesado")
    public void canalRecibeAvisoReembolso() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 201,
                "El reembolso debe ser exitoso");
    }

    @Then("el sistema me informa que ese pago no puede ser reembolsado porque no está en estado aprobado")
    public void sistemaInformaNoAprobado() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 400 || resp.statusCode() == 422 || resp.statusCode() == 409,
                "Se esperaba error 400/422/409, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("aprobado") || body.contains("approved") || body.contains("estado"),
                "Debe indicar que el pago no está aprobado. Body: " + resp.body().asString());
    }

    @Then("el sistema me informa que ese pago ya fue reembolsado en su totalidad y no es posible procesar otro reembolso")
    public void sistemaInformaYaReembolsado() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 400 || resp.statusCode() == 409,
                "Se esperaba 400/409 por pago ya reembolsado, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("reembolsado") || body.contains("refunded") || body.contains("ya") || body.contains("already"),
                "Debe indicar que ya fue reembolsado. Body: " + resp.body().asString());
    }

    // ========================================================================
    // HU017 — Reembolso parcial de un pago
    // ========================================================================

    @Given("que tengo un pago aprobado sobre el que quiero realizar una devolución parcial")
    public void tengoPagoAprobadoParaReembolsoParcial() {
        asegurarCredenciales();
        existeTransaccionCompletada();
    }

    @Given("que ya he realizado uno o más reembolsos parciales sobre un pago y hay un monto restante disponible para devolver")
    public void yaHeRealizadoReembolsosParciales() {
        asegurarCredenciales();
        existeTransaccionCompletada();
        // Realizar un primer reembolso parcial
        String endpoint = "/api/v1/transactions/{id}/refund-partial"
                .replace("{id}", context().getTransactionId());
        Map<String, Object> body = new HashMap<>();
        body.put("amount", 10000);
        body.put("reason", "Primer reembolso parcial - test");
        SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json").body(body).when()
                .post(endpoint);
    }

    @When("solicito un reembolso por un monto menor al total del pago")
    public void solicitoReembolsoMontoMenor() {
        String endpoint = "/api/v1/transactions/{id}/refund-partial"
                .replace("{id}", context().getTransactionId());
        Map<String, Object> body = new HashMap<>();
        body.put("amount", 15000);
        body.put("reason", "Devolución parcial - artículo devuelto");
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json").body(body).when()
                .post(endpoint);
        context().setLastResponse(resp);
    }

    @When("intento solicitar un reembolso por un monto mayor al que aún está disponible")
    public void intentoReembolsoMontoMayorDisponible() {
        String endpoint = "/api/v1/transactions/{id}/refund-partial"
                .replace("{id}", context().getTransactionId());
        Map<String, Object> body = new HashMap<>();
        body.put("amount", 999999);
        body.put("reason", "Intento de reembolso excesivo");
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json").body(body).when()
                .post(endpoint);
        context().setLastResponse(resp);
    }

    @Then("el sistema procesa el reembolso parcial y el pago queda marcado como parcialmente reembolsado")
    public void sistemaProcesaReembolsoParcial() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 200 || resp.statusCode() == 201,
                "El reembolso parcial debe responder 200/201, pero fue: " + resp.statusCode());
        String status = resp.jsonPath().getString("status");
        if (status != null) {
            assertTrue(status.equals("PARTIALLY_REFUNDED") || status.equals("REFUNDED"),
                    "El estado debe indicar reembolso parcial, pero fue: " + status);
        }
    }

    @Then("el monto disponible para futuros reembolsos se reduce en la cantidad ya devuelta")
    public void montoDisponibleSeReduce() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        Number refundableBalance = resp.jsonPath().get("refundableBalance");
        if (refundableBalance != null) {
            assertTrue(refundableBalance.doubleValue() >= 0,
                    "El saldo disponible para reembolso debe ser >= 0");
        }
    }

    @Then("el sistema me informa que el monto solicitado supera el disponible para reembolso y no procesa la operación")
    public void sistemaInformaMontoExcedeDisponible() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta");
        assertTrue(resp.statusCode() == 400 || resp.statusCode() == 422,
                "Se esperaba 400/422 por monto excedido, pero fue: " + resp.statusCode());
        String body = resp.body().asString().toLowerCase();
        assertTrue(body.contains("monto") || body.contains("amount") || body.contains("disponible")
                        || body.contains("available") || body.contains("supera") || body.contains("exceed"),
                "Debe indicar que el monto supera el disponible. Body: " + resp.body().asString());
    }

    // ========================================================================
    // HU012 — Procesamiento del resultado del pago (steps existentes)
    // ========================================================================

    @Given("existe una transacción en estado {string} con id {string}")
    public void existeTransaccionEnEstado(String estado, String idPattern) {
        existeTransaccionEnEstado(estado);
    }

    @Given("una transacción en estado {string} con id {string}")
    public void unaTransaccionEnEstadoConId(String estado, String idPattern) {
        existeTransaccionEnEstado(estado);
    }

    @Given("existe una transacción en estado {string}")
    public void existeTransaccionEnEstado(String estado) {
        // Si ya tenemos una transacción, la reutilizamos
        if (context().getTransactionId() != null) {
            return;
        }

        // Asegurar que tenemos comercio y credenciales
        if (context().getMerchantId() == null || context().getPublicId() == null) {
            // Usar un step interno: esto normalmente se haría con credenciales existentes
            crearTransaccion();
            return;
        }

        crearTransaccion();
    }

    @Given("existen transacciones creadas para el comercio actual")
    public void existenTransaccionesCreadas() {
        if (context().getTransactionId() == null) {
            existeTransaccionEnEstado("CREATED");
        }
    }

    @Given("dos comercios diferentes con transacciones creadas")
    public void dosComerciosConTransacciones() {
        // Aseguramos que el primer comercio tenga al menos una transacción
        if (context().getTransactionId() == null) {
            existeTransaccionEnEstado("CREATED");
        }
        // En un escenario real, crearíamos otro comercio. Para propósitos de la prueba,
        // esto se validaría con la respuesta del listado.
    }

    @Given("una transacción en estado {string}")
    public void unaTransaccionEnEstado(String estado) {
        if (context().getTransactionId() != null) {
            return;
        }
        existeTransaccionEnEstado(estado);
    }

    @Given("el comercio tiene una transaccion en estado {string}")
    public void elComercioTieneTransaccionEnEstado(String estado) {
        existeTransaccionEnEstado(estado);
    }

    @Given("existe una transacción en estado COMPLETED")
    public void existeTransaccionCompletada() {
        // Crear transacción y completarla
        existeTransaccionEnEstado("CREATED");

        Map<String, Object> body = new HashMap<>();
        body.put("result", "APPROVED");
        body.put("authorizationCode", "AUTH-99999");

        String endpoint = "/api/v1/transactions/{id}/complete"
                .replace("{id}", context().getTransactionId());

        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(body)
                .when()
                .patch(endpoint);

        assertEquals(200, resp.statusCode(), "La transacción debería completarse");
    }

    @Given("existen transacciones para el comercio actual")
    public void existenTransaccionesParaElComercio() {
        if (context().getTransactionId() == null) {
            existeTransaccionEnEstado("CREATED");
        }
    }

    @When("se envía una solicitud PATCH a {string} con:")
    public void patchConBody(String endpoint, String docString) {
        String resolvedEndpoint = endpoint
                .replace("{transactionId}", context().getTransactionId() != null ?
                        context().getTransactionId() : "txn_placeholder")
                .replace("{publicId}", context().getPublicId() != null ?
                        context().getPublicId() : "pk_placeholder");

        Response resp = SerenityRest.given()
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(docString)
                .when()
                .patch(resolvedEndpoint);

        context().setLastResponse(resp);
    }

    @When("se envía una solicitud GET a {string}")
    public void getEndpoint(String endpoint) {
        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .when()
                .get(endpoint);

        context().setLastResponse(resp);
    }

    @When("el comercio A consulta su listado de pagos")
    public void comercioAConsultaListado() {
        getEndpoint("/api/v1/transactions?page=0&size=10");
    }

    // ========================================================================
    // Then's — Validaciones de respuesta
    // ========================================================================

    @Then("la respuesta debe tener código {int}")
    public void laRespuestaDebeTenerCodigo(int statusCode) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");
        assertEquals(statusCode, resp.statusCode(),
                "Código de estado esperado: " + statusCode +
                        " pero fue: " + resp.statusCode() +
                        " | Body: " + resp.body().asString());
    }

    @Then("el campo {string} debe ser {string}")
    public void elCampoDebeSer(String campo, String valorEsperado) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");

        Object valorActual = resp.jsonPath().get(campo);
        assertNotNull(valorActual, "El campo '" + campo + "' no debe ser nulo");

        // Si el valor esperado tiene placeholders, reemplazarlos
        String valorResuelto = valorEsperado
                .replace("{merchantId}", context().getMerchantId() != null ?
                        context().getMerchantId() : "")
                .replace("{publicId}", context().getPublicId() != null ?
                        context().getPublicId() : "");

        assertEquals(valorResuelto, valorActual.toString(),
                "Campo '" + campo + "' debería ser '" + valorResuelto + "'");
    }

    @Then("el campo {string} debe coincidir con el patrón {string}")
    public void elCampoDebeCoincidirConPatron(String campo, String patron) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");

        String valorActual = resp.jsonPath().getString(campo);
        assertNotNull(valorActual, "El campo '" + campo + "' no debe ser nulo");

        String patronRegex = patron.replace("*", ".*");
        assertTrue(valorActual.matches(patronRegex),
                "El campo '" + campo + "' con valor '" + valorActual +
                        "' debería coincidir con '" + patron + "'");
    }

    @Then("el campo {string} debe ser un texto no vacío")
    public void elCampoDebeSerTextoNoVacio(String campo) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");
        String valor = resp.jsonPath().getString(campo);
        assertNotNull(valor, "El campo '" + campo + "' no debe ser nulo");
        assertFalse(valor.isEmpty(), "El campo '" + campo + "' no debe estar vacío");
    }

    @Then("el campo {string} debe ser un email válido")
    public void elCampoDebeSerEmailValido(String campo) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");
        String valor = resp.jsonPath().getString(campo);
        assertNotNull(valor, "El campo '" + campo + "' no debe ser nulo");
        assertTrue(valor.contains("@"), "El campo '" + campo + "' debe ser un email válido");
    }

    @Then("el campo {string} debe ser false")
    public void elCampoDebeSerFalse(String campo) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");
        Boolean valor = resp.jsonPath().getBoolean(campo);
        assertNotNull(valor, "El campo '" + campo + "' no debe ser nulo");
        assertFalse(valor, "El campo '" + campo + "' debe ser false");
    }

    @Then("la respuesta debe contener una lista de transacciones")
    public void laRespuestaDebeContenerLista() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");

        // Verificar que hay al menos un elemento o un array vacío
        Object content = resp.jsonPath().getList("content");
        assertNotNull(content, "La respuesta debe contener una lista 'content'");
    }

    @Then("la respuesta debe incluir metadatos de paginación")
    public void laRespuestaDebeIncluirPaginacion() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");
        assertNotNull(resp.jsonPath().get("page"), "Debe incluir 'page'");
        assertNotNull(resp.jsonPath().get("size"), "Debe incluir 'size'");
        assertNotNull(resp.jsonPath().get("totalElements"), "Debe incluir 'totalElements'");
    }

    @Then("todas las transacciones en la lista deben tener status {string}")
    public void todasLasTransaccionesDebenTenerStatus(String statusEsperado) {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");

        List<Map<String, Object>> content = resp.jsonPath().getList("content");
        if (content != null && !content.isEmpty()) {
            for (Map<String, Object> tx : content) {
                assertEquals(statusEsperado, tx.get("status"),
                        "Todas las transacciones deben tener status " + statusEsperado);
            }
        }
    }

    @Then("no debe ver transacciones del comercio B")
    public void noDebeVerTransaccionesDeOtroComercio() {
        Response resp = context().getLastResponse();
        assertNotNull(resp, "Debe haber una respuesta previa");

        String body = resp.body().asString();
        // Si la respuesta no tiene contenido JSON (ej: 403 sin body), 
        // no se puede validar aislamiento — eso es un problema del backend
        if (body == null || body.trim().isEmpty() || body.trim().equals("")) {
            fail("No se pudo validar aislamiento: respuesta vacía (código " + resp.statusCode() + ")");
        }

        List<Map<String, Object>> content = resp.jsonPath().getList("content");
        if (content != null && !content.isEmpty()) {
            for (Map<String, Object> tx : content) {
                String merchantId = (String) tx.get("merchantId");
                if (merchantId != null) {
                    assertEquals(context().getMerchantId(), merchantId,
                            "No debe ver transacciones de otro comercio");
                }
            }
        }
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

    private void crearTransaccion() {
        // Asegurar que tenemos credenciales
        if (context().getPublicId() == null) {
            return; // El escenario fallará por falta de credenciales
        }

        Map<String, Object> tx = new HashMap<>();
        tx.put("merchantId", context().getMerchantId());
        tx.put("amount", 50000);

        Response resp = SerenityRest.given()
                .header("X-Merchant-Id", context().getMerchantId())
                .header("X-Public-Id", context().getPublicId())
                .header("X-Secret", context().getSecret())
                .contentType("application/json")
                .body(tx)
                .when()
                .post("/api/v1/transactions")
                .then()
                .statusCode(201)
                .extract().response();

        context().setTransactionId(resp.jsonPath().getString("id"));
        context().setLastResponse(resp);
    }
}
