# =============================================================================
# HU-12: Procesamiento del resultado del pago
# Prioridad: 3 | Módulo: Transactions / Payment Result
# =============================================================================
# Como plataforma de pagos,
# Quiero procesar el resultado final de un pago (aprobado/rechazado/fallido),
# Para notificar al comercio el estado final de la transacción.

Feature: Procesamiento del resultado del pago
  Como plataforma de pagos
  Quiero procesar el resultado final de los pagos
  Para notificar a los comercios el estado de sus transacciones

  Background:
    Given el sistema de pagos está listo para recibir peticiones
    Given un comercio verificado con credenciales activas

  @HU012 @CP-S2-014 @camino-feliz
  Scenario: Pago aprobado exitosamente
    Given una transacción en estado "CREATED" con id "{transactionId}"
    When se envía una solicitud PATCH a "/api/v1/transactions/{transactionId}/complete" con:
      """
      {
        "result": "APPROVED",
        "authorizationCode": "AUTH-12345"
      }
      """
    Then la respuesta debe tener código 200
    And el campo "status" debe ser "COMPLETED"
    And el campo "result" debe ser "APPROVED"

  @HU012 @CP-S2-015 @borde
  Scenario: Pago rechazado por fondos insuficientes
    Given una transacción en estado "CREATED" con id "{transactionId}"
    When se envía una solicitud PATCH a "/api/v1/transactions/{transactionId}/complete" con:
      """
      {
        "result": "REJECTED",
        "rejectionReason": "INSUFFICIENT_FUNDS"
      }
      """
    Then la respuesta debe tener código 200
    And el campo "status" debe ser "COMPLETED"
    And el campo "result" debe ser "REJECTED"
