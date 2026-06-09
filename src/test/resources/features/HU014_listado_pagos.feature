# =============================================================================
# HU014 - Consulta del listado de pagos del comercio
# Épica: EP03 | Feature: FE301 | Sprint: Sprint 2 | Prioridad: Alta
# Story Points: 5 SP
# =============================================================================
# Como administrador de un comercio,
# Quiero filtrar el listado de las transacciones del comercio,
# Para monitorear la actividad de cobros de mi plataforma.
# =============================================================================

Feature: Consulta y seguimiento de transacciones de pago por parte del comercio
  Como administrador de un comercio
  Quiero filtrar el listado de las transacciones del comercio
  Para monitorear la actividad de cobros de mi plataforma

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU014 @camino-feliz
  Scenario: Consulta exitosa de pagos aprobados en un período determinado
    Given que tengo transacciones registradas en diferentes estados durante el último mes
    When consulto mis pagos filtrando solo los aprobados dentro de un rango de fechas y solicito la primera página de resultados
    Then el sistema me muestra el listado de pagos aprobados en ese período con la cantidad de resultados configurada
    And puedo navegar entre páginas para ver el resto de los resultados

  @HU014 @seguridad
  Scenario: El comercio solo puede consultar sus propios pagos
    Given que estoy autenticado como administrador de mi comercio
    When consulto el listado de mis pagos
    Then el sistema aplica automáticamente un filtro de comercio basado en mi sesión activa
    And el listado muestra únicamente las transacciones de mi comercio, sin incluir transacciones de otros comercios de la plataforma

  @HU014 @camino-feliz
  Scenario: Consulta con filtros que no retornan resultados devuelve una lista vacía
    Given que aplico filtros de búsqueda (estado, rango de fechas o monto) para los que no existe ninguna transacción en mi comercio
    When ejecuto la consulta
    Then el sistema devuelve una lista de resultados vacía
    And el sistema indica que no se encontraron transacciones para los filtros aplicados, sin generar un error

  @HU014 @error
  Scenario: Consulta con rango de fechas inválido no retorna resultados
    Given que intento consultar mis transacciones con un rango de fechas en el que la fecha de inicio es posterior a la fecha de fin
    When ejecuto la consulta
    Then el sistema informa que el rango de fechas no es válido y no devuelve ningún resultado
