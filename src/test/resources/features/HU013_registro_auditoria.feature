# =============================================================================
# HU013 - Registro de eventos de auditoría
# Épica: EP04 | Feature: FE401 | Sprint: Sprint 2 | Prioridad: Alta
# Story Points: 8 SP
# =============================================================================
# Como representante de un comercio,
# Quiero que la plataforma registre automáticamente cada cambio de estado de una
# transacción,
# Para garantizar la trazabilidad completa e íntegra de cada pago.
# =============================================================================

Feature: Registro y consulta de la bitácora inmutable de eventos de pago
  Como representante de un comercio
  Quiero que la plataforma registre automáticamente cada cambio de estado de una transacción
  Para garantizar la trazabilidad completa e íntegra de cada pago

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU013 @camino-feliz
  Scenario: Cada cambio de estado de un pago genera un registro de auditoría automático
    Given que el estado de un pago ha cambiado dentro del ciclo de vida de la transacción
    When el sistema persiste ese cambio de estado
    Then se crea automáticamente un registro de auditoría con: identificador del pago, tipo de evento, estado anterior, nuevo estado, quién realizó la acción y el momento exacto en que ocurrió
    And ese registro no puede ser editado ni eliminado bajo ninguna circunstancia

  @HU013 @camino-feliz
  Scenario: La integridad de la bitácora de auditoría puede ser verificada
    Given que existe una secuencia de eventos de auditoría registrados para un pago
    When el sistema verifica la integridad de esos registros
    Then cualquier alteración en un registro previo es detectada automáticamente por el sistema
    And el sistema genera una alerta indicando que la integridad de la bitácora ha sido comprometida

  @HU013 @camino-feliz
  Scenario: Un administrador autorizado puede consultar la bitácora de eventos de un pago
    Given que soy un administrador de la plataforma o un administrador del comercio dueño del pago, y estoy autenticado
    When consulto la bitácora de eventos de una transacción específica
    Then el sistema muestra la lista cronológica de todos los eventos registrados para ese pago
    And cada registro incluye: qué ocurrió, quién lo originó, desde qué estado venía, a qué estado pasó y cuándo sucedió exactamente

  @HU013 @error
  Scenario: No es posible modificar ni eliminar un registro de la bitácora
    Given que existe un registro de auditoría en la bitácora de la plataforma
    When cualquier actor (interno o externo) intenta modificar o eliminar ese registro
    Then el sistema rechaza la operación y el registro permanece intacto
    And el intento de modificación queda registrado como un evento de alerta de seguridad
