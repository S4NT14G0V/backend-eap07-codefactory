# **Plataforma de APIs de Pagos Embebidos tipo Stripe — Caso 8**

# **Documento de Casos de Prueba**

Equipo Avanzado Presencial \#07 — CodeF@ctory — Universidad de Antioquia

Versión 2.0 · Sprints 1 – 2 · 24 HU · Documento de Casos de Prueba

## **Introducción**

Este documento contiene los casos de prueba correspondientes a las historias de usuario comprometidas en los Sprint 1 y 2 para la “Plataforma de APIs de Pagos Embebidos tipo Stripe – Caso 8”, desarrollado por el equipo AP07 de la Fábrica Escuela.

Los casos fueron diseñados a partir de los criterios de aceptación en lenguaje Gherkin redactadas en el Backlog de HU en Azure DevOps y cubren escenarios de camino feliz, excepción, seguridad e idempotencia.

Los pasos de ejecución distinguen entre validación funcional observable y verificaciones técnicas complementarias. Las precondiciones describen el estado del sistema requerido sin incluir detalles de implementación interna.

La estructura de campos propuesta para cada uno de estos casos de prueba es:

1. **ID:** Identificador único del caso de prueba.  
2. **HU donde se aplica:** Historia de usuario que abarca el caso de prueba.  
3. **Descripción de la prueba:** Detalle de aquello que se está verificando con la realización del caso de prueba.  
4. **Supuestos y precondiciones:** Condiciones iniciales que se deben cumplir antes de la ejecución de la prueba.  
5. **Pasos y condiciones de ejecución:** Acciones paso a paso que realiza la prueba, en orden secuencial.  
6. **Resultado esperado:** Descripción del comportamiento que se espera que el sistema exhiba después de la correcta ejecución de los pasos de una prueba, asumiendo que se cumplen todas las precondiciones y supuestos.  
7. **Estado del caso de prueba: I**ndica la situación actual del caso de prueba dentro del ciclo de pruebas. Por ejemplo: No ejecutado significa que la prueba no ha sido puesta en acción.  
8. **Resultado obtenido:** Descripción del comportamiento real observado tras ejecutar la prueba.  
9. **Errores asociados:** Defectos relacionados con la ejecución fallida del caso de prueba. Puede incluir identificadores de bugs, mensajes de error.  
10. **Responsable de ejecución:** Persona encargada de ejecutar y documentar el caso de prueba.  
11. **Tipo de prueba:** Objetivo o naturaleza de la prueba realizada, por ejemplo: camino feliz, error, seguridad, idempotencia.

# **Sprint 1**

| HU001 \- Registro de nuevo comercio |  |
| :---: | ----- |

| CP-S1-001  ·  Registro exitoso de un nuevo comercio en la plataforma |  |
| ----- | :---- |
| **ID** | **CP-S1-001** |
| **HU donde se aplica** | **HU001 — Registro de nuevo comercio** |
| **Descripción de la prueba** | **Verificar que un representante de un comercio puede completar el registro en la plataforma con datos legales válidos y que el sistema confirme el registro asignando un identificador único.** |
| **Supuestos	y precondiciones** | **El endpoint de registro está disponible. No existe previamente un comercio con el mismo correo electrónico ni el mismo taxId. Todos los campos obligatorios del formulario están disponibles para ser enviados.** |
| **Pasos y condiciones de ejecución** | **Construir el payload con datos válidos: nombre='Mi Tienda SAS', taxId='901234567-8',	[email='admin@mitienda.com',](mailto:email%3D%27admin@mitienda.com) businessType='RETAIL'. Enviar POST /api/merchants/register con el payload.c Verificar el código de respuesta HTTP. Confirmar que la respuesta incluye un merchantId único y el estado PENDING\_VERIFICATION. Confirmar que el comercio recibió un correo de bienvenida con los pasos a seguir. *Verificaciones técnicas complementarias:* Confirmar que el merchantId generado es un UUID v4. Verificar que el evento de creación quedó registrado en la bitácora con timestamp UTC e IP de origen.** |
| **Resultado esperado** | **HTTP 201 Created. El sistema confirma el registro exitoso del comercio, asigna un identificador único (merchantId) y la cuenta queda en estado PENDING\_VERIFICATION hasta ser aprobada por un administrador.** |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS \-** HTTP 201\. merchantId retornado con formato mch\_\*. businessName coincide con el enviado. Status: VERIFIED. ("IntegrationApiTest.cp\_s1\_001\_registerMerchantSuccess") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | **Camino feliz** |

| CP-S1-002  ·  Falla en el registro del comercio por error interno de la plataforma |  |
| :---- | :---- |
| **ID** | **CP-S1-002** |
| **HU donde se aplica** | **HU001 — Registro de nuevo comercio** |
| **Descripción de la prueba** | **Verificar que cuando la plataforma experimenta un error interno durante el registro, el sistema informa al representante que no fue posible completar la operación y no deja al comercio parcialmente registrado.** |
| **Supuestos	y precondiciones** | **El comercio no existe previamente en la plataforma. Se ha simulado un fallo en la capa de persistencia del sistema (ej.: timeout de base de datos) en el entorno de pruebas. El endpoint de registro está disponible.** |
| **Pasos y condiciones de ejecución** | **Activar la simulación de fallo en la capa de persistencia del entorno de pruebas. Construir el payload con los mismos datos válidos que CP-S1-001. Enviar POST /api/merchants/register. Verificar el código de respuesta HTTP y el mensaje retornado. Confirmar que no existe ningún registro parcial del comercio en el sistema.** |
| **Resultado esperado** | **HTTP 500 Internal Server Error. El sistema informa que no fue posible completar el registro e indica al representante que intente nuevamente más tarde. El comercio no queda registrado en la plataforma.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Error** |

| CP-S1-003 · Intento de registro con correo electrónico ya registrado en la plataforma |  |
| :---- | :---- |
| **ID** | **CP-S1-003** |
| **HU donde se aplica** | **HU001 — Registro de nuevo comercio** |
| **Descripción de la prueba** | **Verificar que el sistema impide el registro de un nuevo comercio cuando el correo electrónico ya está asociado a otro comercio existente, evitando duplicados de identidad en la plataforma.** |
| **Supuestos	y precondiciones** | **Ya existe un comercio registrado con el email ['admin@mitienda.com'.](mailto:%27admin@mitienda.com) El nuevo comercio intenta registrarse con el mismo correo pero un taxId diferente. El endpoint de registro está disponible.** |
| **Pasos y condiciones de ejecución** | **Confirmar	que	existe	un	comercio	registrado	con [email='admin@mitienda.com'.](mailto:email%3D%27admin@mitienda.com) Construir payload de un segundo comercio usando el mismo email y un taxId diferente. Enviar POST /api/merchants/register con el nuevo payload. Verificar el código de respuesta HTTP y el mensaje de error retornado. Confirmar que no se creó un segundo comercio en la plataforma.** |
| **Resultado esperado** | **HTTP 409 Conflict. El sistema informa que el correo electrónico ya está en uso y no permite continuar el registro. No se crea ningún comercio adicional en la plataforma.** |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS \-** HTTP 409 Conflict. errorCode: BUSINESS\_RULE\_VIOLATION. Mensaje contiene "correo electrónico". ("IntegrationApiTest.cp\_s1\_003\_registerDuplicateEmail") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | **Error** |

| CP-S1-004  ·  Intento de registro con campo obligatorio vacío |  |
| ----- | :---- |
| **ID** | **CP-S1-004** |
| **HU donde se aplica** | **HU001 — Registro de nuevo comercio** |
| **Descripción de la prueba** | **Verificar que el sistema rechaza el registro cuando alguno de los campos obligatorios (nombre, taxId, email o tipo de negocio) no ha sido diligenciado, informando cuál campo es requerido.** |
| **Supuestos	y precondiciones** | **El endpoint de registro está disponible. No se requiere ningún estado previo del comercio. Se ejecuta una variante por cada campo obligatorio omitido.** |
| **Pasos y condiciones de ejecución** | **Construir un payload de registro dejando vacío el campo 'nombre del comercio'. Enviar POST /api/merchants/register. Verificar el código de respuesta HTTP y confirmar que el mensaje indica que el campo es obligatorio. Repetir los pasos 1 a 3 omitiendo el campo 'taxId'. Repetir los pasos 1 a 3 omitiendo el campo 'correo electrónico'. Repetir los pasos 1 a 3 omitiendo el campo 'tipo de negocio'. Confirmar que en ninguno de los casos se creó un comercio.** |
| **Resultado esperado** | **HTTP 400 Bad Request en cada variante. El sistema indica que el campo correspondiente (nombre / taxId / correo / tipo de negocio) es obligatorio y no permite continuar hasta que sea completado. No se crea ningún comercio.** |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 400 Bad Request. errorCode: VALIDATION\_ERROR para todos los campos vacíos. (": IntegrationApiTest.cp\_s1\_004\_registerBlankFields") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | **Error** |

| CP-S1-005 ·  Generación exitosa de credenciales para un comercio verificado |  |
| :---- | :---- |
| **ID** | **CP-S1-005** |
| **HU donde se aplica** | **HU002 — Generación de credenciales de acceso** |
| **Descripción de la prueba** | **Verificar que un desarrollador de un comercio activo y aprobado puede generar un conjunto de credenciales de acceso a la plataforma y que la secretKey se muestra únicamente en el momento de la creación.** |
| **Supuestos	y precondiciones** | **El comercio existe y está en estado ACCEPTED. El comercio tiene menos de 5 credenciales activas en el entorno. Endpoint POST /api/merchants/{id}/credentials disponible.** |
| **Pasos y condiciones de ejecución** | **Autenticarse como desarrollador del comercio en estado ACCEPTED. Enviar POST /api/merchants/{merchantId}/credentials. Verificar el código de respuesta HTTP. Confirmar que la respuesta contiene una publicKey y una secretKey. Verificar que la secretKey solo es visible en esta respuesta y no puede recuperarse posteriormente. *Verificaciones técnicas complementarias:* Confirmar  que  el  formato  de  la  publicKey  sigue el patrón pk\_{entorno}\_{uuid}. Confirmar  que  el  formato  de  la  secretKey  sigue el patrón sk\_{entorno}\_{uuid}.** |
| **Resultado esperado** | **HTTP 201 Created. El sistema genera un conjunto de credenciales válidas (publicKey y secretKey). La secretKey se muestra en texto plano únicamente en este momento; el desarrollador debe guardarla de forma segura. Las credenciales quedan disponibles para autenticar solicitudes de pago.** |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 201 Created. publicId con formato pk\_\*, secret con formato sk\_\*. (": IntegrationApiTest.cp\_s1\_005\_generateCredentials") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | **Camino feliz** |

| CP-S1-006  ·  Comercio no verificado intenta generar credenciales |  |
| ----- | :---- |
| **ID** | **CP-S1-006** |
| **HU donde se aplica** | **HU002 — Generación de credenciales de acceso** |
| **Descripción de la prueba** | **Verificar que el sistema impide la generación de credenciales cuando el comercio no ha sido aprobado por la plataforma, garantizando que únicamente comercios verificados puedan operar.** |
| **Supuestos	y precondiciones** | **El comercio está en estado PENDING\_VERIFICATION o SUSPENDED. No existen credenciales previas generadas para este comercio. Endpoint POST /api/merchants/{id}/credentials disponible.** |
| **Pasos y condiciones de ejecución** | **Identificar un comercio en estado PENDING\_VERIFICATION o SUSPENDED. Intentar enviar POST /api/merchants/{merchantId}/credentials. Verificar el código de respuesta HTTP y el mensaje retornado. Confirmar que no se generaron credenciales.** |
| **Resultado esperado** | **HTTP 403 Forbidden. El sistema impide la generación de credenciales e informa que el comercio debe estar activo y verificado para operar en la plataforma. No se genera ninguna credencial.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Error — Seguridad** |

| CP-S1-007  ·  Intento de generar credenciales al alcanzar el límite máximo permitido |  |
| ----- | :---- |
| **ID** | **CP-S1-007** |
| **HU donde se aplica** | **HU002 — Generación de credenciales de acceso** |
| **Descripción de la prueba** | **Verificar que el sistema impide la generación de una nueva credencial cuando el comercio ya ha alcanzado el límite de 5 credenciales activas, y sugiere revocar alguna existente.** |
| **Supuestos	y precondiciones** | **El comercio está en estado ACCEPTED. El comercio ya tiene 5 credenciales en estado ACTIVE (límite máximo por defecto). Endpoint POST /api/merchants/{id}/credentials disponible.** |
| **Pasos y condiciones de ejecución** | **Confirmar que el comercio tiene exactamente 5 credenciales en estado ACTIVE. Intentar enviar POST /api/merchants/{merchantId}/credentials. Verificar el código de respuesta HTTP y el mensaje retornado. Confirmar que el mensaje sugiere revocar una credencial existente antes de crear una nueva. Confirmar que no se generó ninguna credencial adicional.** |
| **Resultado esperado** | **HTTP 422 Unprocessable Entity. El sistema informa que se ha alcanzado el límite de credenciales activas permitidas y sugiere revocar alguna credencial existente antes de generar una nueva. No se genera ninguna credencial adicional.** |
| **Estado del caso de prueba** | **No ejecutado** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Error** |

| HU003 \- Creación de una transacción de pago |  |
| :---: | ----- |

| CP-S1-008  ·  Inicio exitoso de un pago con datos completos y válidos |  |
| :---- | :---- |
| **ID** | **CP-S1-008** |
| **HU donde se aplica** | **HU003 — Creación de una transacción de pago** |
| **Descripción de la prueba** | **Verificar que el sistema del comercio puede registrar exitosamente una solicitud de pago con todos los datos requeridos y que la transacción queda en estado CREATED disponible para seguimiento.** |
| **Supuestos	y precondiciones** | **El	comercio	tiene	credenciales	activas	y	válidas	(scope payments:write). El método de pago ya está tokenizado; no se aceptan datos crudos de tarjeta. No existe una transacción previa con la misma Idempotency-Key. Endpoint POST /api/payments disponible.** |
| **Pasos y condiciones de ejecución** | **Construir	el	payload:	amount=10000	(COP),	currency='COP', paymentToken='tok\_visa\_approved', Idempotency-Key='unica-123'. Incluir las credenciales válidas en el header Authorization. Enviar POST /api/payments. Verificar el código de respuesta HTTP. Confirmar que la respuesta incluye un paymentId único y el estado CREATED. Consultar GET /api/payments/{paymentId} y verificar que el pago está disponible para seguimiento.** |
| **Resultado esperado** | **HTTP 201 Created. El sistema registra la transacción y retorna un paymentId único con estado CREATED. El equipo del comercio puede consultar el estado del pago en cualquier momento.** |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 201 Created. paymentId retornado con status CREATED. GET /transactions/{id} retorna 200 con los mismos datos. (": IntegrationApiTest.cp\_s1\_008\_createTransactionSuccess \+ cp\_s1\_008\_getTransaction") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | **Camino feliz** |

| CP-S1-009  ·  Prevención de cobros duplicados para una misma operación |  |
| :---- | :---- |
| **ID** | **CP-S1-009** |
| **HU donde se aplica** | **HU003 — Creación de una transacción de pago** |
| **Descripción de la prueba** | **Verificar que el mecanismo de idempotencia impide el registro de un cobro duplicado cuando se reenvía exactamente la misma solicitud con la misma Idempotency-Key, retornando la información del pago ya registrado.** |
| **Supuestos	y precondiciones** | **Existe una transacción ya creada con Idempotency-Key='key123'. Las credenciales del comercio están activas. Se reenvía exactamente la misma solicitud (mismo payload y misma clave). Endpoint POST /api/payments disponible.** |
| **Pasos y condiciones de ejecución** | **Enviar POST /api/payments con Idempotency-Key='key123' y payload válido (primer registro). Confirmar que el primer pago queda en estado CREATED con un paymentId asignado. Enviar exactamente la misma solicitud nuevamente con la misma Idempotency-Key='key123'. Verificar el código de respuesta HTTP del segundo envío. Confirmar que la respuesta retorna el mismo paymentId del primer registro, sin crear uno nuevo. Confirmar que no existen dos transacciones con la misma Idempotency-Key.** |
| **Resultado esperado** | **HTTP 200 OK en el segundo envío. El sistema detecta la operación duplicada, la ignora y retorna la información del pago previamente registrado. No se genera un segundo cobro ni un nuevo paymentId.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Idempotencia** |

| CP-S1-010  ·  Intento de pago con monto inválido |  |
| ----- | :---- |
| **ID** | **CP-S1-010** |
| **HU donde se aplica** | **HU003 — Creación de una transacción de pago** |
| **Descripción de la prueba** | **Verificar que el sistema rechaza una solicitud de pago cuando el monto es inválido (cero, negativo o ausente), informando al comercio que el monto no es válido para procesar el pago.** |
| **Supuestos	y precondiciones** | **El comercio tiene credenciales activas y válidas. Se ejecuta una variante por cada tipo de monto inválido: cero, negativo y ausente. Endpoint POST /api/payments disponible.** |
| **Pasos y condiciones de ejecución** | **Construir payload con amount=0 y los demás datos válidos. Enviar POST /api/payments y verificar rechazo. Construir payload con amount=-500 y los demás datos válidos. Enviar POST /api/payments y verificar rechazo. Construir payload sin el campo amount. Enviar POST /api/payments y verificar rechazo. En cada variante confirmar que el mensaje de error indica que el monto no es válido. Confirmar que no se registró ninguna transacción en ninguna de las variantes.** |
| **Resultado esperado** | **HTTP 400 Bad Request en las tres variantes. El sistema rechaza la operación e informa que el monto no es válido para procesar el pago. No se genera ninguna transacción.** |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 400 para amount=0 y amount=-500. errorCode: VALIDATION\_ERROR en ambos casos. (": IntegrationApiTest.cp\_s1\_010\_createTransactionZeroAmount \+ cp\_s1\_010\_createTransactionNegativeAmount") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | **Error** |

| CP-S1-011 · Intento de pago con credenciales inválidas |  |
| ----- | :---- |
| **ID** | **CP-S1-011** |
| **HU donde se aplica** | **HU003 — Creación de una transacción de pago** |
| **Descripción de la prueba** | **Verificar que el sistema rechaza una solicitud de pago cuando las credenciales del comercio han sido revocadas, han expirado o son incorrectas, garantizando que únicamente comercios autorizados puedan procesar pagos.** |
| **Supuestos	y precondiciones** | **El comercio es válido en la plataforma. Las credenciales a utilizar están en estado REVOKED, han superado su vigencia de 365 días, o la secretKey es incorrecta. Endpoint POST /api/payments disponible.** |
| **Pasos y condiciones de ejecución** | **Obtener una secretKey revocada o expirada del comercio. Construir payload de pago válido (amount=10000, currency='COP', paymentToken válido). Incluir la credencial inválida en el header Authorization: Bearer \<secretKey\_invalida\>. Enviar POST /api/payments. Verificar el código de respuesta HTTP y el mensaje de error. Confirmar que no se registró ninguna transacción.** |
| **Resultado esperado** | **HTTP 401 Unauthorized. El sistema rechaza la solicitud e informa que las credenciales no son válidas. El acceso queda denegado y no se registra ninguna transacción.** |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 401 para credenciales falsas (errorCode: INVALID\_CREDENTIALS) y HTTP 401 para credenciales ausentes (errorCode: MISSING\_CREDENTIALS). (": IntegrationApiTest.cp\_s1\_011\_createTransactionInvalidCredentials \+ cp\_s1\_011\_createTransactionMissingCredentials") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | **Error — Seguridad** |

| HU004 \- Asignación del estado inicial de un pago |  |
| :---: | ----- |

| CP-S1-012 · Asignación exitosa del estado inicial al iniciar el procesamiento de un pago |  |
| :---- | :---- |
| **ID** | **CP-S1-012** |
| **HU donde se aplica** | **HU004 — Asignación del estado inicial de un pago** |
| **Descripción de la prueba** | **Verificar que cuando el sistema inicia el procesamiento de un pago registrado en estado CREATED, la transacción transiciona a estado PROCESSING y el comercio puede consultar el avance.** |
| **Supuestos	y precondiciones** | **Existe una transacción en estado CREATED (creada con HU003). El sistema interno está disponible para iniciar el procesamiento. La máquina de estados del pago permite la transición CREATED → PROCESSING.** |
| **Pasos y condiciones de ejecución** | **Identificar un pago en estado CREATED con su paymentId. Activar el inicio del procesamiento de la transacción por parte del sistema. Consultar GET /api/payments/{paymentId}. Verificar que el estado del pago es ahora PROCESSING. Confirmar que el comercio puede consultar el estado y sabe que el pago está en proceso. *Verificaciones técnicas complementarias:* · Verificar en la bitácora: estado anterior (CREATED), estado nuevo (PROCESSING), actorID y timestamp UTC.** |
| **Resultado esperado** | **El pago transiciona de estado CREATED a PROCESSING. El comercio puede consultar el estado actualizado y sabe que la transacción está en proceso de validación. La transición queda registrada en la bitácora de auditoría.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Camino feliz — Máquina de estados** |

| CP-S1-013  ·  Transición de estado no válida sobre un pago ya finalizado |  |
| :---- | :---- |
| **ID** | **CP-S1-013** |
| **HU donde se aplica** | **HU004 — Asignación del estado inicial de un pago** |
| **Descripción de la prueba** | **Verificar que el sistema impide cualquier cambio de estado sobre un pago que ya se encuentra en un estado terminal (APPROVED, REJECTED, FAILED o REFUNDED), garantizando la consistencia del ciclo de vida del pago.** |
| **Supuestos	y precondiciones** | **Existe un pago en un estado terminal: APPROVED, REJECTED, FAILED o REFUNDED. Se intenta retroceder su estado a PROCESSING mediante un reintento o acción del sistema. La máquina de estados del pago tiene las transiciones inválidas configuradas.** |
| **Pasos y condiciones de ejecución** | **Identificar un pago en estado APPROVED con su paymentId. Intentar ejecutar una acción que cambie el estado del pago a PROCESSING. Verificar el código de respuesta o excepción retornada por el sistema. Consultar GET /api/payments/{paymentId} y confirmar que el estado sigue siendo APPROVED. Confirmar que el estado del pago no fue modificado.** |
| **Resultado esperado** | **El sistema rechaza el intento de cambio de estado e informa que el pago ya se encuentra en un estado final y no puede ser modificado. El estado del pago permanece inalterado. La transición inválida no persiste.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Error — Máquina de estados** |

| HU005 \- Autenticación con doble factor |  |
| :---: | ----- |

| CP-S1-014  ·  Inicio de sesión exitoso con segundo factor configurado |  |
| :---- | :---- |
| **ID** | **CP-S1-014** |
| **HU donde se aplica** | **HU005 — Autenticación con doble factor** |
| **Descripción de la prueba** | **Verificar que un administrador de la plataforma con segundo factor configurado puede iniciar sesión correctamente proporcionando sus credenciales y un código TOTP vigente.** |
| **Supuestos	y precondiciones** | **El administrador existe en la plataforma y tiene MFA configurado (TOTP). El código TOTP a utilizar está vigente dentro de la ventana de tolerancia (±1 intervalo de 30 segundos). Endpoint POST /api/auth/login disponible.** |
| **Pasos y condiciones de ejecución** | **Construir el payload: [email='admin@platform.com',](mailto:email%3D%27admin@platform.com) password correcta y código TOTP vigente (ej.: 123456). Enviar POST /api/auth/login con el payload. Verificar el código de respuesta HTTP. Confirmar que la respuesta incluye un token de acceso válido. Confirmar que el administrador puede acceder a los recursos de la plataforma con el token recibido.** |
| **Resultado esperado** | **HTTP 200 OK. La sesión se inicia correctamente. El sistema retorna un token de acceso que permite al administrador operar en la plataforma de forma segura.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Camino feliz — Seguridad** |

| CP-S1-015 · Cuenta bloqueada temporalmente por múltiples intentos fallidos de acceso |  |
| :---- | :---- |
| **ID** | **CP-S1-015** |
| **HU donde se aplica** | **HU005 — Autenticación con doble factor** |
| **Descripción de la prueba** | **Verificar que el sistema bloquea temporalmente la cuenta de un administrador cuando se detectan 5 intentos fallidos de autenticación en un período de 10 minutos, notificando el evento como un posible riesgo de seguridad.** |
| **Supuestos	y precondiciones** | **El administrador existe en la plataforma. Se ejecutan 5 intentos fallidos de autenticación (contraseña o TOTP erróneos) en menos de 10 minutos. Endpoint POST /api/auth/login disponible.** |
| **Pasos y condiciones de ejecución** | **Enviar 5 solicitudes POST /api/auth/login consecutivas con credenciales incorrectas para la misma cuenta. Confirmar que cada intento retorna error de autenticación. En el sexto intento, usar las credenciales correctas (contraseña y TOTP válido). Verificar el código de respuesta HTTP del sexto intento. Confirmar que el mensaje indica que la cuenta está bloqueada temporalmente. Confirmar que el administrador recibió una notificación sobre el bloqueo.** |
| **Resultado esperado** | **HTTP 423 Locked en el sexto intento. El sistema bloquea temporalmente el acceso a la cuenta e informa al administrador que debe revisar su correo. El evento queda registrado como un posible riesgo de seguridad.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Error — Seguridad** |

| CP-S1-016  ·  Inicio de sesión con código de segundo factor inválido o expirado |  |
| :---- | :---- |
| **ID** | **CP-S1-016** |
| **HU donde se aplica** | **HU005 — Autenticación con doble factor** |
| **Descripción de la prueba** | **Verificar que el sistema rechaza el acceso cuando el administrador proporciona credenciales correctas pero un código TOTP incorrecto o perteneciente a un intervalo anterior, sin revelar cuál de los factores falló.** |
| **Supuestos	y precondiciones** | **El administrador existe en la plataforma y tiene MFA configurado. La contraseña proporcionada es correcta. El código TOTP proporcionado es incorrecto o pertenece a un intervalo anterior (expirado). Endpoint POST /api/auth/login disponible.** |
| **Pasos y condiciones de ejecución** | **Construir el payload con email y contraseña correctos y un TOTP inválido (ej.: 000000). Enviar POST /api/auth/login. Verificar el código de respuesta HTTP. Confirmar que el mensaje indica que el código de verificación es inválido o expirado. Confirmar que no se retornó ningún token de acceso. Repetir los pasos 1 a 5 con un código TOTP de un intervalo anterior.** |
| **Resultado esperado** | **HTTP 401 Unauthorized. El sistema rechaza el acceso e informa que el código de verificación es inválido o expirado. No se retorna ningún token de acceso. El mensaje de error no revela cuál de los dos factores falló.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Error — Seguridad** |

| CP-S1-017  ·  Inicio de sesión sin método de segundo factor configurado |  |
| :---- | :---- |
| **ID** | **CP-S1-017** |
| **HU donde se aplica** | **HU005 — Autenticación con doble factor** |
| **Descripción de la prueba** | **Verificar que el sistema dirige al administrador a configurar un método de segundo factor cuando intenta iniciar sesión sin tenerlo configurado, impidiendo el acceso hasta que el MFA esté activo.** |
| **Supuestos	y precondiciones** | **El administrador existe en la plataforma pero no tiene MFA configurado. El administrador envía únicamente correo y contraseña. Endpoint POST /api/auth/login disponible.** |
| **Pasos y condiciones de ejecución** | **Construir el payload con email y contraseña correctos, sin incluir código TOTP. Enviar POST /api/auth/login. Verificar el código de respuesta HTTP. Confirmar que el mensaje indica que debe configurarse un método de segundo factor antes de iniciar sesión. Confirmar que no se retornó ningún token de acceso.** |
| **Resultado esperado** | **HTTP 403 Forbidden. El sistema informa al administrador que debe configurar un método de autenticación de segundo factor antes de poder iniciar sesión. No se otorga acceso a la plataforma.** |
| **Estado del caso de prueba** | **NO EJECUTABLE** |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | **Error — Seguridad** |

# **Sprint 2**

| HU006 — Validación de credenciales por solicitud de pago |  |
| :---: | ----- |

| CP-S2-001 · Solicitud de pago autorizada con credenciales válidas y scope suficiente |  |
| :---- | :---- |
| **ID** | CP-S2-001 |
| **HU donde se aplica** | HU006 — Validación de credenciales por solicitud de pago |
| **Descripción de la prueba** | Verificar que el sistema autoriza una solicitud de pago cuando las credenciales del comercio están activas, vigentes y tienen el scope payments:write. |
| **Supuestos	y precondiciones** | Comercio en estado ACCEPTED con credenciales activas (scope payments:write). Token de método de pago previamente tokenizado por el vault. Endpoint POST /api/payments disponible y operativo. |
| **Pasos y condiciones de ejecución** | Construir	el	payload:	amount=10000,	currency='COP', paymentToken='tok\_visa\_approved', Idempotency-Key única. Incluir la secretKey activa en el header Authorization: Bearer \<secretKey\>. Enviar POST /api/payments. Verificar el código de respuesta HTTP. Confirmar que la respuesta contiene: paymentId único, estado CREATED y correlationID. |
| **Resultado esperado** | HTTP 201 Created. El sistema autoriza la solicitud y retorna un paymentId único, estado CREATED y correlationID. La transacción queda disponible para consulta por el equipo del comercio. |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 201 Created con status CREATED. (": IntegrationApiTest.cp\_s2\_001\_transactionWithValidCredentials") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | Camino feliz |

| CP-S2-002 ·  Solicitud rechazada por credenciales sin permiso para la operación |  |
| :---- | :---- |
| **ID** | CP-S2-002 |
| **HU donde se aplica** | HU006 — Validación de credenciales por solicitud de pago |
| **Descripción de la prueba** | Verificar que el sistema rechaza una solicitud de pago cuando las credenciales presentadas tienen únicamente el scope reports:read, sin autorización para ejecutar transacciones. |
| **Supuestos	y precondiciones** | Comercio activo con credenciales que tienen únicamente scope reports:read. Endpoint POST /api/payments disponible. Sistema de autorización por scope activo. |
| **Pasos y condiciones de ejecución** | Obtener las credenciales del comercio con scope limitado a reports:read. Construir un payload de pago válido (amount=10000, currency='COP', paymentToken válido). Incluir la secretKey con scope reports:read en el header Authorization. Enviar POST /api/payments. Verificar el código de respuesta HTTP y el mensaje de error retornado. Confirmar que no se generó ninguna transacción. |
| **Resultado esperado** | HTTP 403 Forbidden. El sistema rechaza la solicitud e informa que las credenciales no tienen el permiso requerido para la operación. No se registra ninguna transacción. El correlationID está presente en la respuesta de error. |
| **Estado del caso de prueba** | NO EJECUTABLE |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Error |

| CP-S2-003 · Solicitud rechazada al presentar credenciales de un comercio diferente |  |
| :---- | :---- |
| **ID** | CP-S2-003 |
| **HU donde se aplica** | HU006 — Validación de credenciales por solicitud de pago |
| **Descripción de la prueba** | Verificar que el sistema detecta y rechaza el intento de operar con credenciales que pertenecen a otro comercio registrado en la plataforma, garantizando el aislamiento entre entornos. |
| **Supuestos	y precondiciones** | Existen al menos dos comercios activos (Comercio A y Comercio B) con credenciales generadas. Se utiliza la secretKey del Comercio B intentando operar sobre el entorno del Comercio A. Endpoint POST /api/payments disponible. |
| **Pasos y condiciones de ejecución** | Obtener la secretKey válida y activa del Comercio B. Construir solicitud POST /api/payments asociando el merchantId del Comercio A. Incluir la secretKey del Comercio B en el header Authorization. Enviar la solicitud al endpoint. Verificar el código de respuesta HTTP y el mensaje de error. Confirmar que no se generó ninguna transacción para el Comercio A ni para el Comercio B. |
| **Resultado esperado** | El sistema rechaza la solicitud e informa que las credenciales no son válidas para el entorno utilizado. No se genera ninguna transacción para ninguno de los comercios involucrados. En términos técnicos, esto equivale a HTTP 401 (Unauthorized) |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 401 Unauthorized. errorCode: CREDENTIAL\_MISMATCH. (": IntegrationApiTest.cp\_s2\_003\_transactionWithWrongMerchantCredentials") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | Error — Seguridad |

| HU007 — Consulta del perfil del comercio |  |
| :---: | ----- |

| CP-S2-004  ·  Consulta exitosa del perfil del propio comercio |  |
| ----- | :---- |
| **ID** | CP-S2-004 |
| **HU donde se aplica** | HU007 — Consulta del perfil del comercio |
| **Descripción de la prueba** | Verificar que un administrador autenticado puede consultar la información completa de su propio comercio, incluyendo datos bancarios parcialmente enmascarados. |
| **Supuestos	y precondiciones** | Comercio en estado ACCEPTED con datos de perfil y bancarios registrados. Administrador con sesión activa (token no vencido). Endpoint GET /api/merchants/{merchantId}/profile disponible. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del comercio y obtener token de sesión. Enviar GET /api/merchants/{merchantId}/profile con el token en el header Authorization. Verificar el código de respuesta HTTP. Validar que la respuesta incluye: nombre, datos de contacto, estado actual (ACCEPTED) y datos bancarios enmascarados. Confirmar que el número de cuenta bancaria aparece enmascarado (ej.: \*\*\*\*5678). Confirmar que el merchantId en la respuesta coincide con el del comercio autenticado. |
| **Resultado esperado** | HTTP 200 OK. La respuesta incluye todos los campos del perfil del comercio. Los datos bancarios se presentan con enmascaramiento parcial (últimos 4 dígitos visibles). El estado del comercio es ACCEPTED. El campo merchantID no es editable. |
| **Estado del caso de prueba** | No ejecutado |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Camino feliz |

| CP-S2-005  ·  Acceso denegado al intentar consultar el perfil de otro comercio |  |
| :---- | :---- |
| **ID** | CP-S2-005 |
| **HU donde se aplica** | HU007 — Consulta del perfil del comercio |
| **Descripción de la prueba** | Verificar que el sistema impide a un administrador acceder a la información de perfil de un comercio distinto al suyo, garantizando el aislamiento total de datos entre comercios. |
| **Supuestos	y precondiciones** | Existen al menos dos comercios registrados (Comercio A y Comercio B). Administrador autenticado como administrador del Comercio A. Se conoce el merchantId del Comercio B. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del Comercio A y obtener token de sesión. Enviar GET /api/merchants/{merchantId\_B}/profile usando el token del Comercio A. Verificar el código de respuesta HTTP. Confirmar que la respuesta no contiene ningún dato del Comercio B. Verificar que el mensaje de error no revela la existencia del Comercio B. |
| **Resultado esperado** | HTTP 403 Forbidden. El sistema niega el acceso e informa que el administrador no tiene permisos para visualizar esa información. No se retorna ningún dato del Comercio B. El error no revela información sensible sobre la existencia de ese comercio. |
| **Estado del caso de prueba** | NO EJECUTABLE |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Error — Seguridad |

| HU008 — Actualización de información bancaria del comercio |  |
| :---: | ----- |

| CP-S2-006  ·  Actualización exitosa de datos bancarios del comercio |  |
| :---- | :---- |
| **ID** | CP-S2-006 |
| **HU donde se aplica** | HU008 — Actualización de información bancaria del comercio |
| **Descripción de la prueba** | Verificar que un administrador del comercio puede actualizar los datos bancarios, que quedan en estado PENDING\_VERIFICATION y que el cambio queda trazado en la bitácora de auditoría. |
| **Supuestos	y precondiciones** | Administrador	autenticado	con	rol	MERCHANT\_ADMIN	o MERCHANT\_OWNER. Comercio en estado ACCEPTED. Endpoint PATCH /api/merchants/{merchantId}/profile disponible. Solo el propietario del comercio o un administrador de plataforma puede modificar el perfil. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del comercio y obtener token de sesión. Construir payload con nuevos datos bancarios: número de cuenta, banco y tipo de cuenta. Enviar PATCH /api/merchants/{merchantId}/profile con el payload. Verificar el código de respuesta HTTP. Validar que en la respuesta el estado de los datos bancarios es PENDING\_VERIFICATION.  ***Verificaciones técnicas complementarias:*** · Consultar la bitácora y confirmar que registra: campos modificados, actorID y timestamp UTC. |
| **Resultado esperado** | HTTP 200 OK. El sistema confirma que la actualización fue procesada y los datos bancarios quedan en estado PENDING\_VERIFICATION hasta ser validados por el equipo de la plataforma. El cambio queda trazado en la bitácora de auditoría. |
| **Estado del caso de prueba** | NO EJECUTABLE |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Camino feliz |

| CP-S2-007 ·  Intento de modificación de campos inmutables del comercio rechazado |  |
| :---- | :---- |
| **ID** | CP-S2-007 |
| **HU donde se aplica** | HU008 — Actualización de información bancaria |
| **Descripción de la prueba** | Verificar que el sistema rechaza cualquier intento de modificar los campos taxId o businessType, que son de registro único e inmutables post-registro, y que los valores originales permanecen inalterados. |
| **Supuestos	y precondiciones** | Comercio registrado con taxId y businessType definidos. Administrador con sesión activa. Endpoint PATCH /api/merchants/{merchantId}/profile disponible. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del comercio. Construir payload incluyendo el campo taxId con un valor diferente al original. Enviar PATCH /api/merchants/{merchantId}/profile. Verificar el código de respuesta HTTP y el mensaje de error. Repetir los pasos 2 a 4 usando el campo businessType. Consultar el perfil del comercio y confirmar que taxId y businessType son idénticos a los originales. |
| **Resultado esperado** | HTTP 422 Unprocessable Entity en ambos intentos. El sistema informa que los campos taxId y businessType son inmutables y no pueden modificarse; se sugiere contactar al equipo de soporte. Los valores originales permanecen sin cambios. |
| **Estado del caso de prueba** | NO EJECUTABLE |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Error |

| HU009 — Revocación inmediata de credenciales comprometidas |  |
| :---: | ----- |

| CP-S2-010  ·  Revocación exitosa de una credencial activa con efecto inmediato |  |
| :---- | :---- |
| **ID** | CP-S2-010 |
| **HU donde se aplica** | HU009 — Revocación inmediata de credenciales comprometidas |
| **Descripción de la prueba** | Verificar que un administrador puede revocar una credencial activa de forma inmediata, que el bloqueo de acceso se hace efectivo en menos de 5 segundos y que el evento queda registrado en la bitácora. |
| **Supuestos	y precondiciones** | Comercio con al menos una credencial en estado ACTIVE. Administrador	autenticado	con	rol	MERCHANT\_OWNER	o MERCHANT\_ADMIN. Endpoint DELETE /api/merchants/{merchantId}/credentials/{credentialId} disponible. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del comercio. Listar	las	credenciales	activas	vía	GET /api/merchants/{merchantId}/credentials. Seleccionar el credentialId a revocar. Enviar DELETE /api/merchants/{merchantId}/credentials/{credentialId}. Verificar el código de respuesta HTTP. Esperar hasta 5 segundos y confirmar que el estado de la credencial es REVOKED.  ***Verificaciones técnicas complementarias:*** · Verificar en la bitácora: credentialId, actorID y timestamp UTC del evento de revocación. |
| **Resultado esperado** | HTTP 200 OK. La credencial queda inhabilitada de forma inmediata (en menos de 5 segundos) y el sistema confirma la revocación. El estado pasa a REVOKED y la credencial no puede reactivarse. |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 200 OK. publicId coincide. active=false. (": IntegrationApiTest.cp\_s2\_010\_revokeCredential") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | Camino feliz |

| CP-S2-011 · Credencial revocada rechaza nuevas solicitudes de pago |  |
| :---- | :---- |
| **ID** | CP-S2-011 |
| **HU donde se aplica** | HU009 — Revocación inmediata de credenciales comprometidas |
| **Descripción de la prueba** | Verificar que una credencial en estado REVOKED no puede autenticar ninguna nueva solicitud de pago, garantizando el bloqueo de acceso inmediato. |
| **Supuestos	y precondiciones** | Una credencial del comercio ha sido revocada previamente (estado REVOKED). Se conoce la secretKey de la credencial revocada. Endpoint POST /api/payments disponible. |
| **Pasos y condiciones de ejecución** | Tomar nota de la secretKey de la credencial en estado REVOKED. Construir una solicitud POST /api/payments con payload válido. Incluir  la  secretKey  revocada  en  el  header  Authorization:  Bearer \<secretKey\_revocada\>. Enviar la solicitud al endpoint. Verificar el código de respuesta HTTP y el mensaje de error retornado. Confirmar que no se registró ninguna transacción. |
| **Resultado esperado** | HTTP 401 Unauthorized. El sistema rechaza la solicitud e informa que las credenciales no son válidas. No se registra ninguna transacción. El bloqueo opera de forma inmediata, sin importar el tiempo transcurrido desde la revocación. |
| **Estado del caso de prueba** | **Ejecutado** |
| **Resultado obtenido** | **PASS** — HTTP 401 Unauthorized. errorCode: INVALID\_CREDENTIALS. (": IntegrationApiTest.cp\_s2\_011\_transactionWithRevokedCredential") |
| **Errores asociados** | N/A |
| **Tipo de prueba** | Error |

| HU010 — Rotación de credenciales sin interrupción del servicio |  |
| :---: | ----- |

| CP-S2-012 · Rotacion exitosa de credenciales con periodo de gracia activo de 24 |  |
| ----- | :---- |
| **ID** | CP-S2-012 |
| **HU donde se aplica** | HU010 — Rotación de credenciales sin interrupción del servicio |
| **Descripción de la prueba** | Verificar que al solicitar la rotación de credenciales el sistema genera un nuevo conjunto, las credenciales antiguas permanecen válidas durante 24 horas en estado ROTATING y ambos conjuntos aceptan solicitudes durante el período de gracia. |
| **Supuestos	y precondiciones** | Comercio con credenciales activas en estado ACTIVE. Administrador con sesión activa. Endpoint POST /api/merchants/{merchantId}/credentials/{credentialId}/rotate disponible. Proceso automático de expiración de credenciales activo en el entorno. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del comercio. Enviar POST /api/merchants/{merchantId}/credentials/{credentialId}/rotate. Verificar que la respuesta contiene las nuevas credenciales (nueva publicKey y nueva secretKey). Confirmar que las credenciales anteriores están ahora en estado ROTATING. Usar la secretKey antigua (ROTATING) para enviar POST /api/payments y confirmar que es aceptada (HTTP 201). Usar la nueva secretKey para enviar POST /api/payments y confirmar que también es aceptada (HTTP 201). |
| **Resultado esperado** | HTTP 201 con las nuevas credenciales. Las credenciales anteriores quedan en estado ROTATING y son válidas durante 24 horas. Las nuevas credenciales son operativas desde el momento de la rotación. El período de gracia permite migración sin downtime. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Camino feliz |

| CP-S2-013 · Credencial en estado ROTATING revocada automáticamente al vencer el período de gracia |  |
| :---- | :---- |
| **ID** | CP-S2-013 |
| **HU donde se aplica** | HU010 — Rotación de credenciales sin interrupción del servicio |
| **Descripción de la prueba** | Verificar que el proceso automático revoca las credenciales en estado ROTATING una vez vence el período de gracia de 24 horas, sin requerir intervención manual. |
| **Supuestos	y precondiciones** | Credenciales antiguas en estado ROTATING tras una rotación previa. Proceso automático de expiración de credenciales activo en el entorno. Entorno de pruebas con capacidad de simular el avance del período de gracia. |
| **Pasos y condiciones de ejecución** | Confirmar que las credenciales antiguas están en estado ROTATING. Simular el vencimiento del período de gracia de 24 horas en el entorno de pruebas. Aguardar la ejecución del proceso automático de revocación. Consultar el estado de la credencial antigua y confirmar que es REVOKED. Intentar  usar  la  secretKey  antigua  (ahora  REVOKED)  en  POST /api/payments. Verificar el código de respuesta HTTP y el mensaje de error. |
| **Resultado esperado** | Las credenciales pasan de estado ROTATING a REVOKED de forma automática al cumplirse el período de gracia, sin requerir intervención manual. Cualquier intento de uso posterior de esas credenciales es rechazado con HTTP 401 Unauthorized. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Error |

| HU011 — Procesamiento del resultado del pago |  |
| :---: | ----- |

| CP-S2-014 · Pago aprobado correctamente por el procesador financiero externo |  |
| :---- | :---- |
| **ID** | CP-S2-014 |
| **HU donde se aplica** | HU011 — Procesamiento del resultado del pago |
| **Descripción de la prueba** | Verificar que cuando el procesador financiero externo confirma la aprobación de una transacción, el sistema actualiza el estado a APPROVED y envía la notificación al canal de webhooks del comercio. |
| **Supuestos	y precondiciones** | Transacción en estado PROCESSING registrada en la plataforma. Servicio de simulación del procesador (mock PSP) configurado para responder con aprobación. Comercio con canal de notificación (webhook) en estado ACTIVE. |
| **Pasos y condiciones de ejecución** | Crear un pago con token tok\_visa\_approved y confirmar que transiciona a PROCESSING. Simular la respuesta de aprobación del PSP externo para el paymentId. Consultar GET /api/payments/{paymentId} y verificar que el estado es APPROVED. Verificar que el webhook del comercio recibió la notificación con: tipo de evento, paymentId, estado, monto, moneda y timestamp.  ***Verificaciones técnicas complementarias:*** Confirmar que la firma HMAC-SHA256 de la notificación es válida. Verificar en la bitácora: estado anterior (PROCESSING), estado nuevo (APPROVED), actorID y timestamp UTC. |
| **Resultado esperado** | El pago queda en estado APPROVED. El comercio recibe la notificación de aprobación a través de su canal de webhooks con el contenido correcto del evento. La transición de estado es definitiva e irreversible. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Camino feliz |

| CP-S2-015 · Pago rechazado por fondos insuficientes con motivo en lenguaje de negocio |  |
| :---- | :---- |
| **ID** | CP-S2-015 |
| **HU donde se aplica** | HU011 — Procesamiento del resultado del pago |
| **Descripción de la prueba** | Verificar que cuando el PSP externo rechaza una transacción por fondos insuficientes, el sistema actualiza el estado a REJECTED con el motivo expresado en lenguaje de negocio y notifica al comercio correctamente. |
| **Supuestos	y precondiciones** | Transacción en estado PROCESSING. Mock PSP configurado para responder con código de rechazo por fondos insuficientes. Comercio con canal de notificación activo. Mapeo de códigos del procesador a mensajes de negocio configurado. |
| **Pasos y condiciones de ejecución** | Crear un pago usando el token de simulación de rechazo (ej.: tok\_insufficient\_funds). Confirmar que el pago transiciona a PROCESSING. Simular la respuesta de rechazo del PSP por fondos insuficientes. Consultar GET /api/payments/{paymentId} y verificar que el estado es REJECTED. Confirmar que el campo motivoRechazo contiene: 'El medio de pago del cliente no tiene saldo suficiente'. Verificar que el motivo es un valor del catálogo controlado, no un código técnico del procesador. Verificar que el webhook del comercio recibió la notificación de rechazo con el motivo correspondiente. |
| **Resultado esperado** | El pago queda en estado REJECTED. El motivo de rechazo es comunicado al comercio en lenguaje de negocio comprensible, sin exponer códigos técnicos internos del procesador. El comercio recibe la notificación de rechazo a través de su canal de webhooks. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Error |

**HU012 — Configuración de canales de notificación automática (webhooks)**

| CP-S2-016  ·  Registro exitoso de un canal de notificación webhook con verificación |  |
| :---- | :---- |
| **ID** | CP-S2-016 |
| **HU donde se aplica** | HU012 — Configuración de canales de notificación automática |
| **Descripción de la prueba** | Verificar que un administrador puede registrar una URL de webhook, que el sistema la verifica con un mensaje de confirmación y que el canal queda activo únicamente tras la respuesta correcta del endpoint receptor. |
| **Supuestos	y precondiciones** | Administrador con sesión activa. URL de endpoint activa y accesible desde la plataforma. Comercio con menos de 5 canales de notificación activos. Endpoint POST /api/merchants/{merchantId}/webhooks disponible. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del comercio. Enviar POST /api/merchants/{merchantId}/webhooks con URL del endpoint y eventos suscritos (pago\_aprobado, pago\_rechazado). Verificar HTTP 201 y que la respuesta contiene webhookId único con estado PENDING\_VERIFICATION. Confirmar que la plataforma envía solicitud de verificación a la URL registrada. Responder desde el endpoint receptor con el código de verificación esperado. Verificar que el canal queda en estado ACTIVE.  ***Verificaciones técnicas complementarias:*** · Confirmar que el payload de notificación incluye firma HMAC-SHA256. |
| **Resultado esperado** | HTTP 201 Created con webhookId único. Tras la respuesta correcta al mensaje de verificación, el canal pasa a estado ACTIVE y comienza a recibir notificaciones para los eventos suscritos. Se acepta un máximo de 5 canales activos por comercio. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Camino feliz |

| CP-S2-017 · Reintentos automáticos y marcado como INACTIVE ante canal de notificación no disponible |  |
| :---- | :---- |
| **ID** | CP-S2-017 |
| **HU donde se aplica** | HU012 — Configuración de canales de notificación automática |
| **Descripción de la prueba** | Verificar que cuando el webhook del comercio no responde, el sistema aplica la política de reintentos con backoff exponencial (1 min, 5 min, 30 min, 2 horas) y marca el canal como INACTIVE tras 4 intentos fallidos, notificando al administrador. |
| **Supuestos	y precondiciones** | Comercio con canal de notificación en estado ACTIVE. Endpoint del webhook configurado para no responder (timeout o error 500). Tiempo máximo de espera por respuesta del canal: 10 segundos. |
| **Pasos y condiciones de ejecución** | Simular un cambio de estado de pago que genera notificación al webhook del comercio. Confirmar que el primer intento de entrega falla (timeout a los 10 segundos). Confirmar que los reintentos se ejecutan en los intervalos: 1 min, 5 min, 30 min y 2 horas. Verificar que todos los intentos fallan por no respuesta del canal. Confirmar que el canal queda en estado INACTIVE tras el cuarto intento fallido. Confirmar que el administrador del comercio recibe una alerta sobre el problema de entrega.  ***Verificaciones técnicas complementarias:*** · Verificar en los logs del sistema que los tiempos entre reintentos respetan el backoff exponencial definido. |
| **Resultado esperado** | El sistema reintenta la entrega en 4 ocasiones respetando los intervalos de backoff definidos (1 min → 5 min → 30 min → 2 horas). Tras el cuarto intento fallido, el canal queda en estado INACTIVE y el administrador del comercio recibe una alerta sobre el problema de entrega. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Error |

| HU013 — Registro de eventos de auditoría |  |
| :---: | ----- |

| CP-S2-018 · Generación automática de registro de auditoría inmutable ante cambio de estado |  |
| :---- | :---- |
| **ID** | CP-S2-018 |
| **HU donde se aplica** | HU013 — Registro de eventos de auditoría |
| **Descripción de la prueba** | Verificar que cada cambio de estado de una transacción genera automáticamente un registro de auditoría completo con todos los campos requeridos, y que dicho registro no puede ser modificado ni eliminado. |
| **Supuestos	y precondiciones** | Transacción en estado CREATED en la plataforma. Mecanismo de auditoría inmutable activo en el sistema. Endpoint GET /api/payments/{paymentId}/audit disponible. |
| **Pasos y condiciones de ejecución** | Verificar el estado inicial de la transacción (CREATED). Ejecutar el procesamiento que transiciona el pago a PROCESSING. Consultar GET /api/payments/{paymentId}/audit. Verificar que el registro contiene: paymentId, tipo de evento, estado anterior (CREATED), estado nuevo (PROCESSING), actorID y timestamp UTC.  ***Verificaciones técnicas complementarias:*** Intentar ejecutar UPDATE sobre el registro de auditoría en la base de datos. Intentar ejecutar DELETE sobre el mismo registro. Confirmar que ambas operaciones son rechazadas por el sistema. |
| **Resultado esperado** | El sistema genera automáticamente un registro de auditoría completo e inmutable ante cada cambio de estado de la transacción. El registro contiene todos los campos de trazabilidad requeridos y no puede ser modificado ni eliminado bajo ninguna circunstancia. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Camino feliz |

| CP-S2-019 · Detección automática de ruptura de integridad en la cadena de auditoría |  |
| :---- | :---- |
| **ID** | CP-S2-019 |
| **HU donde se aplica** | HU013 — Registro de eventos de auditoría |
| **Descripción de la prueba** | Verificar que el mecanismo de hash encadenado SHA-256 detecta correctamente cualquier modificación introducida en un registro previo de la bitácora, garantizando la inmutabilidad verificable de la cadena de auditoría. |
| **Supuestos	y precondiciones** | Al menos 3 registros de auditoría encadenados para una misma transacción. Proceso de verificación de integridad disponible para el equipo auditor. Acceso directo a la base de datos disponible en el entorno de pruebas para simular la modificación. |
| **Pasos y condiciones de ejecución** | Consultar la secuencia de registros de auditoría de una transacción con múltiples eventos. Ejecutar el proceso de verificación de integridad sobre la cadena de hashes. Confirmar que el resultado es ÍNTEGRO cuando los registros no han sido modificados. Modificar manualmente el contenido de un registro intermedio en la base de datos (entorno de pruebas). Ejecutar nuevamente el proceso de verificación de integridad. Confirmar que el sistema detecta la ruptura e identifica el registro comprometido. |
| **Resultado esperado** | La verificación sobre registros no modificados retorna estado ÍNTEGRO. Tras la modificación de un registro, el proceso detecta automáticamente la ruptura en la cadena SHA-256 e identifica el registro comprometido sin intervención manual. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Seguridad |

| HU015 — Consulta del listado de pagos del comercio |  |
| :---: | ----- |

| CP-S2-020 · Consulta exitosa de transacciones aplicando filtros y paginación |  |
| :---- | :---- |
| **ID** | CP-S2-020 |
| **HU donde se aplica** | HU014 — Consulta del listado de pagos del comercio |
| **Descripción de la prueba** | Verificar que un administrador del comercio puede consultar su listado de transacciones aplicando filtros por estado (APPROVED) y rango de fechas, recibiendo resultados paginados con un máximo de 100 registros por página. |
| **Supuestos	y precondiciones** | Comercio con transacciones en distintos estados registradas durante el último mes. Administrador con sesión activa. Endpoint GET /api/payments disponible con soporte de filtros y paginación. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del comercio y obtener token de sesión. Enviar	GET /api/payments?status=APPROVED\&dateFrom=2025-04-01\&dateTo=2025-04-30 \&page=1\&pageSize=10. Verificar el código de respuesta HTTP. Confirmar que todos los registros retornados tienen estado APPROVED. Confirmar que todos los registros están dentro del rango de fechas especificado. Verificar la estructura de paginación en la respuesta: totalItems, totalPages, currentPage y pageSize. Solicitar la página 2 y verificar la continuidad de resultados sin duplicados. |
| **Resultado esperado** | HTTP 200 OK. La respuesta contiene únicamente transacciones APPROVED dentro del rango de fechas. La estructura de paginación es correcta. El filtro de |
|  | comercio es aplicado automáticamente según la sesión activa. El tamaño de página no supera 100 registros. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Camino feliz |

| CP-S2-021 · Aislamiento garantizado: el comercio no accede a transacciones de otro comercio |  |
| :---- | :---- |
| **ID** | CP-S2-021 |
| **HU donde se aplica** | HU014 — Consulta del listado de pagos del comercio |
| **Descripción de la prueba** | Verificar que el filtro de aislamiento por comercio es aplicado automáticamente por el backend y que un comercio autenticado nunca puede visualizar transacciones de otro comercio, incluso si lo intenta explícitamente como parámetro de consulta. |
| **Supuestos	y precondiciones** | Transacciones registradas para el Comercio A y el Comercio B. Administrador autenticado como administrador del Comercio A. Se conoce el merchantId del Comercio B. Endpoint GET /api/payments disponible. |
| **Pasos y condiciones de ejecución** | Autenticarse como administrador del Comercio A y obtener token de sesión. Enviar	GET	/api/payments?merchantId={merchantId\_B}	incluyendo explícitamente el merchantId del Comercio B. Verificar el código de respuesta HTTP. Confirmar que los resultados pertenecen únicamente al Comercio A. Confirmar que no se expone ninguna transacción del Comercio B. Verificar que el parámetro merchantId enviado por el cliente es ignorado y el filtro se extrae de la sesión activa. |
| **Resultado esperado** | HTTP 200 OK. La respuesta contiene exclusivamente las transacciones del Comercio A. El parámetro merchantId enviado por el cliente es ignorado; el filtro se aplica desde la sesión activa en el backend. Ningún dato del Comercio B es expuesto en la respuesta. |
| **Estado del caso de prueba** | Esperando implementación de HU |
| **Resultado obtenido** |  |
| **Errores asociados** |  |
| **Tipo de prueba** | Error — Seguridad |

