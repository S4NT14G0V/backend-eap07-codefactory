# =============================================================================
# HU010 - Rotación de credenciales sin interrupción del servicio
# Épica: EP02 | Feature: FE202 | Sprint: Sprint 2 | Prioridad: Media
# Story Points: 5 SP
# =============================================================================
# Como administrador de un comercio,
# Quiero rotar mis credenciales activas de forma que las nuevas sean generadas y
# las antiguas sigan siendo válidas temporalmente durante la transición,
# Para migrar mis sistemas a las nuevas credenciales sin interrumpir el servicio
# a mis clientes.
# =============================================================================

Feature: Ciclo de vida de las credenciales
  Como administrador de un comercio
  Quiero rotar mis credenciales activas de forma que las nuevas sean generadas y las antiguas sigan siendo válidas temporalmente durante la transición
  Para migrar mis sistemas a las nuevas credenciales sin interrumpir el servicio a mis clientes

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU010 @camino-feliz
  Scenario: Rotación exitosa con periodo de gracia para migración
    Given que tengo credenciales activas y quiero renovarlas de forma segura
    When solicito la rotación de mis credenciales actuales
    Then el sistema genera un nuevo juego de credenciales y me lo muestra una única vez para que el administrador las custodie
    And las credenciales anteriores permanecen válidas durante un período de gracia de 24 horas para permitir la migración de los sistemas de mi comercio
    And el sistema muestra claramente la fecha y hora exacta en que las credenciales anteriores serán revocadas automáticamente

  @HU010 @camino-feliz
  Scenario: Las credenciales antiguas son revocadas automáticamente al vencer el periodo de gracia
    Given que una rotación de credenciales fue iniciada y el periodo de gracia de 24 horas ha transcurrido
    When el sistema ejecuta el proceso automático de revocación
    Then las credenciales antiguas quedan inhabilitadas y ya no pueden utilizarse para autenticar solicitudes
    And el evento de revocación automática queda registrado en la bitácora de auditoría

  @HU010 @error
  Scenario: No es posible rotar credenciales cuando se ha alcanzado el límite máximo de credenciales activas
    Given que mi comercio ya tiene el número máximo de credenciales activas permitidas en este entorno
    When intento iniciar una rotación que generaría un nuevo juego de credenciales
    Then el sistema informa que se ha alcanzado el límite de credenciales activas
    And sugiere revocar alguna credencial existente antes de continuar con la rotación