# =============================================================================
# HU011 - Procesamiento de un pago
# Épica: EP03 | Feature: FE302 | Sprint: Sprint 2 | Prioridad: Crítica
# Story Points: 8 SP
# =============================================================================
# Como motor de procesamiento de pagos de la plataforma,
# Quiero actualizar el estado de cada transacción según la respuesta recibida del
# procesador de pagos externo,
# Para reflejar el resultado real de cada cobro y habilitar las notificaciones
# correspondientes al comercio y al usuario que realizó el pago.
# =============================================================================

Feature: Procesamiento del resultado de pagos por parte del procesador financiero externo
  Como motor de procesamiento de pagos de la plataforma
  Quiero actualizar el estado de cada transacción según la respuesta recibida del procesador de pagos externo
  Para reflejar el resultado real de cada cobro y habilitar las notificaciones correspondientes

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU011 @camino-feliz
  Scenario: Pago aprobado por el procesador financiero externo
    Given que el procesador financiero externo confirma la aprobación de la transacción
    When el sistema recibe y procesa esa confirmación
    Then el pago queda marcado como aprobado en la plataforma
    And el comercio recibe la notificación del pago aprobado a través de su canal de notificaciones configurado
    And el evento queda registrado en la bitácora de auditoría del pago

  @HU011 @camino-feliz
  Scenario Outline: Pago rechazado por el procesador financiero según el motivo de rechazo
    Given que el procesador financiero externo rechaza la transacción por <motivo_rechazo>
    When el sistema procesa el rechazo
    Then el pago queda marcado como rechazado con el motivo correspondiente en términos de negocio: <descripcion_negocio>
    And el comercio recibe la notificación del rechazo con el motivo de negocio correspondiente

    Examples:
      | motivo_rechazo       | descripcion_negocio                                                      |
      | fondos insuficientes | El medio de pago del cliente no tiene saldo suficiente                    |
      | tarjeta expirada     | El medio de pago presentado está vencido                                  |
      | datos incorrectos    | Los datos del medio de pago no coinciden con los registrados              |
      | tarjeta bloqueada    | El medio de pago fue bloqueado por la institución financiera del cliente  |

  @HU011 @error
  Scenario: Fallo de la transacción por error de comunicación con el procesador externo
    Given que la plataforma intenta comunicarse con el procesador financiero externo para procesar la transacción
    When el procesador no responde dentro del tiempo máximo de espera o retorna un error de conectividad
    Then el pago queda marcado como fallido (FAILED) en la plataforma
    And el comercio recibe la notificación indicando que el pago no pudo ser procesado por un error técnico
    And el evento queda registrado en la bitácora con el detalle del error de comunicación
