# =============================================================================
# HU-10: Revocación inmediata de credenciales
# Prioridad: 3 | Módulo: Identity / CredentialService
# =============================================================================
# Como comercio o administrador,
# Quiero revocar credenciales API de forma inmediata,
# Para desactivar accesos comprometidos sin demora.

Feature: Revocación inmediata de credenciales
  Como administrador de la plataforma
  Quiero revocar credenciales API de forma inmediata
  Para desactivar accesos no autorizados

  Background:
    Given el sistema de pagos está listo para recibir peticiones
    And existen credenciales activas para un comercio verificado

  @HU010 @CP-S2-010 @camino-feliz
  Scenario: Revocación exitosa de credencial activa
    Given un administrador autenticado
    And una credencial activa con publicId "{publicId}"
    When se envía una solicitud PATCH a "/api/v1/admin/credentials/{publicId}/revoke"
    Then la respuesta debe tener código 200
    And el campo "active" debe ser false

  @HU010 @CP-S2-011 @seguridad
  Scenario: Credencial revocada rechaza transacciones
    Given una credencial que fue revocada
    When se envía una solicitud POST a "/api/v1/transactions" con la credencial revocada
    Then la respuesta debe tener código 401
    And el campo "errorCode" debe ser "INVALID_CREDENTIALS"