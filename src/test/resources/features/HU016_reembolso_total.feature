# =============================================================================
# HU016 - Solicitud de reembolso total de un pago
# Épica: EP03 | Feature: FE303 | Sprint: Sprint 3 | Prioridad: Alta
# Story Points: 8 SP
# =============================================================================
# Como administrador del comercio,
# Quiero solicitar el reembolso completo del monto de un pago ya aprobado,
# Para devolver la totalidad del dinero al cliente en los casos en que sea necesario,
# manteniendo la trazabilidad completa del proceso.
# =============================================================================

Feature: Gestión de reembolsos totales y parciales sobre pagos aprobados
  Como administrador del comercio
  Quiero solicitar el reembolso completo del monto de un pago ya aprobado
  Para devolver la totalidad del dinero al cliente, manteniendo la trazabilidad completa del proceso

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU016 @camino-feliz
  Scenario: Reembolso total exitoso de un pago aprobado
    Given que tengo un pago aprobado que mi cliente solicita que sea devuelto en su totalidad
    When solicito el reembolso completo de ese pago indicando el motivo de la devolución
    Then el sistema registra la operación de reembolso y le asigna un identificador único
    And el pago original queda marcado como reembolsado en su totalidad
    And mi canal de notificación recibe el aviso del reembolso procesado

  @HU016 @error
  Scenario: No se puede reembolsar un pago que no está aprobado
    Given que el pago que quiero reembolsar fue rechazado o está en proceso
    When intento solicitar su reembolso
    Then el sistema me informa que ese pago no puede ser reembolsado porque no está en estado aprobado

  @HU016 @error
  Scenario: Un pago ya reembolsado en su totalidad no puede reembolsarse nuevamente
    Given que un pago ya fue reembolsado completamente con anterioridad
    When intento solicitar un nuevo reembolso sobre ese mismo pago
    Then el sistema me informa que ese pago ya fue reembolsado en su totalidad y no es posible procesar otro reembolso
