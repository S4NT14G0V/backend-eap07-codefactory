# =============================================================================
# HU007 - Consulta del perfil del comercio
# Épica: EP01 | Feature: FE102 | Sprint: Sprint 2 | Prioridad: Media
# Story Points: 3 SP
# =============================================================================
# Como representante del comercio,
# Quiero consultar en cualquier momento la información de identificación de mi comercio,
# Para verificar que los datos del comercio estén correctos.
# =============================================================================

Feature: Consulta y administración del perfil de un comercio registrado
  Como representante del comercio
  Quiero consultar en cualquier momento la información de identificación de mi comercio
  Para verificar que los datos del comercio estén correctos

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU007 @camino-feliz
  Scenario: Consulta exitosa del perfil del propio comercio
    Given que soy el administrador de un comercio activo y estoy autenticado en la plataforma
    When accedo a la sección de perfil de mi comercio
    Then el sistema me muestra la información completa de mi comercio: nombre, datos de contacto, estado actual y datos bancarios registrados

  @HU007 @error
  Scenario: No es posible consultar el perfil de otro comercio
    Given que estoy autenticado como administrador de mi comercio
    When intento acceder a la información de un comercio diferente al mío
    Then el sistema niega el acceso e informa que no tengo permisos para ver esa información

  @HU007 @error
  Scenario: Un comercio suspendido puede consultar su perfil pero no operar
    Given que mi comercio se encuentra en estado suspendido y estoy autenticado en la plataforma
    When accedo a la sección de perfil
    Then el sistema muestra mi información de perfil en modo solo lectura
    And el sistema indica claramente que mi cuenta está suspendida y que no puedo procesar ni modificar datos

  @HU007 @error
  Scenario: Acceso denegado sin autenticación
    Given que no estoy autenticado en la plataforma
    When intento acceder a la sección de perfil de un comercio
    Then el sistema rechaza la solicitud e indica que se requiere autenticación para acceder a este recurso
