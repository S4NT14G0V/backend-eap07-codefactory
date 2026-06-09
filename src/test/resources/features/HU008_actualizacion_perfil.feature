# =============================================================================
# HU008 - Actualización de información bancaria del comercio
# Épica: EP01 | Feature: FE102 | Sprint: Sprint 2 | Prioridad: Media
# Story Points: 5 SP
# =============================================================================
# Como administrador de un comercio,
# Quiero actualizar la información bancaria del comercio,
# Para mantener dichos datos vigentes, garantizando que los fondos se transfieran
# correctamente a mi cuenta.
# =============================================================================

Feature: Consulta y administración del perfil de un comercio registrado
  Como administrador de un comercio
  Quiero actualizar la información bancaria del comercio
  Para mantener dichos datos vigentes, garantizando que los fondos se transfieran correctamente a mi cuenta

  Background:
    Given el sistema de pagos está listo para recibir peticiones

  @HU008 @camino-feliz
  Scenario: Actualización exitosa de datos de contacto del comercio
    Given que soy el administrador de mi comercio y estoy autenticado en la plataforma
    When modifico los datos de contacto (correo, teléfono o dirección) y guardo los cambios
    Then el sistema confirma que la información fue actualizada correctamente
    And el sistema registra en la bitácora qué campos fueron modificados, por quién y en qué momento

  @HU008 @camino-feliz
  Scenario: Actualización de datos bancarios queda en estado de verificación pendiente
    Given que soy el administrador de mi comercio y estoy autenticado en la plataforma
    When modifico los datos de mi cuenta bancaria y guardo los cambios
    Then el sistema confirma que la solicitud de cambio fue recibida
    And los nuevos datos bancarios quedan en estado de verificación pendiente hasta ser validados por el equipo de la plataforma
    And mientras la verificación esté pendiente, las liquidaciones continúan realizándose a la cuenta bancaria anterior
    And el sistema registra en la bitácora los campos modificados, el autor del cambio y el momento exacto

  @HU008 @error
  Scenario: No es posible modificar datos de identificación del comercio
    Given que intento modificar el número de identificación fiscal o el tipo de negocio registrados al crear la cuenta
    When guardo los cambios
    Then el sistema me informa que esos datos son de registro único y no pueden modificarse
    And el sistema sugiere contactar al equipo de soporte si hay un error en los datos originales

  @HU008 @error
  Scenario: Usuario con el rol de Desarrollador no puede modificar el perfil del comercio
    Given que estoy autenticado en la plataforma con el rol de desarrollador
    When intento actualizar los datos de perfil o configuración bancaria del comercio
    Then el sistema rechaza la acción e informa que mi rol no tiene permisos para modificar esta información

  @HU008 @error
  Scenario: Intento de actualización con datos bancarios en formato inválido
    Given que soy el administrador de mi comercio y estoy autenticado
    When ingreso datos bancarios con un formato incorrecto (número de cuenta con caracteres no numéricos o con una longitud inválida) e intento guardar
    Then el sistema rechaza la actualización e indica qué campos tienen un formato inválido
    And los datos bancarios anteriores permanecen sin cambios