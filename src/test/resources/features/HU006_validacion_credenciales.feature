# =============================================================================
# HU006: Validación de credenciales por solicitud
# Prioridad: 1 | Módulo: Security / CredentialValidationFilter
# =============================================================================
# Como un comercio integrado a la plataforma,
# Quiero que cada solicitud a la API transaccional valide mis credenciales (publicKey + secret),
# Para que solo pagos autorizados puedan ser procesados.

Feature: Validación de credenciales por solicitud
  Como comercio verificado
  Quiero que cada petición a la API valide mis credenciales API
  Para garantizar que solo transacciones autorizadas sean procesadas

  Background:
    Given el sistema de pagos está listo para recibir peticiones
    And existen credenciales activas con permiso "payments:write"

  @HU006 @CP-S2-001 @camino-feliz
  Scenario: Transacción exitosa con credenciales válidas
    Given un comercio verificado con credenciales activas
    When se envía una solicitud POST a "/api/v1/transactions" con las credenciales válidas
    And el cuerpo de la transacción contiene:
      """
      {
        "merchantId": "{merchantId}",
        "amount": 25000
      }
      """
    Then la respuesta debe tener código 201
    And el campo "status" debe ser "CREATED"
    And el campo "id" debe coincidir con el patrón "txn_*"

  @HU006 @CP-S2-003 @seguridad
  Scenario: Rechazo por credenciales de otro comercio
    Given un comercio verificado con credenciales activas
    When se envía una solicitud POST a "/api/v1/transactions" con X-Merchant-Id de otro comercio
    Then la respuesta debe tener código 401
    And el campo "errorCode" debe ser "CREDENTIAL_MISMATCH"