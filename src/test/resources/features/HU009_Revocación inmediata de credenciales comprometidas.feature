# =============================================================================
# HU-09: Revocación inmediata de credenciales comprometidas
# Prioridad: 3 | Módulo: Identity / CredentialApplicationService
# =============================================================================
# Como administrador del comercio,
# Quiero revocar inmediatamente cualquiera de mis credenciales activas
# ante una sospecha de uso no autorizado,
# Para detener de inmediato el acceso indebido y proteger las operaciones
# de mi negocio ante una posible filtración.

Feature: Revocación inmediata de credenciales comprometidas
  Como administrador de un comercio
  Quiero revocar credenciales API de forma inmediata
  Para detener accesos no autorizados ante una posible filtración

  Background:
    Given el sistema de pagos está listo para recibir peticiones
    And existen credenciales activas para un comercio verificado

  # ---------------------------------------------------------------------------
  # Escenario 1: Revocación exitosa + auditoría
  # ---------------------------------------------------------------------------
  @HU009 @CP-S2-010 @camino-feliz
  Scenario: Revocación exitosa de una credencial activa con registro de auditoría
    Given un administrador autenticado
    And una credencial activa con publicId "{publicId}"
    When se envía una solicitud PATCH a "/api/v1/admin/credentials/{publicId}/revoke"
    Then la respuesta debe tener código 200
    And el campo "active" debe ser false
    And el campo "status" debe ser "REVOKED"

  # ---------------------------------------------------------------------------
  # Escenario 2: Credencial revocada no puede autenticar nuevas solicitudes
  # ---------------------------------------------------------------------------
  @HU009 @CP-S2-011 @seguridad
  Scenario: Credencial revocada rechaza solicitudes de pago posteriores
    Given una credencial previamente revocada
    When se envía una solicitud POST a "/api/v1/transactions" con la credencial revocada
    Then la respuesta debe tener código 401
    And el campo "errorCode" debe ser "INVALID_CREDENTIALS"

  # ---------------------------------------------------------------------------
  # Escenario 3: No es posible revocar una credencial ya revocada
  # ---------------------------------------------------------------------------
  @HU009 @CP-S2-revocada-idempotencia @borde
  Scenario: Intento de revocar una credencial que ya fue revocada
    Given una credencial que fue revocada
    When se envía una solicitud PATCH a "/api/v1/admin/credentials/{publicId}/revoke"
    Then la respuesta debe tener código 409
    And el campo "errorCode" debe ser "BUSINESS_RULE_VIOLATION"

  # ---------------------------------------------------------------------------
  # Escenario 4: No se puede revocar credencial de otro comercio
  # ---------------------------------------------------------------------------
  @HU009 @CP-S2-aislamiento @seguridad
  Scenario: Intento de revocar credencial de otro comercio
    Given un comercio verificado con credenciales activas
    When se envía una solicitud PATCH de revocación con credenciales de otro comercio
    Then la respuesta debe tener código 403
    And el campo "errorCode" debe ser "ACCESS_DENIED"