# =============================================================================
# HU-15: Consulta del listado de pagos con filtros
# Prioridad: 3 | Módulo: Transactions / Query API
# =============================================================================
# Como comercio,
# Quiero consultar un listado de pagos con filtros por estado, fecha y monto,
# Para revisar el historial de transacciones de mi negocio.

Feature: Consulta del listado de pagos con filtros
  Como comercio registrado
  Quiero consultar mi historial de pagos con filtros
  Para revisar las transacciones de mi negocio

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU015 @CP-S2-020 @camino-feliz
  Scenario: Listado paginado de transacciones
    Given un comercio autenticado con credenciales activas
    And existen transacciones creadas para el comercio actual
    When se envía una solicitud GET a "/api/v1/transactions?page=0&size=10"
    Then la respuesta debe tener código 200
    And la respuesta debe contener una lista de transacciones
    And la respuesta debe incluir metadatos de paginación

  @HU015 @CP-S2-021 @seguridad
  Scenario: Aislamiento de datos entre comercios
    Given un comercio autenticado con credenciales activas
    And dos comercios diferentes con transacciones creadas
    When el comercio A consulta su listado de pagos
    Then no debe ver transacciones del comercio B
