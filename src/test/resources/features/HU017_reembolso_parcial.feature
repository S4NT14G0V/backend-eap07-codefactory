# =============================================================================
# HU017 - Solicitud de reembolso parcial de un pago
# Épica: EP03 | Feature: FE303 | Sprint: Sprint 3 | Prioridad: Alta
# Story Points: 5 SP
# =============================================================================
# Como administrador del comercio,
# Quiero solicitar reembolsos parciales sobre un pago aprobado, controlando que
# la suma de devoluciones no supere el monto original cobrado,
# Para gestionar devoluciones de artículos individuales de un pedido sin tener
# que reembolsar el cobro completo.
# =============================================================================

Feature: Gestión de reembolsos parciales sobre pagos aprobados
  Como administrador del comercio
  Quiero solicitar reembolsos parciales sobre un pago aprobado
  Para gestionar devoluciones de artículos individuales sin tener que reembolsar el cobro completo

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU017 @camino-feliz
  Scenario: Reembolso parcial exitoso dentro del monto disponible
    Given que tengo un pago aprobado sobre el que quiero realizar una devolución parcial
    When solicito un reembolso por un monto menor al total del pago
    Then el sistema procesa el reembolso parcial y el pago queda marcado como parcialmente reembolsado
    And el monto disponible para futuros reembolsos se reduce en la cantidad ya devuelta

  @HU017 @error
  Scenario: No es posible reembolsar más del monto disponible
    Given que ya he realizado uno o más reembolsos parciales sobre un pago y hay un monto restante disponible para devolver
    When intento solicitar un reembolso por un monto mayor al que aún está disponible
    Then el sistema me informa que el monto solicitado supera el disponible para reembolso y no procesa la operación
