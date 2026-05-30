# =============================================================================
# HU-07: Consulta de perfil de comercio
# Prioridad: 2 | Módulo: Identity / MerchantPortalController
# =============================================================================
# Como comercio registrado en la plataforma,
# Quiero consultar mi perfil comercial y configuración bancaria,
# Para verificar que mi información está correcta antes de operar.

Feature: Consulta de perfil de comercio
  Como comercio registrado
  Quiero consultar mi perfil comercial
  Para verificar mi información registrada

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU007 @CP-S2-004 @camino-feliz
  Scenario: Consulta exitosa del propio perfil
    Given un comercio autenticado con credenciales activas
    When se envía una solicitud GET a "/api/v1/merchant-portal/profile"
    Then la respuesta debe tener código 200
    And el campo "businessName" debe ser un texto no vacío
    And el campo "email" debe ser un email válido
    And el campo "status" debe ser "VERIFIED"
