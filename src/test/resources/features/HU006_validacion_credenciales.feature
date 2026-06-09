# =============================================================================
# HU006 - Validación de credenciales por solicitud de pago
# Épica: EP02 | Feature: FE201 | Sprint: Sprint 2 | Prioridad: Crítica
# Story Points: 5 SP
# =============================================================================
# Como administrador de la plataforma de pagos,
# Quiero que el sistema verifique en cada solicitud de pago que las credenciales del
# comercio sean válidas,
# Para garantizar que únicamente los comercios autorizados puedan realizar transacciones.
# =============================================================================

Feature: Autenticación y generación de credenciales
  Como administrador de la plataforma de pagos
  Quiero que el sistema verifique en cada solicitud de pago que las credenciales del comercio sean válidas
  Para garantizar que únicamente los comercios autorizados puedan realizar transacciones

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU006 @camino-feliz
  Scenario: Solicitud de pago con credenciales válidas
    Given que las credenciales del comercio están vigentes
    And las credenciales tienen permisos para realizar transacciones
    When el sistema recibe una transacción del comercio
    Then el pago generado por el comercio es autorizado
    And el saldo del comercio es modificado según el valor del pago

  @HU006 @error
  Scenario: Solicitud de pago no exitosa debido a permisos insuficientes
    Given que las credenciales del comercio no tienen permiso para realizar transacciones
    When el sistema intenta usar las credenciales para iniciar una transacción
    Then el sistema rechaza la solicitud de pago
    And muestra un mensaje al comercio que sus credenciales no tienen el permiso requerido para esa operación
    And el saldo del comercio no es modificado

  @HU006 @error
  Scenario: Solicitud de pago con credenciales de otro comercio
    Given que las credenciales ingresadas corresponden a otro comercio
    When el comercio intenta usarlas para realizar operaciones
    Then el sistema rechaza la solicitud
    And muestra un mensaje de que las credenciales no son válidas