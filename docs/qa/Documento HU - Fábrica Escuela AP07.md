# **Plataforma de APIs de Pagos Embebidos tipo Stripe — Caso 8**

**Backlog de Historias de Usuario**

Equipo Avanzado Presencial \#07 — CodeF@ctory — Universidad de Antioquia

Versión 3.0 · Sprints 1 – 3 · 24 HU · Backlog de Historias de Usuario

## **Resúmenes**

### **Resumen ejecutivo**

Este documento contiene el backlog completo de historias de usuario para el Caso 8: Plataforma de APIs de pagos embebidos tipo Stripe. Las historias siguen el estándar de “Behavior Driven Development” (BDD), con criterios de aceptación escritos en lenguaje Gherkin (Feature / Escenario / Given / When / Then), describiendo el QUÉ debe hacer el sistema desde la perspectiva del negocio, sin incluir detalles de implementación en los escenarios.

Cada historia incluye: nombre, descripción estándar (Como / Quiero / Para), bloque “**Feature**” que enmarca la funcionalidad, escenarios de caso feliz y excepciones, uso de “**Scenario Outline**” con ejemplos donde aplica, notas técnicas y de seguridad separadas de los criterios de aceptación, y dependencias explícitas entre historias.

### **Resumen del backlog por Sprint**

| Sprint | Objetivo del sprint | Épicas cubiertas | HUs | Story Points |
| :---- | :---- | :---- | :---- | :---- |
| Sprint 1 | Un comercio procesa su primera transacción exitosa desde cero. | Épica 1, Épica 2 y Épica 3 | 5 HU | 42 SP |
| Sprint 2 | El comercio puede realizar seguimiento a las transacciones e implementa medidas para mitigar el fraude. | Épica 1, Épica 2, Épica 3 y Épica 4 | 9 HU | 52 SP |
| Sprint 3 | El comercio recibe reportes de sus transacciones y puede reembolsar a sus clientes. | Épica 1, Épica 3, Épica 4, Épica 5 y Épica 6 | 10 HU | 55 SP |

## **Estructura de Épicas**

### **Épica 1 (EP01) — Gestión de comercios y onboarding**

Abarca todo el proceso de incorporación de nuevos comercios a la plataforma de pagos: registro inicial, verificación de la identidad del negocio, administración del perfil, configuración bancaria y gestión de los usuarios del equipo del comercio. Es la puerta de entrada al ecosistema de pagos y establece la identidad de cada comercio dentro del sistema.

### **Épica 2 (EP02) — Seguridad y acceso a APIs**

Garantiza que solo los comercios autorizados accedan a los servicios de la plataforma. Incluye la generación, administración, rotación y revocación de credenciales de acceso, la autenticación de administradores con un método alternativo (segundo factor), la separación entre entornos de pruebas y producción, y los controles de autorización por operación y por entorno.

### **Épica 3 (EP03) — Procesamiento central de pagos**

Es el núcleo funcional de la plataforma: comprende la creación de transacciones, la gestión completa de la máquina de estados del pago (desde su creación hasta la aprobación, rechazo o reembolso), el mecanismo que evita cobros dobles, y el sistema de notificaciones automáticas hacia los sistemas internos del comercio.

### **Épica 4 (EP04) — Trazabilidad y auditoría**

Asegura que cada evento de una transacción quede registrado de forma inmutable, ordenada y verificable. Esta bitácora es esencial para resolver disputas entre el comercio y sus clientes, para cumplir con requerimientos regulatorios y financieros, y para que el equipo de soporte pueda entender los cambios en una transacción de un usuario final.

### **Épica 5 (EP05) — Reporting y analítica**

Proporciona a los comercios visibilidad necesaria sobre su actividad transaccional: volumen de pagos por período, distribución de resultados por estado, tasa de aprobación y otras herramientas de análisis a nivel corporativo, así como la capacidad de exportar estos datos para contabilidad y análisis de tendencias.

### **Épica 6 (EP06) — Entorno de pruebas (SandBox)**

Permite a los desarrolladores de cada comercio y a los de la plataforma de pagos integrar nuevas funcionalidades y realizar pruebas antes de su salida a producción, de modo que los cambios sean validados en un entorno controlado y aislado del sistema productivo. Este entorno garantiza una integración segura, reproducible y sin impacto en datos reales.

## **Estructura de Features**

### **Feature 1.1 (FE101) — Registro y administración de comercios**

Comprende el registro de nuevos comercios en la plataforma, validación de su información y control de su estado operativo, permitiendo a los administradores aprobar, rechazar o suspender comercios según las políticas de la plataforma y requisitos regulatorios.

### **Feature 1.2 (FE102) — Gestión del perfil y configuración operativa del** 

### **comercio**

Permite a los comercios consultar y mantener actualizada la información principal de su organización dentro de la plataforma, incluyendo datos de contacto, configuración bancaria y estado operativo de la cuenta. Garantiza que los cambios sensibles sean controlados y trazables, preservando la integridad de la información del comercio y la continuidad de sus operaciones financieras.

### **Feature 1.3 (FE103) — Administración de usuarios y permisos del**

### **comercio**

Permite al/los propietario(s) del comercio incorporar miembros de su equipo, asignar responsabilidades según su función y controlar el acceso a las diferentes capacidades de la plataforma. Facilita la delegación segura de tareas operativas, financieras y técnicas, evitando el uso compartido de credenciales y manteniendo trazabilidad sobre las acciones realizadas por cada usuario.

### **Feature 2.1 (FE201) — Autenticación y generación de credenciales**

Permite generar y validar credenciales de acceso a la plataforma de pagos para los comercios registrados y activos en la plataforma, así como implementar mecanismos de autenticación segura para los administradores de dichos comercios.

### **Feature 2.2 (FE202) — Administración segura de credenciales de acceso**

Permite a los comercios gestionar el ciclo de vida de sus credenciales de acceso a la plataforma, incluyendo renovación, reemplazo y desactivación inmediata en caso de riesgo de seguridad. Garantiza la continuidad operativa durante los procesos de actualización de credenciales y protege el acceso a las operaciones del comercio frente a usos no autorizados.

### **Feature 3.1 (FE301) — Creación y consulta de pagos**

Permite a los usuarios de los comercios generar nuevos pagos o transacciones y consultar el historial de dichas transacciones. Proporciona las operaciones básicas para registrar pagos en el sistema y obtener información sobre su estado y detalles, incluyendo capacidades de filtrado para facilitar la gestión y monitoreo de transacciones.

### **Feature 3.2 (FE302) — Gestión de estados de un pago**

Gestiona los diferentes estados de un pago, controlando las transiciones entre estados de una misma transacción. Garantiza que cada transacción siga un flujo definido y que los cambios de estado se realicen de manera consistente, permitiendo el control del proceso transaccional y la toma de decisiones operativas.

### **Feature 3.3 (FE303) — Gestión de devoluciones y reembolsos de pagos**

Permite a los comercios devolver total o parcialmente el dinero de pagos previamente aprobados, asegurando que cada devolución quede correctamente registrada y asociada a la transacción original. Facilita la atención de cancelaciones, devoluciones comerciales y resolución de disputas con clientes, manteniendo control sobre los montos reembolsados y la trazabilidad del proceso.

### **Feature 3.4 (FE304) — Notificaciones automáticas del estado de los** 

### **pagos**

Permite a los comercios recibir alertas automáticas cuando una transacción cambia de estado dentro de la plataforma. Facilita que los sistemas del comercio reaccionen en tiempo real ante pagos aprobados, rechazados o reembolsados, mejorando la sincronización operativa y la experiencia de los usuarios finales. 

**Feature 4.1 (FE401) — Registro inmutable de eventos transaccionales**

Garantiza que cada acción relevante relacionada con un pago quede registrada de manera cronológica, íntegra y verificable dentro de la plataforma. Proporciona evidencia confiable para auditorías, análisis operativos y resolución de disputas, asegurando que los eventos históricos no puedan ser alterados ni eliminados. 

**Feature 4.2 (FE402) — Consulta del historial completo de eventos de un**   
**pago**

Permite a los comercios y equipos autorizados consultar la secuencia completa de eventos asociados a una transacción específica, incluyendo cambios de estado, rechazos, reembolsos y notificaciones emitidas. Facilita el análisis operativo y la resolución de incidentes al ofrecer visibilidad detallada sobre el ciclo de vida de cada pago. 

**Feature 5.1 (FE501) — Reportes de actividad y volumen transaccional**

Permite a los comercios analizar el comportamiento de sus operaciones de pago mediante reportes consolidados por período de tiempo. Proporciona visibilidad sobre el volumen de transacciones, montos procesados y resultados operativos, facilitando la toma de decisiones comerciales y el seguimiento del desempeño financiero de la compañía. 

**Feature 5.2 (FE502) — Visualización del desempeño de pagos por estado**

Permite a los comercios visualizar la distribución de sus transacciones según el resultado de los pagos, identificando tendencias de aprobación, rechazo y reembolso dentro de períodos específicos. Facilita el monitoreo de la salud operativa de la integración y la detección temprana de posibles problemas en el procesamiento de pagos. 

**Feature 6.1 (FE601) — Separación de entornos de pruebas y operación**   
**real**

Permite a los comercios integrar y validar sus procesos de pago en un entorno seguro de simulación antes de operar con transacciones reales. Mantiene completamente separados los datos, credenciales y operaciones de pruebas frente al entorno productivo, reduciendo riesgos y facilitando una adopción controlada de la plataforma. 

## **Backlog de Historias de Usuario**

###   **SPRINT 1**

**Habilitación del flujo mínimo viable de pagos**

**Objetivo:** Que un comercio pueda registrarse, obtener sus credenciales y procesar su primera transacción de pago de forma exitosa y segura.

El Sprint 1 abarca el núcleo de la plataforma. Sin los módulos que se desarrollan en este sprint, ninguna otra funcionalidad tiene sentido. Se prioriza la ruta crítica completa del negocio: un comercio nuevo debe poder registrarse (Épica 1), obtener sus credenciales de acceso a la plataforma de pagos (Épica 2\) y generar una transacción (Épica 3). Paralelamente, se establece la autenticación con segundo factor para los administradores de la plataforma de pagos y el mecanismo de validación de credenciales en cada solicitud. La máquina de estados del pago también se implementa en este sprint porque define el contrato que todos los módulos posteriores utilizarán. Al finalizar el Sprint 1, la plataforma debe permitir ejecutar el escenario completo de extremo a extremo: registro → credenciales → primer pago → estado inicial asignado.

**Valor entregado:** *Al término del Sprint 1, existe un sistema funcional que puede recibir y registrar pagos. Este es el MPV técnico sobre el que se construirán todos los sprints siguientes.*

#### **HU001 \- Registro de nuevo comercio**

| Épica: EP01 | Feature: FE101 | Sprint: Sprint 1 | Prioridad: Crítica |  |
| :---- | :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Representante del comercio** | **Módulo: Comercios / Onboarding** | **Estado: Completada** |  |

##### **Historia de Usuario**

**Como** representante de un comercio que desea aceptar pagos digitales, **quiero** registrar mi empresa en la plataforma de pagos, **para** habilitar la recepción de pagos de mis clientes.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature 1.1:** Registro y administración de comercios

**Escenario:** Registro exitoso de comercio en la plataforma de pagos

**Given** un representante de un comercio que tiene sus datos legales completos y vigentes (nombre del comercio, número de identificación fiscal, correo electrónico corporativo y tipo de negocio)

**When** completa el formulario de registro y lo envía a la plataforma de pagos

**Then** el sistema confirma el registro exitoso del comercio

**And** el sistema asigna un identificador único de comercio

**And** la cuenta del comercio queda pendiente de verificación hasta que sea aprobada

**And** recibe un correo electrónico de bienvenida con los pasos a seguir para activar la cuenta del comercio

**Escenario:** Falla en el registro del comercio por error interno de la plataforma de pagos

**Given** un representante de un comercio que intenta registrarlo en la plataforma de pagos

**When** ocurre un error interno al procesar la solicitud de registro

**Then** el sistema informa que no fue posible completar el registro

**And** el comercio no queda registrado en la plataforma

**Escenario:** Intento de registro de comercio con correo ya registrado en la plataforma  
**Given** un representante de un comercio que intenta registrarse con un correo ya asociado a otro comercio

**When** envía la solicitud de registro

**Then** el sistema informa que el correo ya está en uso

**And** no se crea un nuevo comercio en la plataforma

**Escenario Outline:** Intento de registro de comercio con campo obligatorio vacío

**Given** un representante de un comercio que intenta registrarse sin haber diligenciado el \<campo\> en el formulario de registro

**When** envía la solicitud de registro

**Then** el sistema indica que el \<campo\> es obligatorio

**And** el sistema no permite continuar hasta completarlo

**Examples:**

| campo |

| nombre del comercio |

| número de identificación fiscal|

| correo electrónico corporativo |

| tipo de negocio |

##### **Notas técnicas y de seguridad**

* El identificador único del comercio (merchantID) se genera en el backend como UUID v4. No es editable por el equipo o los usuarios del comercio.

* El estado inicial del comercio es “**PENDING\_VERIFICATION**”**,** y solo un administrador de la plataforma puede cambiar este estado a “**ACCEPTED**”.

* La contraseña del usuario administrador del comercio debe cumplir políticas: mínimo 12 caracteres, al menos 1 mayúscula, 1 número y 1 carácter especial.

* La respuesta debe incluir links HATEOAS: self, verify y credentials.

* Todo evento de creación queda registrado en la bitácora de auditoría con timestamp en UTC, IP de origen y correlationID.

* El endpoint de onboarding tiene rate limiting de 10 solicitudes por minuto por dirección IP para prevenir abusos.

#### **HU002 \- Generación de credenciales**

| Épica: EP02 | Feature: FE201 | Sprint: Sprint 1 | Prioridad: Crítica |  |
| :---- | :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Desarrollador del comercio** | **Módulo: Seguridad / Credenciales** | **Estado: Completada** |  |

#### **Historia de Usuario**

**Como** desarrollador de un comercio, **quiero** generar credenciales de acceso a la plataforma de pagos, **para** poder procesar pagos de mis clientes de forma segura.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature 2.1:** Autenticación y generación de credenciales  
**Escenario:** Generación exitosa de credenciales del comercio

**Given** un comercio activo y verificado en la plataforma de pagos

**When** el desarrollador solicita credenciales para integrar pagos en el comercio

**Then** el sistema genera un conjunto de credenciales válidas

**And** el comercio puede utilizarlas para procesar los pagos de sus clientes

**Escenario:** Comercio no verificado intenta generar credenciales

**Given** un comercio que no ha sido aprobado por la plataforma

**When** el desarrollador intenta generar credenciales de acceso

**Then** el sistema impide la generación de credenciales

**And** informa que el comercio debe estar activo para operar

**Escenario:** El comercio alcanza el límite máximo de credenciales activas

**Given** que se ha alcanzado la cantidad máxima de credenciales activas permitidas del comercio

**When** el desarrollador del comercio intenta generar nuevas credenciales

**Then** el sistema informa que se ha alcanzado el límite permitido de credenciales activas

**And** sugiere revocar alguna credencial existente antes de crear una nueva

##### **Notas técnicas y de seguridad**

* El secretKey se muestra en texto plano únicamente al momento de la creación. A partir de ese punto solo se almacena el hash bcrypt (cost factor 12).

* El identificador público sigue el formato: pk\_{entorno}\_{uuid}. La clave secreta: sk\_{entorno}\_{uuid}.

* Habrá un máximo de 5 credenciales activas por comercio, por entorno, por defecto. Este límite es configurable por un administrador de la plataforma de pagos.

* Cada credencial tiene un alcance de permisos configurable: payments: write, payments: read, reports: read.

* Las credenciales tienen vigencia de 365 días si no se rotan manualmente.

**Dependencias**

* HU001 — El comercio debe estar en estado **“ACCEPTED”** para generar credenciales

#### **HU003 \- Creación de una transacción**

| Épica: EP03 | Feature: FE301 | Sprint: Sprint 1 | Prioridad: Crítica |  |
| :---- | :---- | :---- | :---- | :---- |
| **Story Points: 13 SP** | **Actor / Rol: Sistema del comercio** | **Módulo: Pagos / Transacciones** | **Estado: Completada** |  |

##### **Historia de Usuario**

**Como** representante de un comercio, **quiero** que el sistema del comercio registre una solicitud de pago de un cliente, **para** poder cobrarle por mis servicios, haciendo seguimiento al resultado de la transacción.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature 3.1:** Creación y consulta de pagos

**Escenario:** Inicio exitoso de un pago con datos completos y válidos

**Given** un comercio con credenciales válidas

**When** solicita el registro de un pago con información válida del cliente

**Then** el sistema registra la transacción de pago

**And** el equipo del comercio puede consultar el estado del pago

**Escenario:** Prevención de cobros duplicados para una misma operación 

**Given** un comercio que ya registró un pago para una operación específica

**When** intenta registrar nuevamente el mismo pago

**Then** el sistema evita duplicar el cobro

**And** retorna la información del pago previamente registrado

**Escenario:** Intento de pago con monto inválido

**Given** un comercio que intenta registrar un pago con un monto inválido

**When** envía la solicitud de pago

**Then** el sistema rechaza la operación

**And** el sistema informa que el monto no es válido para procesar el pago

**Escenario:** Intento de pago con credenciales inválidas

**Given** un comercio cuyas credenciales de acceso han sido revocadas, han vencido o son incorrectas

**When** envía la solicitud de pago

**Then** el sistema rechaza la solicitud

**And** informa que las credenciales no son válidas

##### **Notas técnicas y de seguridad**

* Un monto inválido incluye valores nulos, negativos o no numéricos.

* El monto se envía como entero en la menor unidad monetaria definida (pesos o dólares). Ejemplo: COP 1 o USD 0.01; si al realizar una conversión el monto no da un múltiplo de estos valores, se aproxima a la siguiente unidad múltiplo de la respectiva moneda.

* Implementar motor de idempotencia: almacenar hash de Idempotency-Key \+ merchantID con TTL de 24 horas en REDIS.

* El sistema incorpora pagos en pesos colombianos (COP) y dólares estadounidenses (USD).

* La moneda debe ser un código ISO 4217 válido y soportado por la plataforma.

* El token de método de pago debe ser previamente tokenizado por el vault. No se aceptan datos crudos de tarjeta.

* Rate limiting: 100 req/s por credencial en producción, 10 req/s en sandbox.

* La respuesta debe ser inferior a 300 ms en el percentil 99 bajo carga normal.

* Incluir correlationID en todos los logs y en las respuestas de error para trazabilidad extremo a extremo.

**Dependencias**

* HU001 — Se requiere un comercio registrado y activo en la plataforma de pagos

* HU002 — Credenciales válidas requeridas

#### **HU004 \- Asignación del estado inicial de un pago**

| Épica: EP03 | Feature: FE302 | Sprint: Sprint 1 | Prioridad: Alta |  |
| :---- | :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio** | **Módulo: Pagos / Máquina de estados** | **Estado: Completada** |  |

##### **Historia de Usuario**

**Como** representante de un comercio, **quiero** que cada pago tenga un estado inicial definido automáticamente, **para** poder monitorear su avance dentro del proceso de pago.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature 3.2:** Gestión de estados de un pago

**Escenario:** Asignación exitosa del estado inicial de un pago

**Given** un pago registrado correctamente

**When** el sistema inicia su procesamiento

**Then** el pago queda en un estado inicial que permite su seguimiento

**And** el comercio puede conocer que el pago está en proceso

**Escenario:** Se detecta una transición de estado no válida

**Given** un pago que ya se encuentra finalizado

**When** se intenta modificar su estado

**Then** el sistema impide el cambio

**And** mantiene la consistencia del proceso de pago

##### **Notas técnicas y de seguridad**

* La máquina de estados permite las siguientes transiciones: **CREATED** → **PROCESSING** →  
  **APPROVED** | **REJECTED** | **FAILED**.

* Los estados finales, tanto exitosos como no exitosos son: **APPROVED**, **REJECTED**, **FAILED**, **PARTIALLY\_REFUNDED**, **REFUNDED**.

* Solo los estados finales exitosos **APPROVED** y **PARTIALLY\_REFUNDED**, son susceptibles de cambios de estado por medio de reembolsos.

* Las transiciones inválidas lanzan excepción de dominio y no persisten.

* Cada cambio de estado registra: estado anterior, estado nuevo, timestamp UTC, actorID y motivo.

* Implementar patrón Outbox para garantizar la atomicidad entre el cambio de estado en bases de datos y el envío del evento de notificación.

**Dependencias**

* HU003 — La transacción debe existir en estado “**CREATED**”

#### **HU005 \- Autenticación con doble factor**

| Épica: EP02 | Feature: FE201 | Sprint: Sprint 1 | Prioridad: Crítica |  |
| :---- | :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Administrador de plataforma** | **Módulo: Seguridad / Autenticación** | **Estado: Completada** |  |

##### **Historia de Usuario**

**Como** administrador de la plataforma de pagos, **quiero** autenticarme con doble factor, **para** proteger el acceso a información financiera sensible, evitando accesos no autorizados.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature 2.1:** Autenticación y generación de credenciales

**Escenario:** Inicio de sesión exitoso con segundo factor configurado

**Given** un administrador con credenciales válidas y segundo factor configurado

**When** inicia sesión correctamente

**Then** el sistema le permite acceder a la plataforma

**And** garantiza que la sesión es segura

**Escenario:** Cuenta bloqueada por múltiples intentos fallidos de acceso

**Given** un administrador que ha fallado múltiples intentos de autenticación

**When** intenta iniciar sesión nuevamente

**Then** el sistema bloquea temporalmente el acceso a la plataforma

**And** notifica el evento como un posible riesgo de seguridad

**Escenario:** Inicio de sesión con código de segundo factor inválido o expirado  
**Given** un administrador con credenciales válidas y segundo factor configurado  
**When** ingresa un código de segundo factor no válido

**Then** el sistema rechaza el acceso a la plataforma

**Escenario:** Inicio de sesión sin método de segundo factor configurado  
**Given** un administrador sin un método de autenticación de segundo factor  
**When** intenta iniciar sesión

**Then** el sistema lo lleva a configurar un método de segundo factor

##### **Notas técnicas y de seguridad**

* Un código de segundo factor no válido incluye aquel que es incorrecto o está vencido.

* Se sugiere usar JWT con algoritmo RS256. Token de acceso con TTL de 15 minutos. Token de renovación con TTL de 7 días.

* Blacklist de tokens revocados implementada en REDIS para gestión de cierre de sesión y revocación manual.

* TOTP (RFC 6238): intervalo de 30 segundos, ventana de tolerancia de ±1 intervalo.

* Bloqueo de cuenta: 5 intentos fallidos en 10 minutos → bloqueo de 30 minutos.

* Cookies de token de renovación: HttpOnly, Secure, SameSite=Strict.

* Contraseñas almacenadas con bcrypt, cost factor mínimo 12\.

### **SPRINT 2**

**Seguridad avanzada, ciclo de vida del pago y trazabilidad**

**Objetivo:** Completar el ciclo de vida del pago con resultados reales, habilitar las notificaciones automáticas hacia los comercios, robustecer la gestión de credenciales y establecer la bitácora de auditoría inmutable.

### 

Con los fundamentos del Sprint 1 en producción, el Sprint 2 completa la experiencia transaccional y la hace confiable para comercios reales. Se incorpora la respuesta del procesador externo de pagos (aprobación y rechazo), lo que cierra el ciclo iniciado en el Sprint 1\. Se implementan los webhooks, que son el mecanismo por el cual los sistemas del comercio se enteran del resultado de cada pago sin necesidad de hacer consultas periódicas. La gestión avanzada de credenciales (revocación, rotación, separación de entornos) hace la plataforma más resiliente ante incidentes de seguridad. La bitácora de auditoría, que registra de forma inmutable cada evento del sistema, es el requisito de cumplimiento más importante de la plataforma: sin ella no se puede operar en el sector financiero. Se agrega también la consulta paginada de transacciones, que es la funcionalidad básica de seguimiento operativo que cualquier comercio necesita desde el primer día.

**Valor entregado:** *Al término del Sprint 2, la plataforma procesa pagos completos con notificación en tiempo real, tiene credenciales gestionables de forma segura y cuenta con trazabilidad total de cada operación.*

#### **HU006 \- Validación de credenciales por solicitud de pago**

| Épica: EP02 | Feature: FE201 | Sprint: Sprint 2 | Prioridad: Crítica |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Sistema interno (capa de seguridad)** | **Módulo: Seguridad / Autorización** | **Estado: Completada** |

##### **Historia de Usuario**

**Como** administrador de la plataforma de pagos, **quiero** que el sistema verifique en cada solicitud de pago que las credenciales del comercio sean válidas, **para** garantizar que únicamente los comercios autorizados puedan realizar transacciones.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature 2.1:** Autenticación y generación de credenciales

**Escenario:** Solicitud de pago con credenciales válidas  
**Given** que las credenciales del comercio están vigentes  
**And** las credenciales tienen permisos para realizar transacciones

**When** el sistema recibe una transacción del comercio

**Then** el pago generado por el comercio es autorizado

**And** el saldo del comercio es modificado según el valor del pago

**Escenario:** Solicitud de pago no exitosa debido a permisos insuficientes

**Given** que las credenciales del comercio no tienen permiso para realizar transacciones

**When** el sistema intenta usar las credenciales para iniciar una transacción

**Then** el sistema rechaza la solicitud de pago

**And** muestra un mensaje al comercio que sus credenciales no tienen el permiso requerido para esa operación

**And** el saldo del comercio no es modificado

**Escenario:** Solicitud de pago con credenciales de otro comercio.

**Given** que las credenciales ingresadas corresponden a otro comercio

**When** el comercio intenta usarlas para realizar operaciones

**Then** el sistema rechaza la solicitud

**And** muestra un mensaje de que las credenciales no son válidas

##### **Notas técnicas y de seguridad**

* Implementar como filtro de seguridad en la cadena de Spring Security: extraer el Bearer token, calcular hash y comparar con BD.

* Cachear la validación de credenciales en REDIS con TTL de 5 minutos para reducir la carga sobre la base de datos.

* El sistema de autorización implementa RBAC por endpoint y reglas ABAC básicas (solo el propietario del recurso puede modificarlo).

**Dependencias**

* HU002 — Credenciales deben existir en el sistema

#### **HU007 \- Consulta del perfil del comercio**

| Épica: EP01 | Feature: FE102 | Sprint: Sprint 2 | Prioridad: Media |
| :---- | :---- | :---- | :---- |
| **Story Points: 3 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Comercios / Perfil** | **Estado: Completada** |

##### **Historia de Usuario**

**Como** representante del comercio**, quiero** consultar en cualquier momento la información de identificación de mi comercio**, para** verificar que los datos del comercio estén correctos.

##### 

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Consulta y administración del perfil de un comercio registrado

**Escenario:** Consulta exitosa del perfil del propio comercio

**Given** que soy el administrador de un comercio activo y estoy autenticado en la plataforma

**When** accedo a la sección de perfil de mi comercio

**Then** el sistema me muestra la información completa de mi comercio: nombre, datos de contacto, estado actual y datos bancarios registrados

**Escenario:** No es posible consultar el perfil de otro comercio

**Given** qué estoy autenticado como administrador de mi comercio

**When** intento acceder a la información de un comercio diferente al mío

**Then** el sistema niega el acceso e informa que no tengo permisos para ver esa información

**Escenario:** Un comercio suspendido puede consultar su perfil pero no operar

**Given** qué mi comercio se encuentra en estado suspendido y estoy autenticado en la plataforma

**When** accedo a la sección de perfil

**Then** el sistema muestra mi información de perfil en modo solo lectura

**And** el sistema indica claramente que mi cuenta está suspendida y que no puedo procesar ni modificar datos

**Escenario:** Acceso denegado sin autenticación

**Given** que no estoy autenticado en la plataforma

**When** intento acceder a la sección de perfil de un comercio

**Then** el sistema rechaza la solicitud e indica que se requiere autenticación para acceder a este recurso

##### **Notas técnicas y de seguridad**

* Los datos bancarios se muestran parcialmente enmascarados por seguridad (últimos 4 dígitos de la cuenta).

* El filtro de mercado (merchantID) es aplicado automáticamente por el backend según el token de sesión.

* Los datos bancarios se almacenan cifrados en reposo con AES-256.

**Dependencias**

* HU001 \- El comercio debe estar registrado en la plataforma

* HU005 \- El administrador debe estar autenticado con doble factor

#### **HU008 \- Actualización de información bancaria del comercio**

| Épica: EP01 | Feature: FE102 | Sprint: Sprint 2 | Prioridad: Media |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Comercios / Perfil** | **Estado: Completada** |

##### **Historia de Usuario**

**Como** administrador de un comercio**, quiero** actualizar la información bancaria del comercio**, para** mantener dichos datos vigentes, garantizando que los fondos se transfieran correctamente a mi cuenta.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Consulta y administración del perfil de un comercio registrado

**Escenario:** Actualización exitosa de datos de contacto del comercio

**Given** que soy el administrador de mi comercio y estoy autenticado en la plataforma

**When** modifico los datos de contacto (correo, telefono o direccion) y guardo los cambios

**Then** el sistema confirma que la información fue actualizada correctamente  
**And** el sistema registra en la bitácora qué campos fueron modificados, por quién y en qué momento

**Escenario:** Actualización de datos bancarios queda en estado de verificación pendiente

**Given** que soy el administrador de mi comercio y estoy autenticado en la plataforma

**When** modifico los datos de mi cuenta bancaria y guardo los cambios

**Then** el sistema confirma que la solicitud de cambio fue recibida

**And** los nuevos datos bancarios quedan en estado de verificación pendiente hasta ser validados por el equipo de la plataforma

**And** mientras la verificación esté pendiente, las liquidaciones continuan realizandose a la cuenta bancaria anterior

**And** el sistema registra en la bitácora los campos modificados, el autor del cambio el momento exacto

**Escenario:** No es posible modificar datos de identificación del comercio

**Given** que intento modificar el número de identificación fiscal o el tipo de negocio registrados al crear la cuenta

**When** guardo los cambios

**Then** el sistema me informa que esos datos son de registro único y no pueden modificarse

**And** el sistema sugiere contactar al equipo de soporte si hay un error en los datos originales

**Escenario:** Usuario con el rol de Desarrollador no puede modificar el perfil del comercio

**Given** que estoy autenticado en la plataforma con el rol de desarrollador

**When** intento actualizar los datos de perfil o configuración bancaria del comercio

**Then** el sistema rechaza la acción e informa que mi rol no tiene permisos para modificar esta información

**Escenario:** Intento de actualización con datos bancarios en formato invalido

**Given** que soy el administrador de mi comercio y estoy autenticado

**When** ingreso datos bancarios con un formato incorrecto (número de cuenta con caracteres no numéricos o con una longitud inválida) e intento guardar

**Then** el sistema rechaza la actualización e indica qué campos tiene un formato inválido

**And** los datos bancarios anteriores permanecen sin cambios

##### **Notas técnicas y de seguridad**

* Aplicar regla ABAC: solo el propietario del recurso o un PLATFORM\_ADMIN puede modificar el perfil.

* Implementar audit diff: registrar únicamente los campos que cambiaron, no el objeto completo.

* Campos inmutables post-registro: merchantID, businessType, taxId. Intentar modificarlos debe retornar error de validación.

**Dependencias**

* HU007 \- El administrador debe poder consultar el perfil antes de editarlo

#### **HU009 \- Revocación inmediata de credenciales comprometidas**

| Épica: EP02 | Feature: FE202 | Sprint: Sprint 2 | Prioridad: Alta |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Seguridad / Credenciales** | **Estado: En proceso** |

##### **Historia de Usuario**

**Como** administrador de un comercio**, quiero** revocar inmediatamente cualquiera de mis credenciales activas ante una sospecha de uso no autorizado**, para** detener de inmediato el acceso indebido a la plataforma, protegiendo las operaciones de mi negocio ante una posible filtración.

##### 

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Revocación y rotación de credenciales de acceso a la API

**Escenario:** Revocación exitosa de una credencial activa

**Given** que tengo credenciales activas visibles en mi panel de administración

**When** seleccionó una credencial y confirmó su revocación

**Then** la credencial queda inhabilitada de forma inmediata

**And** el sistema confirma la revocación  
**And** el evento queda registrado en la bitácora de auditoría con la hora exacta y el administrador que realizó la acción

**Escenario:** Credencial revocada no puede autenticar nuevas solicitudes

**Given** que una de mis credenciales ha sido revocada

**When** el sistema recibe una solicitud de pago que utiliza esa credencial revocada

**Then** el sistema rechaza la solicitud

**And** informa que las credenciales presentadas no son válidas

**Escenario:** No es posible revocar una credencial que fue revocada

**Given** que una credencial de mi comercio ya se encuentra en estado revocada

**When** intento devolverla a su estado anterior

**Then** el sistema informa que la credencial ya fue revocada con anterioridad y no requiere ninguna acción adicional

**Escenario:** No es posible revocar una credencial de otro comercio

**Given** que estoy autenticado como administrador de mi comercio

**When** intentó revocar una credencial que pertenece a un comercio diferente al mío

**Then** el sistema rechaza la acción e informa que no tengo permisos sobre esa credencial

##### **Notas técnicas y de seguridad**

* La revocación debe ser efectiva en menos de 5 segundos (invalida la caché REDIS inmediatamente).

* Una credencial revocada no puede reactivarse. Solo se puede generar un nuevo juego de credenciales.

**Dependencias**

* HU002 \- Las credenciales debe haber sido generadas previamente en el sistema

#### **HU010 \- Rotación de credenciales sin interrupción del servicio**

| Épica: EP02 | Feature: FE202 | Sprint: Sprint 2 | Prioridad: Media |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Seguridad	/ Credenciales** | **Estado: Completada** |

##### **Historia de Usuario**

**Como** administrador de un comercio**, quiero** rotar mis credenciales activas de forma que las nuevas sean generadas y las antiguas sigan siendo válidas temporalmente durante la transición**, para** migrar mis sistemas a las nuevas credenciales sin interrumpir el servicio a mis clientes.

##### 

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Ciclo de vida de las credenciales

**Escenario:** Rotación exitosa con periodo de gracia para migración **Given** que tengo credenciales activas y quiero renovarlas de forma segura  
**When** solicitó la rotación de mis credenciales actuales

**Then** el sistema genera un nuevo juego de credenciales y me lo muestra una única vez para que el administrador las custodie

**And** las credenciales anteriores permanecen válidas durante un período de gracia de 24 horas para permitir la migración de los sistemas de mi comercio

**And** el sistema muestra claramente la fecha y hora exacta en que las credenciales anteriores serán revocadas automáticamente.

**Escenario:** Las credenciales antiguas son revocadas automáticamente al vencer el periodo de gracia

**Given** que una rotación de credenciales fue iniciada y el periodo de gracia de 24 horas ha transcurrido

**When** el sistema ejecuta el proceso automático de revocación

**Then** las credenciales antiguas quedan inhabilitadas y ya no pueden utilizarse para autenticar solicitudes.

**And** el evento de revocación automática queda registrado en la bitácora de auditoría

**Escenario:** No es posible rotar credenciales cuando se ha alcanzado el límite máximo de credenciales activas

**Given** qué mi comercio ya tiene el número máximo de credenciales activas permitidas en este entorno

**When** intentó iniciar una rotación que generaría un nuevo juego de credenciales

**Then** el sistema informa que se ha alcanzado el límite de credenciales activas

**And** sugiere revocar alguna credencial existente antes de continuar con la rotación

##### **Notas técnicas y de seguridad**

* El período de gracia de 24 horas permite que los sistemas del comercio migren sin tiempo de inactividad (downtime).

* Un proceso programado (job) revoca automáticamente las credenciales en estado ROTATING al vencer el TTL.

* La nueva credencial generada sigue el mismo esquema de permisos que la credencial rotada, salvo indicación explícita del administrador.

**Dependencias**

* HU002 \- Las credenciales deben haber sido generadas previamente en el sistema

* HU009 \- La lógica de revocación es reutilizada al vencer el periodo de gracia

#### **HU011 \- Procesamiento de un pago**

| Épica: EP03 | Feature: FE302 | Sprint: Sprint 2 | Prioridad: Crítica |
| :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Sistema interno / Procesador externo** | **Módulo: Pagos / Máquina de estados** | **Estado: En proceso** |

##### **Historia de Usuario**

**Como** motor de procesamiento de pagos de la plataforma**, quiero** actualizar el estado de cada transacción según la respuesta recibida del procesador de pagos externo**, para** reflejar el resultado real de cada cobro y habilitar las notificaciones correspondientes al comercio y al usuario que realizó el pago.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Procesamiento del resultado de pagos por parte del procesador financiero externo

**Escenario:** Pago aprobado por el procesador financiero externo

**Given** que el procesador financiero externo confirma la aprobación de la transacción

**When** el sistema recibe y procesa esa confirmación

**Then** el pago queda marcado como aprobado en la plataforma

**And** el comercio recibe la notificación del pago aprobado a través de su canal de notificaciones configurado

**And** el evento queda registrado en la bitácora de auditoría del pago

**Escenario Outline:** Pago rechazado por el procesador financiero según el motivo de rechazo

**Given** que el procesador financiero externo rechaza la transacción por \<motivo\_rechazo\>

**When** el sistema procesa el rechazo

**Then** el pago queda marcado como rechazado con el motivo correspondiente en términos de negocio: \<descripcion\_negocio\>

**And** el comercio recibe la notificación del rechazo con el motivo de negocio correspondiente

**Ejemplos:**

| motivo\_rechazo | descripcion\_negocio |
| :---- | :---- |
| fondos insuficientes | El medio de pago del cliente no tiene saldo suficiente |
| tarjeta expirada | El medio de pago presentado está vencido |
| datos incorrectos | Los datos del medio de pago no coinciden con los registrados |
| tarjeta bloqueada | El medio de pago fue bloqueada por la institución financiera del cliente |

**Escenario:** Fallo de la transacción por error de comunicación con el procesador externo

**Given** que la plataforma intenta comunicarse con el procesador financiero externo para procesar la transacción

**When** el procesador no responde dentro del tiempo máximo de espera o retorna un error de conectividad

**Then** el pago queda marcado como fallido (FAILED) en la plataforma

**And** el comercio recibe la notificación indicando que el pago no pudo ser procesado por un error técnico

**And** el evento queda registrado en la bitácora con el detalle del error de comunicación

##### **Notas técnicas y de seguridad**

* Mapear todos los códigos de respuesta del PSP a estados internos y mensajes de negocio normalizados.

* El motivo de rechazo debe ser uno de un enum controlado y debe expresarse en términos del negocio, no como código técnico.

* Implementar reintentos con backoff exponencial para fallos de conectividad con el PSP (máximo 3 reintentos).

**Dependencias**

* HU003 \- La transacción debe existir en el sistema

* HU004 \- La máquina de estados debe estar implementadas

#### **HU012 \- Configuración de canales de notificación automática**

| Épica: EP03 | Feature: FE304 | Sprint: Sprint 2 | Prioridad: Alta |
| :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Pagos / Webhooks** | **Estado: En proceso** |

##### **Historia de Usuario**

**Como** administrador de un comercio**, quiero** registrar una o más direcciones URLs de notificación en las que mi sistema recibirá alertas automáticas cuando el estado de un pago cambie**, para** mantener mi plataforma actualizada en tiempo real sobre el resultado de cada cobro sin necesidad de hacer consultas periódicas.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Configuración y gestión de notificaciones automáticas de eventos de pago

**Escenario:** Registro exitoso de un canal de notificación

**Given** que soy el administrador de mi comercio y tengo una dirección URL activa en mi sistema para recibir notificaciones

**When** registro esa URL como canal de notificación indicando los tipos de eventos que quiero recibir (por ejemplo: pago aprobado, pago rechazado, reembolso)

**Then** el sistema confirma el registro del canal y le asigna un identificador único

**And** el canal queda activo y listo para recibir notificaciones de los eventos seleccionados

**Escenario:** La plataforma verifica que el canal de notificación esté disponible antes de activarlo

**Given** que he registrado una nueva dirección de notificación en mi comercio

**When** el sistema intenta activar el canal

**Then** la plataforma envía un mensaje de verificación a esa dirección URL y espera confirmación

**And** el canal solo queda activo si mi sistema responde correctamente con el código de verificación esperado

**Escenario:** La plataforma reintenta la entrega cuando el canal no responde y desactiva el canal tras múltiples fallos

**Given** que el canal de notificación de mi comercio no está disponible en el momento de la entrega

**When** la plataforma intenta entregar la notificación de un evento pago

**Then** el sistema reintentar la entrega de forma automática en intervalos de tiempo crecientes

**And** si luego de varios intentos el canal sigue sin responder, el canal es marcado como inactivo

**And** el administrador del comercio es notificado del problema de entrega

**Escenario:** No es posible registrar un canal con una URL con formato inválido

**Given** que intento registrar una URL de notificación con un formato inválido (sin protocolo HTTPS o con caracteres no permitidos)

**When** envío la solicitud de registro del canal

**Then** el sistema rechaza el registro e indica que la URL proporcionada no tiene un formato válido

**Escenario:** No es posible registrar más canales de notificación cuando se alcanza el límite

**Given** que mi comercio ya tiene el número máximo de canales de notificación activos permitidos

**When** intento registrar un canal adicional

**Then** el sistema informa que se ha alcanzado el límite de canales activos

**And** sugiere desactivar algún canal existente antes de registrar uno nuevo

##### **Notas técnicas y de seguridad**

* Cada notificación incluye una firma digital (HMAC-SHA256) para que el comercio pueda verificar su autenticidad.

* El payload de cada notificación incluye: tipo de evento, identificador del pago, estado, monto, moneda y timestamp.

* Máximo 5 canales de notificación activos por comercio. Tiempo máximo de espera por respuesta del canal: 10 segundos.

* Reintentos con backoff exponencial: 1 min, 5 min, 30 min, 2 horas. Tras 4 intentos fallidos, el canal se marca como inactivo.

* Solo se aceptan URLs con protocolo HTTPS para garantizar la seguridad en la transmisión de eventos.

**Dependencias**

* HU001 \- El comercio debe estar registrado y activo en la plataforma

* HU012 \- Los cambios de estado de los pagos son los eventos que disparan las notificaciones

#### **HU013 \- Registro de eventos de auditoría**

| Épica: EP04 | Feature: FE401 | Sprint: Sprint 2 | Prioridad: Alta |  |
| :---- | :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Sistema interno** | **Módulo: Auditoría / Trazabilidad** | **Estado: En proceso** |  |

##### **Historia de Usuario**

**Como** representante de un comercio**, quiero** que la plataforma registre automáticamente cada cambio de estado de una transacción**, para** garantizar la trazabilidad completa e íntegra de cada pago.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Registro y consulta de la bitácora inmutable de eventos de pago

**Escenario:** Cada cambio de estado de un pago genera un registro de auditoría automático

**Given** que el estado de un pago ha cambiado dentro del ciclo de vida de la transacción

**When** el sistema persiste ese cambio de estado

**Then** se crea automáticamente un registro de auditoría con: identificador del pago, tipo de evento, estado anterior, nuevo estado, quién realizó la acción y el momento exacto en que ocurrió

**And** ese registro no puede ser editado ni eliminado bajo ninguna circunstancia

**Escenario:** La integridad de la bitácora de auditoría puede ser verificada **Given** que existe una secuencia de eventos de auditoría registrados para un pago  
**When** el sistema verifica la integridad de esos registros

**Then** cualquier alteración en un registro previo es detectada automáticamente por el sistema

**And** el sistema genera una alerta indicando que la integridad de la bitácora ha sido comprometida

**Escenario:** Un administrador autorizado puede consultar la bitácora de eventos de un pago

**Given** que soy un administrador de la plataforma o un administrador del comercio dueño del pago, y estoy autenticado

**When** consulto la bitácora de eventos de una transacción específica

**Then** el sistema muestra la lista cronológica de todos los eventos registrados para ese pago

**And** cada registro incluye: qué ocurrió, quién lo originó, desde qué estado venía, a qué estado pasó y cuándo sucedió exactamente

**Escenario:** No es posible modificar ni eliminar un registro de la bitácora

**Given** que existe un registro de auditoría en la bitácora de la plataforma

**When** cualquier actor (interno o externo) intenta modificar o eliminar ese registro

**Then** el sistema rechaza la operación y el registro permanece intacto

**And** el intento de modificación queda registrado como un evento de alerta de seguridad

##### **Notas técnicas y de seguridad**

* Tabla audit\_events con restricción de solo INSERT en la base de datos (trigger que rechaza UPDATE y DELETE).

* Cada registro tiene un hash encadenado al registro anterior (SHA-256) para garantizar inmutabilidad verificable.

* Índices en payment\_id, event\_type y timestamp para consultas de auditoría eficientes.

**Dependencias**

* HU003 \- Las transacciones deben existir para poder registrar sus eventos

* HU004 \- Los cambios de estado son los principales disparadores de eventos de auditoría

* HU012 \- Los eventos de entrega de webhooks también deben quedar en la bitácora

#### **HU014 \- Consulta del listado de pagos del comercio**

| Épica: EP03 | Feature: FE301 | Sprint: Sprint 2 | Prioridad: Alta |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Pagos / Consultas** | **Estado: En proceso** |

##### **Historia de Usuario**

**Como** administrador de un comercio**, quiero** filtrar el listado de las transacciones del comercio**, para** monitorear la actividad de cobros de mi plataforma.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Consulta y seguimiento de transacciones de pago por parte del comercio

**Escenario:** Consulta exitosa de pagos aprobados en un período determinado

**Given** que tengo transacciones registradas en diferentes estados durante el último mes

**When** consulto mis pagos filtrando solo los aprobados dentro de un rango de fechas y solicito la primera página de resultados

**Then** el sistema me muestra el listado de pagos aprobados en ese período con la cantidad de resultados configurada

**And** puedo navegar entre páginas para ver el resto de los resultados

**Escenario:** El comercio solo puede consultar sus propios pagos

**Given** que estoy autenticado como administrador de mi comercio **When** consulto el listado de mis pagos

**Then** el sistema aplica automáticamente un filtro de comercio basado en mi sesión activa

**And** el listado muestra únicamente las transacciones de mi comercio, sin incluir transacciones de otros comercios de la plataforma

**Escenario:** Consulta con filtros que no retornan resultados devuelve una lista vacía **Given** que aplico filtros de búsqueda (estado, rango de fechas o monto) para los que no existe ninguna transacción en mi comercio

**When** ejecuto la consulta

**Then** el sistema devuelve una lista de resultados vacía

**And** el sistema indica que no se encontraron transacciones para los filtros aplicados, sin generar un error

**Escenario:** Consulta con rango de fechas inválido no retorna resultados

**Given** que intento consultar mis transacciones con un rango de fechas en el que la fecha de inicio es posterior a la fecha de fin

**When** ejecuto la consulta

**Then** el sistema informa que el rango de fechas no es válido y no devuelve ningún resultado

##### **Notas técnicas y de seguridad**

* El filtro de comercio se aplica automáticamente en el backend según el token de sesión, no como parámetro del cliente.

* Tamaño máximo de página: 100 registros.

* Índice compuesto en (merchant\_id, status, created\_at) para consultas frecuentes.

**Dependencias**

* HU003 \- Las transacciones deben existir en el sistema para poder consultarlas

* HU005 \- El administrador debe estar autenticado

* HU006 \- Las credenciales deben ser validadas antes de procesar la consulta

### **SPRINT 3**

**Seguridad avanzada, ciclo de vida del pago y trazabilidad**

**Objetivo:** Implementar los distintos tipos de reembolsos,, brindar al comercio reportes de analítica sobre su actividad, habilitar la exportación de datos para conciliación y consolidar el portal administrativo interno.

El Sprint 3 completa el ciclo económico de la plataforma y aporta las herramientas de gestión y visibilidad que hacen de este producto algo que puede operar de forma autónoma. Los reembolsos cierran el último escenario del ciclo del pago: un cobro puede ser devuelto total o parcialmente, con control estricto de los montos y trazabilidad completa. La gestión de usuarios dentro del comercio permite que equipos enteros operen con sus propios accesos y permisos diferenciados, sin compartir credenciales. Los reportes de volumen y distribución por estado son las herramientas de diagnóstico que los comercios necesitan para evaluar su integración y detectar problemas. La exportación de datos en archivo descargable atiende una necesidad contable real: la conciliación mensual entre lo registrado en la plataforma y los movimientos bancarios del comercio. Finalmente, el portal de administración interna consolida el control operativo de la plataforma, permitiendo aprobar, suspender y monitorear comercios de manera estructurada y con evidencia de auditoría.

**Valor entregado:** *Al término del Sprint 3, la plataforma es un producto completo: los comercios cobran, reciben notificaciones, gestionan devoluciones, consultan reportes y descargan datos. Los administradores controlan el ecosistema de forma segura y trazable.*

#### **HU015 \- Gestión de usuarios y roles del equipo del comercio**

| Épica: EP01 | Feature: FE103 | Sprint: Sprint 3 | Prioridad: Media |
| :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Comercio (B2B) — Propietario** | **Módulo: Comercios / Usuarios** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** propietario del comercio**, quiero** invitar a miembros de mi equipo y asignarles roles con permisos diferenciados dentro del portal de mi comercio**, para** delegar tareas operativas con el principio de mínimo acceso, sin necesidad de compartir mis credenciales de administrador.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Gestión de usuarios y roles dentro del portal de un comercio

**Escenario:** Invitación exitosa de un nuevo miembro con rol de desarrollador

**Given** que soy el propietario del comercio y quiero agregar un desarrollador a mi equipo

**When** ingreso el correo del nuevo miembro y le asigno el rol de desarrollador

**Then** el sistema envía una invitación al correo del nuevo miembro con instrucciones para activar su cuenta

**And** el nuevo usuario queda registrado en estado de invitación pendiente hasta que acepte e ingrese por primera vez

**And** queda registrado en la bitácora que el propietario realizó esta invitación

**Escenario:** Un usuario con rol de desarrollador no puede gestionar a otros usuarios

**Given** que estoy autenticado en el portal con el rol de desarrollador de mi comercio

**When** intento crear, modificar o eliminar la cuenta de otro usuario del comercio

**Then** el sistema niega la acción e informa que mi rol no tiene permisos para gestionar usuarios

##### **Notas técnicas y de seguridad**

* Roles: MERCHANT\_OWNER (acceso total), MERCHANT\_ADMIN (gestión sin transferir propiedad), DEVELOPER (credenciales y pagos), ANALYST (solo lectura y reportes).

* El link de invitación expira en 48 horas y es de un solo uso.

**Dependencias**

* HU-01

* HU-05

#### **HU016 \- Solicitud de reembolso total de un pago**

| Épica: EP03 | Feature: FE303 | Sprint: Sprint 3 | Prioridad: Alta |
| :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Pagos / Reembolsos** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** administrador del comercio**, quiero** solicitar el reembolso completo del monto de un pago ya aprobado**, para** devolver la totalidad del dinero al cliente en los casos en que sea necesario, manteniendo la trazabilidad completa del proceso.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Gestión de reembolsos totales y parciales sobre pagos aprobados

**Escenario:** Reembolso total exitoso de un pago aprobado

**Given** que tengo un pago aprobado que mi cliente solicita que sea devuelto en su totalidad

**When** solicito el reembolso completo de ese pago indicando el motivo de la devolución

**Then** el sistema registra la operación de reembolso y le asigna un identificador único

**And** el pago original queda marcado como reembolsado en su totalidad  
**And** mi canal de notificación recibe el aviso del reembolso procesado

**Escenario:** No se puede reembolsar un pago que no está aprobado

**Given** que el pago que quiero reembolsar fue rechazado o está en proceso

**When** intento solicitar su reembolso

**Then** el sistema me informa que ese pago no puede ser reembolsado porque no está en estado aprobado

**Escenario:** Un pago ya reembolsado en su totalidad no puede reembolsarse nuevamente

**Given** que un pago ya fue reembolsado completamente con anterioridad

**When** intento solicitar un nuevo reembolso sobre ese mismo pago

**Then** el sistema me informa que ese pago ya fue reembolsado en su totalidad y no es posible procesar otro reembolso

##### **Notas técnicas y de seguridad**

* El reembolso genera un registro contable independiente en la tabla de reembolsos con su propio identificador.

* REFUNDED es un estado terminal: no puede transicionar a ningún otro estado posterior.

* Implementar idempotencia en reembolsos para evitar dobles procesamiento.

**Dependencias**

* HU003

* HU012

* HU013

#### **HU017 \- Solicitud de reembolso parcial de un pago**

| Épica: EP03 | Feature: FE303 | Sprint: Sprint 3 | Prioridad: Alta |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Pagos / Reembolsos** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** administrador del comercio**, quiero** solicitar reembolsos parciales sobre un pago aprobado, controlando que la suma de devoluciones no supere el monto original cobrado**, para** gestionar devoluciones de artículos individuales de un pedido sin tener que reembolsar el cobro completo.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Gestión de reembolsos totales y parciales sobre pagos aprobados

**Escenario:** Reembolso parcial exitoso dentro del monto disponible

**Given** que tengo un pago aprobado sobre el que quiero realizar una devolución parcial

**When** solicito un reembolso por un monto menor al total del pago

**Then** el sistema procesa el reembolso parcial y el pago queda marcado como parcialmente reembolsado

**And** el monto disponible para futuros reembolsos se reduce en la cantidad ya devuelta

**Escenario:** No es posible reembolsar más del monto disponible

**Given** que ya he realizado uno o más reembolsos parciales sobre un pago y hay un monto restante disponible para devolver

**When** intento solicitar un reembolso por un monto mayor al que aún está disponible

**Then** el sistema me informa que el monto solicitado supera el disponible para reembolso y no procesa la operación

##### **Notas técnicas y de seguridad**

* El campo refundedAmount en la transacción acumula cada reembolso parcial.

* Cuando refundedAmount \= amount, el estado del pago pasa automáticamente a REFUNDED.

**Dependencias**

* HU-17

#### **HU018 \- Consulta del historial de eventos de un pago**

| Épica: EP04 | Feature: FE402 | Sprint: Sprint 3 | Prioridad: Media |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio / Soporte** | **Módulo: Auditoría / Trazabilidad** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** administrador del comercio o miembro del equipo de soporte**, quiero** consultar la secuencia cronológica completa de todos los eventos que ocurrieron sobre un pago específico**, para** entender por qué un pago fue rechazado o reembolsado, resolver disputas con clientes y responder requerimientos de auditoría.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Registro y consulta de la bitácora inmutable de eventos de pago

**Escenario:** Consulta exitosa del historial de eventos de un pago propio

**Given** que tengo un pago registrado en la plataforma que ha pasado por varios estados en su ciclo de vida

**When** consulto el historial de eventos de ese pago

**Then** el sistema me muestra la lista de eventos en orden cronológico, desde el más antiguo hasta el más reciente

**And** cada evento indica: qué ocurrió, de qué estado venía, a qué estado pasó, quién lo originó y cuándo sucedió exactamente

**Escenario:** No es posible consultar el historial de pagos de otro comercio **Given** que el pago que quiero consultar pertenece a un comercio diferente al mío  
**When** intento consultar su historial de eventos

**Then** el sistema no muestra ningún resultado, como si ese pago no existiera, para no revelar información de otros comercios

##### **Notas técnicas y de seguridad**

* El sistema retorna 404 (no encontrado) en lugar de 403 (prohibido) para evitar la enumeración de IDs de otros comercios.

* Los datos del método de pago en la metadata de eventos se muestran enmascarados (nunca datos completos de tarjeta).

**Dependencias**

* HU014

#### **HU019 \- Reporte de volumen de transacciones por período**

| Épica: EP05 | Feature: FE501 | Sprint: Sprint 3 | Prioridad: Media |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Reportes / Analítica** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** administrador del comercio**, quiero** generar un reporte con el volumen total de mis transacciones (cantidad y monto acumulado) agrupado por día o por mes dentro de un rango de fechas**, para** monitorear el desempeño de mis operaciones de cobro y tomar decisiones basadas en el comportamiento real de mis pagos.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Generación de reportes de actividad transaccional del comercio

**Escenario:** Generación exitosa del reporte de volumen agrupado por día

**Given** que tengo transacciones registradas en la plataforma durante el último mes

**When** solicito un reporte de volumen para ese período agrupado por día

**Then** el sistema me entrega un resumen con una entrada por día que incluye: cantidad de transacciones del día, monto total acumulado, cuántas fueron aprobadas y cuántas rechazadas  
**Escenario:** Rango de fechas inválido no genera reporte

**Given** que indico un período en el que la fecha de inicio es posterior a la fecha de fin, o el rango supera el máximo permitido

**When** solicito el reporte

**And** el sistema me informa que el rango de fechas no es válido y no genera ningún reporte

##### **Notas técnicas y de seguridad**

* Rango máximo para agrupación diaria: 90 días. Para períodos mayores usar agrupación mensual.

* Los reportes se calculan sobre la réplica de lectura para no afectar el motor transaccional.

* Cachear resultados de reportes con fechas pasadas (rango cerrado) con TTL de 1 hora.

**Dependencias**

* HU-03

* HU-12

#### **HU020 \- Dashboard de distribución de pagos por estado**

| Épica: EP05 | Feature: FE502 | Sprint: Sprint 3 | Prioridad: Media |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Comercio (B2B)** | **Módulo: Reportes / Analítica** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** administrador del comercio**, quiero** visualizar la distribución porcentual de mis transacciones por estado (aprobadas, rechazadas, reembolsadas) para un período dado**, para** identificar tendencias de rechazo, detectar posibles problemas en mi integración y tener un indicador claro de la salud operativa de mis cobros.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Generación de reportes de actividad transaccional del comercio

**Escenario:** Consulta exitosa del dashboard de distribución por estado

**Given** que tengo transacciones procesadas durante el último mes en diferentes estados finales

**When** consulto el dashboard de distribución para ese período

**Then** el sistema me muestra el total de transacciones finalizadas y el desglose por estado: cuántas y qué porcentaje corresponde a aprobadas, rechazadas y reembolsadas

**And** veo también la tasa de aprobación general de mis cobros en ese período

##### **Notas técnicas y de seguridad**

* La tasa de aprobación \= APPROVED / (APPROVED \+ REJECTED \+ FAILED) × 100\.

* Los estados CREATED y PROCESSING (en curso) se excluyen del cálculo de la tasa de aprobación.

**Dependencias**

* HU-20

#### **HU021 \- Exportación de transacciones a archivo descargable**

| Épica: EP05 | Feature: FE503 | Sprint: Sprint 3 | Prioridad: Baja |
| :---- | :---- | :---- | :---- |
| **Story Points: 3 SP** | **Actor / Rol: Comercio (B2B) — rol Analista** | **Módulo: Reportes / Exportación** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** analista financiero o contador del comercio**, quiero** descargar un archivo con el detalle de todas mis transacciones de un período seleccionado**, para** realizar la conciliación contable mensual en mis herramientas de gestión financiera externas (ERP, hojas de cálculo).

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Exportación de datos transaccionales para conciliación financiera externa

**Escenario:** Exportación exitosa del archivo para un período con pocos registros

**Given** que quiero exportar las transacciones de un mes específico con una cantidad manejable de registros

**When** solicito la exportación del archivo para ese período

**Then** el sistema genera y descarga de inmediato un archivo con el detalle de cada transacción del período

**And** el archivo incluye para cada transacción: identificador, fecha, monto, moneda, estado y código de autorización si fue aprobada

**Escenario:** Exportación programada cuando el volumen de datos es muy alto

**Given** que el período solicitado contiene una cantidad muy grande de transacciones que no puede entregarse de inmediato

**When** solicito la exportación

**Then** el sistema acepta mi solicitud y me informa que el archivo se está generando

**And** cuando el archivo esté listo, recibo una notificación por correo con un enlace de descarga seguro de uso temporal

##### **Notas técnicas y de seguridad**

* Umbral de exportación inmediata: menos de 10,000 registros. Por encima, procesamiento asíncrono.

* El enlace de descarga es un URL firmado (pre-signed URL) con expiración de 24 horas.

* Los datos sensibles del método de pago se entregan enmascarados en el archivo.

**Dependencias**

* HU-20

* HU-21

#### **HU022 \- Aprobación de comercios**

| Épica: EP01 | Feature: FE101 | Sprint: Sprint 3 | Prioridad: Alta |
| :---- | :---- | :---- | :---- |
| **Story Points: 5 SP** | **Actor / Rol: Administrador de plataforma** | **Módulo: Admin Portal / Gestión** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** administrador interno de la plataforma**, quiero** revisar los comercios en proceso de verificación, aprobar los que cumplen los requisitos y suspender los que presentan actividad irregular**, para** mantener la integridad y la confianza del ecosistema de la plataforma, garantizando que solo operen comercios verificados y confiables.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Administración y control de comercios desde el portal interno de la plataforma

**Escenario:** Aprobación de un comercio que completó su proceso de verificación

**Given** que un comercio ha completado su registro y está en proceso de verificación pendiente**When** el administrador de la plataforma revisa la información del comercio y la aprueba **When** el comercio pasa al estado activo y puede comenzar a operar en la plataforma  
**And** el comercio recibe una notificación por correo confirmando que su cuenta ha sido activada

**And** queda un registro de la aprobación en la bitácora con el administrador que tomó la decisión y el momento en que ocurrió

**Escenario:** Suspensión de un comercio por actividad sospechosa

**Given** que el administrador de la plataforma detecta actividad irregular en un comercio activo

**When** el administrador suspende al comercio indicando el motivo

**Then** todas las credenciales del comercio quedan inhabilitadas de forma inmediata

**And** no se aceptan nuevas solicitudes de pago de ese comercio

**And** queda un registro del evento en la bitácora con el detalle de la decisión tomada

##### **Notas técnicas y de seguridad**

* El rol PLATFORM\_ADMIN requiere MFA obligatorio para acceder al portal administrativo.

* Al suspender un comercio, la invalidación de sus credenciales debe ocurrir en menos de 5 segundos (caché REDIS).

* El portal administrativo permite paginar, buscar y filtrar comercios por estado, fecha de registro y país.

**Dependencias**

* HU-01

* HU-05

#### **HU023 \- Suspensión de comercios** 

| Épica: EP01 | Feature: FE101 | Sprint: Sprint 3 | Prioridad: Baja |
| :---- | :---- | :---- | :---- |
| **Story Points: 3 SP** | **Actor / Rol: Administrador de plataforma** | **Módulo: Admin Portal / Gestión** | **Estado: Por hacer** |

##### **Historia de Usuario**

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Administración y control de comercios desde el portal interno de la plataforma

**Escenario:** 

##### **Notas técnicas y de seguridad**

* 

**Dependencias**

* 

#### **HU024 \- Separación de entornos de pruebas y producción**

| Épica: EP06 | Feature: FE601 | Sprint: Sprint 3 | Prioridad: Alta |
| :---- | :---- | :---- | :---- |
| **Story Points: 8 SP** | **Actor / Rol: Desarrollador del comercio** | **Módulo: Seguridad / Entornos** | **Estado: Por hacer** |

##### **Historia de Usuario**

**Como** desarrollador integrador del comercio**, quiero** contar con dos entornos completamente independientes — uno para pruebas y otro para operaciones reales — con credenciales separadas para cada uno**, para** realizar integraciones y pruebas de mis flujos de pago sin afectar dinero real ni mezclar datos con el entorno de producción.

##### **Criterios de Aceptación — Lenguaje Gherkin**

**Feature:** Separación y gestión de entornos de pruebas y producción

**Escenario:** Credenciales de pruebas no pueden operar en el entorno de producción

**Given** que tengo credenciales generadas para el entorno de pruebas

**When** intento realizar un pago real usando esas credenciales de pruebas

**Then** el sistema rechaza la operación e informa que las credenciales no están habilitadas para operar en producción

**Escenario:** Activación del entorno de producción requiere revisión y aprobación

**Given** que mi comercio está activo en el entorno de pruebas pero no ha sido habilitado para producción

**When** solicito la activación de mi cuenta para el entorno de producción

**Then** el sistema registra mi solicitud e inicia el proceso de revisión de identidad del negocio

**And** el acceso a producción solo se habilita una vez que un administrador de la plataforma aprueba la solicitud

**Escenario:** El entorno de pruebas permite simular pagos sin procesamiento real

**Given** que cuento con credenciales de pruebas y uso los datos de pago de simulación provistos por la plataforma

**When** realizo un pago en el entorno de pruebas

**Then** el pago es procesado de forma simulada y refleja el comportamiento real sin realizar ningún cargo financiero

**And** puedo observar todos los estados del ciclo de vida del pago como en producción

##### **Notas técnicas y de seguridad**

* Todo recurso (transacción, credencial, webhook) tiene un campo environment: SANDBOX | PRODUCTION.

* En sandbox, el procesador de pagos externo es reemplazado por un mock PSP que responde en menos de 100ms.

* Las métricas y reportes de sandbox están completamente segregados de los de producción.

**Dependencias**

* HU-01

* HU-02

## **Glosario de referencia**

| Término | Descripción en términos del negocio |
| :---- | :---- |
| **Estado	inicial (transacción)** | Es aquel estado de una transacción o pago de un usuario por medio del motor de pagos que no ha sido aún enviado a validación por su institución financiera. No es un estado susceptible de reembolsos. |
| **Estado	intermedio (transacción)** | Es aquel estado de una transacción o pago de un usuario que está siendo procesado por el motor de pagos o en espera de su aprobación/denegación por parte de la institución financiera del usuario. |
| **Estado	final	exitoso (transacción)** | Es aquel estado de una transacción o pago de un usuario que ha sido exitosamente procesado por el motor de pagos y aprobado por su institución financiera. Es un estado susceptible de reembolsos siempre y cuando haya un monto reembolsable mayor a $0. |
| **Estado final no exitoso (transacción)** | Es aquel estado de una transacción o pago de un usuario que no ha sido exitosamente procesado por el motor de pagos o no ha sido aprobado por su institución financiera. No es un estado susceptible de reembolsos debido a que no se ha realizado el débito al usuario. |
| **Transacción o pago** | Es toda aquella solicitud que realiza el comercio para recibir pagos de sus clientes y que es susceptible de ser reembolsada o cancelada según las validaciones que realice el comercio, y así mismo, las políticas anti fraude de la plataforma. |
|  |  |
|  |  |

## 

## **Estados del ciclo de vida de una transacción**

| Estado | Descripción en términos del negocio |
| :---- | :---- |
| **CREATED** | El usuario inició un pago y se registró correctamente en el sistema. Este es el estado inicial de una transacción. |
| **PROCESSING** | La transacción se encuentra en proceso de validación por la institución financiera del usuario o siendo procesada por por el motor de pagos. Este es el estado intermedio de una transacción. |
| **APPROVED** | La transacción fue aprobada por la institución financiera del usuario. El dinero fue debitado correctamente de la cuenta bancaria del usuario. Este es un estado final exitoso de una transacción. |
| **REJECTED** | La transacción fue denegada por la institución financiera del usuario. El dinero no fue debitado de la cuenta bancaria del usuario. Este es un estado final no exitoso de una transacción. |
| **FAILED** | La transacción falló debido a causas no asociadas a la institución financiera o al motor de pagos, tales como un fallo del servidor o un error de procesamiento. El dinero no fue debitado de la cuenta bancaria del usuario. Este es un estado final no exitoso de una transacción. |
| **PARTIALLY\_REFUNDED** | Se han realizado uno o más reembolsos parciales al usuario sobre la transacción y estos han sido exitosos. Este es un estado final exitoso de una transacción. |
| **REFUNDED** | Se ha realizado el reembolso al usuario del total de la transacción de manera exitosa. Este es un estado final exitoso de una transacción. |

## **Roles en el sistema**

| Rol | Descripción y alcance de acceso |
| :---- | :---- |
| **PLATFORM\_ADMIN** | Administrador de la plataforma de pagos. Tiene acceso total a todos los comercios registrados en la plataforma, sus configuraciones, así como herramientas de monitoreo y gestión de usuarios no directamente asociados a un comercio en particular. |
| **MERCHANT\_OWNER** | Propietario de un comercio registrado en la plataforma de pagos. Acceso completo a su organización, incluyendo la gestión de usuarios de su equipo. |
| **MERCHANT\_ADMIN** | Administrador delegado del comercio registrado en la plataforma. Puede gestionar la operación del comercio sin poder transferir la propiedad de la cuenta. |
| **DEVELOPER** | Miembro técnico del equipo del comercio. Acceso a credenciales de la API, creación de pagos y consultas técnicas. |
| **ANALYST** | Miembro del equipo financiero o contable. Acceso de solo lectura a reportes y descarga de datos para conciliación. |

