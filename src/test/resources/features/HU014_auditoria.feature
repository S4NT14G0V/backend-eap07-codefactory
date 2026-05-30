# =============================================================================
# HU-14: Registro automático de eventos de auditoría
# Prioridad: 3 | Módulo: Auditoría / Observabilidad
# =============================================================================
# Como plataforma de pagos,
# Quiero registrar automáticamente eventos de auditoría inmutables,
# Para garantizar trazabilidad forense de todas las operaciones.

Feature: Registro automático de eventos de auditoría
  Como plataforma de pagos
  Quiero registrar eventos de auditoría automáticamente
  Para garantizar trazabilidad de todas las operaciones

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU014 @CP-S2-018 @camino-feliz
  Scenario: Auditoría de creación de transacción
    Given un comercio verificado con credenciales activas
    When se crea una transacción exitosamente
    Then se debe haber registrado un evento de auditoría
    And el evento debe contener "transactionId", "action" y "timestamp"
