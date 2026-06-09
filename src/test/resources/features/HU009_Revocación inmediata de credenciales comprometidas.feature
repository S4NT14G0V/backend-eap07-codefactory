# =============================================================================
# HU009 - Revocación inmediata de credenciales comprometidas
# Épica: EP02 | Feature: FE202 | Sprint: Sprint 2 | Prioridad: Alta
# Story Points: 5 SP
# =============================================================================
# Como administrador de un comercio,
# Quiero revocar inmediatamente cualquiera de mis credenciales activas ante una
# sospecha de uso no autorizado,
# Para detener de inmediato el acceso indebido a la plataforma, protegiendo las
# operaciones de mi negocio ante una posible filtración.
# =============================================================================

Feature: Revocación y rotación de credenciales de acceso a la API
  Como administrador de un comercio
  Quiero revocar inmediatamente cualquiera de mis credenciales activas ante una sospecha de uso no autorizado
  Para detener de inmediato el acceso indebido a la plataforma, protegiendo las operaciones de mi negocio ante una posible filtración

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU009 @camino-feliz
  Scenario: Revocación exitosa de una credencial activa
    Given que tengo credenciales activas visibles en mi panel de administración
    When selecciono una credencial y confirmo su revocación
    Then la credencial queda inhabilitada de forma inmediata
    And el sistema confirma la revocación
    And el evento queda registrado en la bitácora de auditoría con la hora exacta y el administrador que realizó la acción

  @HU009 @error
  Scenario: Credencial revocada no puede autenticar nuevas solicitudes
    Given que una de mis credenciales ha sido revocada
    When el sistema recibe una solicitud de pago que utiliza esa credencial revocada
    Then el sistema rechaza la solicitud
    And informa que las credenciales presentadas no son válidas

  @HU009 @error
  Scenario: No es posible revocar una credencial que ya fue revocada
    Given que una credencial de mi comercio ya se encuentra en estado revocada
    When intento devolverla a su estado anterior
    Then el sistema informa que la credencial ya fue revocada con anterioridad y no requiere ninguna acción adicional

  @HU009 @error
  Scenario: No es posible revocar una credencial de otro comercio
    Given que estoy autenticado como administrador de mi comercio
    When intento revocar una credencial que pertenece a un comercio diferente al mío
    Then el sistema rechaza la acción e informa que no tengo permisos sobre esa credencial