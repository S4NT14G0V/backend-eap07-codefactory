# =============================================================================
# HU-08: Actualización de perfil / configuración bancaria
# Prioridad: 2 | Módulo: Identity / MerchantPortalController
# =============================================================================
# Como comercio registrado,
# Quiero actualizar mi perfil comercial y configuración bancaria,
# Para mantener mi información al día.

Feature: Actualización de perfil de comercio
  Como comercio registrado
  Quiero actualizar mi perfil comercial
  Para mantener mi información actualizada

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU008 @CP-S2-006 @camino-feliz
  Scenario: Actualización exitosa de datos permitidos
    Given un comercio autenticado con credenciales activas
    When se envía una solicitud PATCH a "/api/v1/merchant-portal/profile" con:
      """
      {
        "businessName": "Mi Tienda Actualizada",
        "contactPhone": "+573001234567"
      }
      """
    Then la respuesta debe tener código 200
    And el campo "businessName" debe ser "Mi Tienda Actualizada"