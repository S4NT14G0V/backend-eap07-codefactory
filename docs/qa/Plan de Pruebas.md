# **Plataforma de APIs de Pagos Embebidos tipo Stripe — Caso 8**

**Plan de Pruebas**

Equipo Avanzado Presencial \#07 — CodeF@ctory — Universidad de Antioquia

# **Versión 1.0  ·  Sprints 1 – 3  ·  Plan de PruebasIntroducción**

### **Resumen**

El presente Plan de Pruebas describe el enfoque, alcance, estrategias y recursos necesarios para verificar y validar la calidad del sistema Motor de Pagos Embebidos (Caso 8), desarrollado por el Equipo Avanzado Presencial \#07 en el marco del programa CodeF@ctory de la Universidad de Antioquia.

El sistema busca ofrecer una plataforma de APIs tipo Stripe que permita a comercios externos procesar pagos de forma segura, gestionar credenciales de acceso, consultar el estado de sus transacciones y recibir notificaciones automáticas sobre el resultado de cada cobro.

Este documento cubre las actividades de prueba para los tres sprints del proyecto, abarcando un total de 23 historias de usuario distribuidas en 6 épicas, desde el registro de comercios hasta la exportación de datos para conciliación financiera.

### **Propósito**

El propósito de este Plan de Pruebas es:

- Definir qué se va a probar, cómo, cuándo y por quién, en cada sprint del proyecto.  
- Establecer los criterios de calidad que debe cumplir el sistema para ser considerado apto para su entrega.  
- Servir como guía de trabajo para el equipo de calidad durante la ejecución de las pruebas funcionales, de seguridad, de integración y automatizadas.  
- Garantizar el cumplimiento de los criterios mínimos exigidos por CodeF@ctory, incluyendo cobertura de pruebas unitarias ≥ 40%, cero vulnerabilidades críticas, deuda técnica máxima de 2 días y complejidad ciclomática menor a 50\.  
- Proveer evidencia documentada del proceso de aseguramiento de calidad para cada entrega académica.

### **Alcance general**

Este plan aplica al backend del sistema desarrollado en Spring Boot, con integración a base de datos relacional (PostgreSQL/MySQL), caché en Redis y un microservicio independiente de Sandbox. Cubre las pruebas desde el Sprint 1 hasta el Sprint 3, incluyendo pruebas unitarias, de integración, de aceptación automatizada (BDD/Gherkin), análisis estático de código y validación de seguridad básica con controles OWASP.

## **Elementos de prueba / Alcance**

### **Resumen**

Esta sección delimita con precisión qué módulos y funcionalidades del sistema serán cubiertos por las actividades de prueba, y cuáles quedan explícitamente fuera del alcance. La delimitación se basa en las épicas, features e historias de usuario definidas en el backlog del proyecto, distribuidas en los tres sprints.

### **Que se va a testear**

Épica 1 — Gestión de comercios y onboarding (EP01)

- Registro de nuevo comercio (HU001)  
- Consulta del perfil y datos del comercio (HU007)  
- Actualización de datos de perfil y configuración bancaria (HU008)  
- Gestión de usuarios y roles del equipo del comercio (HU016)  
- Aprobación y suspensión de comercios desde el portal administrativo (HU023)

Épica 2 — Seguridad y acceso a APIs (EP02)

- Generación de credenciales de acceso a la API (HU002)  
- Autenticación con doble factor — MFA (HU005)  
- Validación de credenciales por solicitud (HU006)  
- Separación de entornos de pruebas y producción (HU009)  
- Revocación inmediata de credenciales comprometidas (HU010)  
- Rotación de credenciales sin interrupción del servicio (HU011)

Épica 3 — Procesamiento central de pagos (EP03)

- Creación de una transacción (HU003)  
- Asignación del estado inicial de un pago (HU004)  
- Procesamiento del resultado del pago: aprobación y rechazo (HU012)  
- Configuración de canales de notificación automática — webhooks (HU013)  
- Consulta del listado de pagos con filtros (HU015)  
- Solicitud de reembolso total (HU017)  
- Solicitud de reembolso parcial (HU018)

Épica 4 — Trazabilidad y auditoría (EP04)

- Registro automático e inmutable de eventos de auditoría (HU014)  
- Consulta del historial completo de eventos de un pago (HU019)

Épica 5 — Reporting y analítica (EP05)

- Reporte de volumen de transacciones por período (HU020)  
- Dashboard de distribución de pagos por estado (HU021)  
- Exportación de transacciones a archivo descargable (HU022)

Módulo transversal — Seguridad y observabilidad

- Validación de controles OWASP básicos: inyección, manejo de secretos, validación de entrada  
- Logs estructurados con correlationID  
- Rate limiting por endpoint  
- RBAC por endpoint y reglas ABAC básicas  
- Registro de eventos de seguridad: intentos fallidos de login, cambios críticos, transiciones de estado

Entorno Sandbox (EP06)

- Simulación de transacciones mediante el microservicio Mock PSP  
- Separación efectiva de datos entre entorno sandbox y producción  
- Ejecución de escenarios predefinidos: éxito, rechazo por fondos insuficientes, tarjeta vencida, timeout

### **Que NO se va a testear**

Los siguientes elementos quedan explícitamente fuera del alcance de este plan de pruebas:

- **Interfaces de usuario (frontend):** El proyecto es un desarrollo de backend. El dashboard administrativo y el checkout demo son herramientas de demostración, no objetos de prueba formales.  
- **Integraciones con procesadores de pago reales (PSP externos):** El sistema utiliza un Mock PSP para pruebas. No se realizarán pruebas contra redes financieras reales como Visa, Mastercard o pasarelas de pago en producción.  
- **Pruebas de carga y estrés a gran escala:** No se realizarán pruebas de rendimiento bajo volúmenes masivos de transacciones concurrentes, dado que el alcance académico del proyecto no contempla infraestructura de pruebas de carga avanzada.  
- **Pruebas de compatibilidad entre navegadores:** Al ser un sistema de backend con APIs REST, no aplica la validación de comportamiento en diferentes navegadores.  
- **Pruebas de recuperación ante desastres (Disaster Recovery):** No se evaluarán escenarios de fallo total de infraestructura ni procedimientos de recuperación de datos.  
- **Vaultización de datos de tarjeta:** Las notas técnicas mencionan que los tokens de método de pago deben ser previamente tokenizados por un vault externo. La integración con dicho vault no es objeto de prueba.  
- **Historia de usuario pendiente:** La HU009 sobre separación de entornos listada como "PENDIENTE" en el backlog original será incluida en pruebas solo una vez que sea formalmente incorporada al sprint correspondiente.

## **Objetivos de las pruebas**

### **Resumen**

Esta sección define las metas concretas que se buscan alcanzar con la ejecución del plan de pruebas. Los objetivos están alineados con los criterios mínimos de calidad exigidos por CodeF@ctory, las historias de usuario definidas en el backlog y los requisitos de seguridad establecidos en el informe arquitectónico del proyecto.

### **Objetivos Generales**

***OBJ-01 — Validar el comportamiento funcional del sistema***

Verificar que cada historia de usuario implementada cumple con sus criterios de aceptación definidos en lenguaje Gherkin, cubriendo tanto los escenarios de caso feliz como los escenarios de excepción documentados en el backlog.

***OBJ-02 — Detectar defectos de forma temprana***

Identificar errores en la lógica de negocio, en las transiciones de la máquina de estados, en la validación de entradas y en el manejo de errores durante cada sprint, antes de que escalen a etapas posteriores del desarrollo.

***OBJ-03 — Verificar la integridad de la máquina de estados de pagos***

Garantizar que las transiciones entre estados de una transacción *(CREATED → PROCESSING → APPROVED | REJECTED | FAILED | REFUNDED)* se comporten de forma estricta y que ninguna transición inválida sea permitida por el sistema.

***OBJ-04 — Validar los mecanismos de seguridad***

Comprobar que los controles de autenticación (MFA, JWT), autorización (RBAC/ABAC), gestión de credenciales (generación, rotación, revocación) y protecciones OWASP básicas funcionan correctamente y no presentan vulnerabilidades críticas.

***OBJ-05 — Garantizar la trazabilidad e inmutabilidad de la auditoría***

Verificar que cada evento significativo del ciclo de vida de una transacción queda registrado de forma automática, completa e inmutable en la bitácora de auditoría, incluyendo la cadena de integridad por hash encadenado.

***OBJ-06 — Confirmar el correcto funcionamiento del motor de idempotencia***

Comprobar que el sistema no genera cobros duplicados ante solicitudes repetidas con la misma clave de operación (Idempotency-Key), tanto en el flujo transaccional como en el proceso de reembolsos.

***OBJ-07 — Validar la separación efectiva de entornos***

Asegurar que las credenciales del entorno sandbox no pueden operar en producción, que los datos de ambos entornos están completamente segregados y que el Mock PSP responde correctamente en el entorno de pruebas.

***OBJ-08 — Cumplir los Quality Gates definidos por CodeF@ctory***

Alcanzar y mantener durante los tres sprints los siguientes umbrales de calidad medidos mediante SonarCloud:

| *Quality Gate* | *Umbral Requerido* |
| :---- | :---- |
| Cobertura de pruebas unitarias | \> 40% |
| Deuda Técnica | \< 2 días |
| Complejidad ciclomática | \< 50 |
| Severidad de Issues | Minor or Better |
| Vulnerabilidades críticas | 0 |

***OBJ-09 — Automatizar la validación de criterios de aceptación***

Implementar pruebas automatizadas de aceptación a nivel de backend basadas en los escenarios Gherkin de cada historia de usuario, de modo que puedan ejecutarse de forma repetible en cada sprint como parte del pipeline de CI/CD.

***OBJ-10 — Verificar la consistencia de las APIs REST***

Validar que todos los endpoints expuestos cumplen con el modelo HATEOAS, devuelven los códigos HTTP correctos, aplican el esquema uniforme de errores (errorCode, message, details, traceId) y están correctamente documentados mediante OpenAPI/Swagger.

## **Estrategias de las pruebas**

### **Resumen**

Esta sección define el enfoque metodológico que el equipo de calidad aplicará durante los tres sprints del proyecto. Se combinan técnicas de caja negra y caja blanca, pruebas manuales y automatizadas, cubriendo los aspectos funcionales, de seguridad y de regresión del sistema. La estrategia está diseñada para integrarse con el pipeline de CI/CD configurado en GitHub Actions y con el análisis estático continuo en SonarCloud.

### **Funcionales**

**Pruebas Unitarias**

- **Enfoque:** Caja blanca  
- **Ejecución:** Automatizada  
- **Herramienta:** JUnit 5 \+ Mockito  
- **Objetivo:** Verificar el comportamiento aislado de componentes individuales: lógica de la máquina de estados, motor de idempotencia, cálculo de reembolsos parciales, validación de reglas de negocio por dominio y generación de credenciales.  
- **Cobertura mínima requerida:** 40% medida con JaCoCo e integrada a SonarCloud.  
- **Patrón de diseño:** AAA (Arrange – Act – Assert) en todos los casos.  
- **Alcance por módulo:**

| Módulo | Componentes a cubrir |
| :---- | :---- |
| Identidad y Comercios | Registro, validación de campos, cambio de estado del comercio |
| Transacciones | Máquina de estados, idempotencia, cálculo de reembolsos |
| Seguridad | Validación de credenciales, rotación, revocación, MFA |
| Auditoría | Generación de eventos, encadenamiento de hashes |
| Reportes | Cálculo de tasa de aprobación, agrupación por período |


**Pruebas de Integración**

- **Enfoque:** Caja negra / Caja gris  
- **Ejecución:** Automatizada  
- **Herramienta:** Spring Boot Test \+ Testcontainers (base de datos real en contenedor)  
- **Objetivo:** Verificar la interacción correcta entre las capas del sistema: controladores REST, servicios de negocio, repositorios y base de datos. Incluye la validación de procedimientos almacenados, triggers de auditoría y consultas no triviales con joins y agregaciones.  
- **Escenarios clave a integrar:**

| Escenarios | Componentes involucrados |
| :---- | :---- |
| Registro de comercio → generación de credenciales | Módulo de comercios \+ módulo de seguridad \+ BD |
| Creación de pago → cambio de estado → registro de auditoría | Motor de transacciones \+ auditoría \+ Redis |
| Revocación de credencial → invalidación de caché | Módulo de seguridad \+ Redis |
| Reembolso parcial acumulado → transición a REFUNDED | Motor de transacciones \+ BD |
| Webhook fallido → reintento con backoff exponencial | Motor de notificaciones \+ módulo de webhooks |

**Pruebas de Sistema**

- **Enfoque:** Caja negra  
- **Ejecución:** Manual y automatizada  
- **Herramienta:** Postman (colecciones por épica) \+ Newman para ejecución en CI/CD  
- **Objetivo:** Validar el comportamiento del sistema completo desplegado en el entorno de pruebas, ejecutando flujos de extremo a extremo que atraviesan múltiples módulos. Se verifican los códigos HTTP, la estructura de respuestas HATEOAS, el esquema uniforme de errores y el comportamiento del rate limiting.  
- **Flujos E2E principales:**

| ID Flujo | Descripción |
| :---- | :---- |
| FL-01 | Registro → aprobación → credenciales → primer pago → estado PROCESSING |
| FL-02 | Pago aprobado → webhook → registro en auditoría → consulta de historial |
| FL-03 | Pago aprobado → reembolso parcial × 2 → transición automática a REFUNDED |
| FL-04 | Credencial comprometida → revocación → intento de pago rechazado |
| FL-05 | Login con MFA → intento fallido × 5 → bloqueo de cuenta → notificación |
| FL-06 | Credencial sandbox → intento en producción → rechazo del sistema |

**Pruebas de Aceptación (BDD)**

- **Enfoque:** Caja negra  
- **Ejecución:** Automatizada  
- **Herramienta:** Cucumber \+ JUnit 5  
- **Objetivo:** Automatizar los escenarios escritos en lenguaje Gherkin definidos en el backlog de historias de usuario. Cada escenario del documento de HU tiene correspondencia directa con un archivo .feature ejecutable. Estas pruebas validan el sistema desde la perspectiva del negocio, sin conocimiento de la implementación interna.  
- **Criterio de cobertura:** Todos los escenarios de caso feliz de las 23 HU deben estar automatizados al cierre del Sprint 3\. Los escenarios de excepción críticos (credenciales inválidas, transiciones de estado no válidas, montos inválidos) deben estar cubiertos desde el Sprint 2\.

### **No Funcionales**

**Pruebas de Rendimiento**

- **Enfoque:** Caja negra  
- **Ejecución:** Automatizada  
- **Herramienta:** K6 o JMeter  
- **Objetivo:** Verificar que el sistema cumple con los umbrales de respuesta establecidos en las notas técnicas de las historias de usuario.  
- **Umbrales a validar:**

| Endpoint | Umbral definido |
| :---- | :---- |
| Creación de transacción (POST /payments) | \< 300ms en percentil 99 bajo carga normal |
| Validación de credenciales (caché Redis) | \< 5ms en caché activa |
| Revocación de credencial (invalidación Redis) | \< 5 segundos efectivos |
| Respuesta del Mock PSP en sandbox | \< 100ms |

**Pruebas de Seguridad**

- **Enfoque:** Caja negra / Caja blanca  
- **Ejecución:** Manual y automatizada  
- **Herramientas:** OWASP ZAP (análisis dinámico), SonarCloud (análisis estático), revisión manual de configuración  
- **Objetivo:** Verificar los controles de seguridad implementados en el sistema, alineados con los requisitos del criterio 5 de CodeF@ctory.  
- **Controles a validar:**

| Control | Mecanismo de verificación |
| :---- | :---- |
| Autenticación MFA (TOTP RFC 6238\) | Casos de prueba con código válido, expirado e inválido |
| JWT RS256 — expiración y blacklist | Prueba con token expirado y token revocado en Redis |
| RBAC por endpoint | Matriz de roles vs. endpoints con intentos de acceso no autorizado |
| Protección contra replay attacks | Reenvío de solicitudes con Idempotency-Key vencida |
| Rate limiting | Superación del límite configurado por IP y por credencial |
| Inyección SQL / validación de entrada | Payloads maliciosos en campos de texto y parámetros de URL |
| Datos bancarios enmascarados | Verificación de respuesta en consulta de perfil y exportación |
| Secretos no expuestos en logs | Revisión de logs estructurados ante operaciones sensibles |

**Pruebas de Usabilidad de la API**

- **Enfoque:** Caja negra  
- **Ejecución:** Manual  
- **Objetivo:** Verificar que la API es consistente, predecible y fácil de consumir para un desarrollador externo, validando la documentación Swagger, los mensajes de error descriptivos con traceId y la presencia de links HATEOAS en las respuestas.

### **Pruebas de Regresión**

- **Enfoque:** Caja negra  
- **Ejecución:** Automatizada  
- **Herramienta:** Suite de pruebas unitarias \+ colecciones Postman/Newman ejecutadas en GitHub Actions en cada Pull Request  
- **Objetivo:** Garantizar que las funcionalidades implementadas y validadas en sprints anteriores no se ven afectadas por los cambios introducidos en el sprint en curso.  
- **Estrategia:** Al inicio de cada sprint se ejecuta la suite de regresión completa del sprint anterior. Cualquier fallo bloquea el merge a la rama principal hasta ser corregido.  
- **Cobertura mínima de regresión por sprint:**

| Sprint | Suite de regresión a ejecutar |
| :---- | :---- |
| Sprint 2 | Todos los casos automatizados del sprint 1 |
| Sprint 3 | Todos los casos automatizados del sprint 1 y sprint 2 |

### **Análisis Estático de Código**

- **Herramienta:** SonarCloud integrado al repositorio GitHub  
- **Ejecución:** Automática en cada Push y Pull Request mediante GitHub Actions  
- **Quality Gates monitoreados en cada sprint:**  
  - Cobertura de pruebas unitarias ≥ 40%  
  - Deuda técnica ≤ 2 días  
  - Complejidad ciclomática \< 50  
  - Severidad de issues: Minor or Better  
  - Vulnerabilidades críticas: 0

## **Criterios de Entrada/Salida \- Criterios de Aceptación/Rechazo**

### **Resumen**

Esta sección establece las condiciones que deben cumplirse para iniciar y dar por terminadas las actividades de prueba en cada sprint. Siguiendo el estándar IEEE 829, se definen criterios de entrada (precondiciones) que garantizan que el ambiente y el objeto de prueba están listos, y criterios de salida (postcondiciones) que determinan cuándo las pruebas han sido suficientes para aprobar o rechazar una entrega.

### **Criterios de Entrada**

Los siguientes criterios deben estar satisfechos antes de iniciar la ejecución de pruebas en cualquier sprint:

**CE-01 — Código desplegado y estable:** El código correspondiente a las historias de usuario del sprint debe estar integrado en la rama principal del repositorio, desplegado en el entorno de pruebas (Render \+ Docker Compose) y accesible sin errores de arranque.

**CE-02 — Ambiente de pruebas operativo:**

- La base de datos relacional (PostgreSQL/MySQL) debe estar disponible con el esquema actualizado y los scripts de despliegue ejecutados correctamente.  
- La instancia de Redis debe estar activa y accesible para los módulos de caché e idempotencia.  
- El microservicio Sandbox Mock debe estar desplegado y respondiendo correctamente.  
- Las variables de entorno y secretos de configuración deben estar correctamente configurados en el entorno de pruebas.

**CE-03 — Historias de usuario en estado "Listo para pruebas":** Las historias de usuario a probar deben cumplir con la Definition of Ready (DoR) del equipo: criterios de aceptación en Gherkin definidos, dependencias resueltas y revisión de código completada (Pull Request aprobado).

**CE-04 — Casos de prueba documentados:** Los casos de prueba correspondientes al sprint deben estar escritos, revisados y disponibles en el repositorio o herramienta de gestión antes de iniciar la ejecución.

**CE-05 — Herramientas de prueba configuradas:**

- SonarCloud debe estar integrado al repositorio y con los Quality Gates configurados.  
- Las colecciones de Postman deben estar actualizadas con los endpoints del sprint.  
- El pipeline de CI/CD en GitHub Actions debe estar activo y ejecutando el análisis estático en cada Push.

**CE-06 — Datos de prueba disponibles:** Deben existir datos de prueba predefinidos en el entorno: al menos un comercio registrado y activo, credenciales válidas generadas para sandbox y producción, y transacciones en diferentes estados para los módulos que lo requieran.

### **Criterios de Salida**

***Criterios de salida para aprobar una entrega (Definition of Done de pruebas)***

Los siguientes criterios deben cumplirse para considerar que las pruebas de un sprint han sido completadas satisfactoriamente:

**CS-01 — Cobertura de casos de prueba:** El 100% de los escenarios de caso feliz de las HU del sprint han sido ejecutados. El 100% de los escenarios de excepción críticos (prioridad Alta y Crítica) han sido ejecutados.

**CS-02 — Tasa de defectos críticos:** No existen defectos abiertos con severidad Crítica o Alta sin resolver al momento del cierre del sprint.

**CS-03 — Quality Gates de SonarCloud aprobados:**

| *Métrica* | *Umbral Requerido* | *Estado requerido* |
| :---- | :---- | :---- |
| Cobertura de pruebas unitarias | \> 40% | Aprobado |
| Deuda Técnica | \< 2 días | Aprobado |
| Complejidad ciclomática | \< 50 | Aprobado |
| Severidad de Issues | Minor or Better | Aprobado |
| Vulnerabilidades críticas | 0 | Aprobado |

**CS-04 — Pruebas de regresión aprobadas:** La suite de regresión del sprint anterior se ejecutó completamente y no presenta fallos sin justificar.

**CS-05 — Escenarios Gherkin automatizados:** Los escenarios de aceptación BDD del sprint fueron implementados en Cucumber y se ejecutan correctamente dentro del pipeline de CI/CD.

**CS-06 — Documentación de resultados:** El informe de ejecución de pruebas del sprint está completo, incluyendo: casos ejecutados, resultado por caso (pasó/falló), defectos encontrados y estado de cierre.

***Criterios de salida para rechazar una entrega***

Una entrega será rechazada y devuelta al equipo de desarrollo si se presenta alguna de las siguientes condiciones:

**CR-01 —** Existen uno o más defectos de severidad crítica abiertos al cierre del sprint.

**CR-02 —** El Quality Gate de SonarCloud reporta vulnerabilidades críticas (score \= 0 tolerancia).

**CR-03 —** La cobertura de pruebas unitarias es inferior al 40% al momento de la entrega.

**CR-04 —** Uno o más flujos E2E definidos en la sección 4.2.1.c fallan de forma consistente y no tienen plan de corrección documentado.

**CR-05 —** El ambiente de pruebas no estuvo disponible por más del 30% del tiempo planificado para ejecución, lo que impidió completar la cobertura mínima requerida.

**CR-06 —** Las pruebas de regresión revelan que funcionalidades aprobadas en sprints anteriores presentan fallos no corregidos.

### **Criterios de suspensión y reanudación**

En caso de que las pruebas deban suspenderse antes de completarse, se aplicarán las siguientes condiciones:

| Condición de suspensión | Condición de reanudación |
| :---- | :---- |
| El ambiente de pruebas no está disponible | El ambiente es restaurado y verificado por el equipo de calidad |
| Se detecta un defecto bloqueante que impide continuar la ejecución | El defecto es corregido, desplegado y verificado en el entorno de pruebas |
| El código base presenta errores de compilación o arranque | El equipo de desarrollo entrega una versión estable y desplegable |
| Cambios no planificados en los requisitos de una HU en curso | Los criterios de aceptación son actualizados y aprobados por el equipo |

## **Recursos \- Entorno de Pruebas (Test Bed)**

### **Resumen**

Esta sección describe los recursos humanos, tecnológicos y de infraestructura necesarios para ejecutar las actividades de prueba definidas en este plan. Se especifican los roles y responsabilidades del equipo de calidad, las características del ambiente de pruebas y el conjunto de herramientas que se utilizarán durante los tres sprints del proyecto.

### **Humanos**

El equipo de calidad del proyecto está conformado por los siguientes integrantes:

| Nombre | Rol | Responsabilidades en pruebas |
| :---- | :---- | :---- |
| Miguel Ángel Castiblanco Flórez | QA | Diseño y ejecución de casos de prueba funcionales, automatización de escenarios Gherkin con Cucumber, reporte y seguimiento de defectos |
| Juan Diego Portillo Parada | QA | Configuración del pipeline de pruebas en GitHub Actions, ejecución de pruebas de integración con Testcontainers, mantenimiento de colecciones Postman |
| Samuel Puerta Patiño | QA | Coordinación del equipo de calidad, elaboración del plan de pruebas, generación de informes de ejecución por sprint, seguimiento de métricas de calidad en Azure DevOps |
| Alejandro Chavarría Mora | QA | Diseño y ejecución de casos de prueba de seguridad, configuración de SonarCloud y Quality Gates, validación de controles OWASP |

### **Ambiente**

El ambiente de pruebas debe replicar en la medida de lo posible las condiciones del entorno productivo, garantizando la confiabilidad de los resultados obtenidos.

**Infraestructura de despliegue**

| *Componente* | *Especificación* |
| :---- | :---- |
| Proveedor de nube | Render |
| Método de despliegue | Docker Compose (backend \+ base de datos \+ redis) |
| CI/CD | GitHub Actions |
| Repositorio de código | GitHub |
| Entorno de pruebas | Instancia separada del entorno productivo, con datos de prueba propios |

**Componentes del sistema en el ambiente de pruebas**

| Componente | Tecnología | Propósito en pruebas |
| :---- | :---- | :---- |
| Backend principal | Spring Boot (java) | Objetivo de prueba principal: APIs REST del monolito modular |
| Base de datos relacional | PostgreSQL / MySQL | Pruebas de integración contra BD real, validación de procedimientos almacenados y triggers de auditoría |
| Caché | Redis | Validación de idempotencia, blacklist de tokens, invalidación de credenciales y caché de validación |
| Microservicio Sandbox Mock | Spring Boot (Servicio independiente) | Simulación del procesador de pagos externo (PSP) en pruebas de entorno sandbox |
| Documentación de API | Swagger / OpenAPI | Referencia para construccion de casos de prueba y validación de contratos de API |

**Datos de prueba**

| *Conjunto de datos* | *Descripción* |
| :---- | :---- |
| Comercio de prueba | Al menos 3 comercios en diferentes estados: PENDING\_VERIFICATION, ACCEPTED, SUSPENDED |
| Credenciales de prueba | Juegos de credenciales válidas, revocadas, vencidas y con diferentes scopes (payments:write, payments:read, reports:read) para sandbox y producción |
| Transacciones de prueba | Transacciones en cada estado del ciclo de vida: CREATED, PROCESSING, APPROVED, REJECTED, FAILED, PARTIALLY\_REFUNDED, REFUNDED |
| Usuarios administrativos | Al menos un PLATFORM\_ADMIN con MFA configurado y uno sin MFA configurado |
| Tarjetas de Simulación | Tarjeta terminada en 4242 (pago exitoso), terminada en 0051 (fondos insuficientes), escenario de timeout configurado |

### **Herramientas**

**Herramientas de desarrollo y ejecución de pruebas**

| *Herramienta* | *Versión recomendada* | *Propósito* |
| :---- | :---- | :---- |
| JUnit 5 | 5.x | Framework de pruebas unitarias para el backend en Spring Boot |
| Mockito | 5.x | Creación de mocks y stubs para pruebas unitarias aisladas |
| Testcontainers | 1.19.x | Levantamiento de contenedores de BD y Redis para pruebas de integración |
| Cucumber | 7.x | Automatización de pruebas de aceptación BDD a partir de escenarios Gherkin |
| JaCoCo | 0.8.x | Medición de coberturas de pruebas unitarias integrada a SonarCloud |

**Herramientas de pruebas de API y rendimiento**

| *Herramienta* | *Propósito* |
| :---- | :---- |
| Postman | Ejecución manual de pruebas de sistema sobre los endpoints REST, organizada por colecciones por épica |
| NewMan | Ejecución automatizadas de colecciones Postman en el pipeline de GitHub Actions |
| K6 / JMeter | Pruebas de rendimiento para validar umbrales de respuesta en endpoints críticos |

**Herramientas de calidad y seguridad**

| *Herramienta* | *Propósito* |
| :---- | :---- |
| SonarCloud | Análisis estático de código, medición de Quality Gates, detección de vulnerabilidades y deuda técnica |
| OWASP ZAP | Análisis dinámico de seguridad sobre los endpoints expuestos |
| GitHub Actions | Automatización del pipeline CI/CD: ejecución de pruebas unitarias, análisis estático y pruebas de regresión en cada Push y Pull Request |

**Herramientas de gestión y reporte**

| *Herramienta* | *Propósito* |
| :---- | :---- |
| Azure DevOps | Gestión del backlog, seguimiento de defectos, tablero de métricas ágiles y burndown chart por sprint |
| GitHub | Control de versiones, revisión de código (Pull Request) y trazabilidad entre commits y casos de prueba |

## **Cronograma**

### **Resumen**

Esta sección presenta la distribución temporal de las actividades de prueba a lo largo de los tres sprints del proyecto. El cronograma está alineado con los entregables académicos definidos por CodeF@ctory para el curso de Calidad de Software, y establece los hitos principales, las actividades por fase y las dependencias entre ellas.

***Nota:*** Las fechas específicas de cada sprint deben ser ajustadas por el equipo según el calendario académico oficial del semestre. El cronograma presentado a continuación refleja la estructura y duración relativa de cada fase dentro de un sprint de duración estándar.

### **Estructura general por sprint**

Cada sprint sigue la misma estructura interna de actividades de prueba, dividida en cuatro fases:

### **Cronograma por sprint**

***Sprint 1 — Habilitación del flujo mínimo viable de pagos***

***HU cubiertas:*** HU001, HU002, HU003, HU004, HU005, HU006

***Story Points:*** 47 SP

***Objetivo de pruebas:*** Validar el flujo completo de registro → credenciales → primer pago → estado inicial asignado.

| Semana | Actividad | Responsable | Entregable |
| :---- | :---- | :---- | :---- |
| S1 \- Días 1-3 | Revisión de criterios de aceptación de HU001–HU006. Verificación del ambiente de pruebas. Configuración de SonarCloud y Quality Gates | Equipo de Calidad \+ Líder de Calidad | Ambiente verificado, Quality Gates configurados |
| S1 \- Días 3-6  | Diseño de casos de prueba para HU001–HU006 (caso feliz \+ excepciones). Escritura de escenarios Gherkin en archivos *.feature* | Equipo de Calidad | Casos de prueba documentados, archivos *.feature* creados |
| S1 \- Días 6-10 | Ejecución de pruebas unitarias (JUnit 5 \+ Mockito). Ejecución de pruebas de integración con Testcontainers. Ejecución manual con Postman de flujos E2E FL-01 y FL-04 | Equipo de Calidad | Resultados de pruebas unitarias e integración, reporte de ejecución Postman |
| S1 \- Días 10-12 | Ejecución de análisis estático en SonarCloud. Validación de complejidad ciclomática y deuda técnica. Pruebas de seguridad básicas: MFA, JWT, rate limiting | Equipo de Calidad | Informe SonarCloud Sprint 1, reporte de pruebas de seguridad |
| S1 \- Días 12-14 | Reporte de defectos encontrados en Azure DevOps. Verificación de correcciones. Generación del informe de ejecución Sprint 1 | Lider de Calidad | Informe de ejecución Sprint 1, defectos registrados en Azure DevOps |

***Hitos Sprint 1:***

| Hito | Condición de cumplimiento |
| :---- | :---- |
| HIT-1.1 | Ambiente de pruebas operativo y verificado |
| HIT-1.2 | Quality Gates de SonarCloud configurados y activos |
| HIT-1.3 | Casos de prueba de HU001 \- HU006 documentados |
| HIT-1.4 | Flujo E2E FL-01 ejecutado y aprobado |
| HIT-1.5 | Informe de ejecución Sprint 1 entregado |

***Sprint 2 — Seguridad avanzada, ciclo de vida del pago y trazabilidad***

***HU cubiertas:*** HU007, HU008, HU009, HU0010, HU0011, HU0012, HU0013, HU0014, HU0015

***Story Points:*** 50 SP

***Objetivo de pruebas:*** Validar el ciclo completo del pago con resultados reales, webhooks, gestión avanzada de credenciales y bitácora de auditoría inmutable.

| Semana | Actividad | Responsable | Entregable |
| :---- | :---- | :---- | :---- |
| S2 \- Día 1-3 | Ejecución de suite de regresión Sprint 1\. Revisión de criterios de aceptación HU007–HU015. Actualización del ambiente con nuevos scripts de BD | Lider de Calidad | Reporte de regresión Sprint 1, ambiente actualizado |
| S2 \- Día 3-6 | Diseño de casos de prueba HU007–HU015. Actualización de escenarios Gherkin. Actualización de colecciones Postman con nuevos endpoints | Equipo de Calidad | Casos de prueba Sprint 2 documentados, colecciones Postman actualizadas |
| S2 \- Día 6-10 | Ejecución de pruebas unitarias nuevas. Pruebas de integración: webhooks, auditoría inmutable, rotación y revocación de credenciales. Ejecución de flujos E2E FL-02, FL-03, FL-05 y FL-06 | Equipo de Calidad | Resultados de pruebas unitarias e integración Sprint 2 |
| S2 \- Día 10-12 | Pruebas de seguridad avanzadas: RBAC por endpoint, ABAC, protección contra replay, validación de datos bancarios enmascarados. Análisis estático SonarCloud Sprint 2 | Equipo de Calidad | Informe de pruebas de seguridad Sprint 2, informe SonarCloud Sprint 2 |
| S2 \- Días 12-14 | Automatización de escenarios Gherkin críticos de Sprint 1 y Sprint 2 en Cucumber. Reporte y seguimiento de defectos. Informe de ejecución Sprint 2 | Lider de Calidad \+ Equipo de Calidad | Suite Cucumber Sprint 1–2 automatizada, informe de ejecución Sprint 2 |

***Hitos Sprint 1:***

| Hito | Condición de cumplimiento |
| :---- | :---- |
| HIT-2.1 | Suite de regresión Sprint 1 ejecutada sin fallos bloqueantes |
| HIT-2.2 | Flujos E2E FL-02 y FL-03 ejecutados y aprobados |
| HIT-2.3 | Bitácora de auditoría: inmutabilidad y encadenamiento de hashes verificados |
| HIT-2.4 | Escenarios Gherkin de Sprint 1 y Sprint 2 automatizados en Cucumber |
| HIT-2.5 | Informe de ejecución Sprint 2 entregado |

***Sprint 3 — Cierre financiero, visibilidad de negocio y administración***

***HU cubiertas:*** HU016, HU017, HU018, HU019, HU020, HU021, HU022, HU023

***Story Points:*** 47 SP

***Objetivo de pruebas:*** Validar reembolsos, reportes de analítica, exportación de datos, gestión de usuarios y portal administrativo. Completar la automatización total de pruebas de aceptación.

| Semana | Actividad | Responsable | Entregable |
| :---- | :---- | :---- | :---- |
| S3 \- Días 1-3 | Ejecución de suite de regresión Sprint 1 y Sprint 2\. Revisión de criterios de aceptación HU016–HU023. Actualización del ambiente | Líder de Calidad \+ Equipo de Calidad | Reporte de regresión Sprint 1–2, ambiente actualizado |
| S3 \- Días 3-6 | Diseño de casos de prueba HU016–HU023. Escenarios Gherkin de reembolsos, reportes y administración. Preparación de datos de prueba para conciliación y exportación | Equipo de Calidad | Casos de prueba Sprint 3 documentados |
| S3 \- Días 6-10 | Ejecución de pruebas unitarias y de integración Sprint 3\. Pruebas de flujo E2E: reembolso parcial acumulado, exportación asíncrona, aprobación y suspensión de comercios | Equipo de Calidad | Resultados de pruebas Sprint 3 |
| S3 \- Días 10-12 | Pruebas de rendimiento con K6/JMeter sobre endpoints críticos. Análisis estático SonarCloud Sprint 3\. Validación final de Quality Gates | Equipo de Calidad | Informe de rendimiento, informe SonarCloud Sprint 3 |
| S3 \- Días 12-14 | Automatización completa de escenarios Gherkin Sprint 3 en Cucumber. Ejecución final de la suite E2E completa. Generación del informe final de calidad del proyecto | Lider de Calidad \+ Equipo de Calidad | Suite Cucumber completa, informe final de calidad |

***Hitos Sprint 1:***

| Hito | Condición de cumplimiento |
| :---- | :---- |
| HIT-3.1 | Suite de regresión Sprint 1 y Sprint 2 ejecutada sin fallos bloqueantes |
| HIT-3.2 | Flujo E2E de reembolso parcial acumulado → REFUNDED ejecutado y aprobado |
| HIT-3.3 | Exportación asíncrona validada con enlace pre-signed URL funcional |
| HIT-3.4 | Quality Gates de SonarCloud aprobados en estado final del proyecto |
| HIT-3.5 | Suite Cucumber con los 23 escenarios de caso feliz automatizados |
| HIT-3.6 | Informe final de calidad del proyecto entregado |

## **Gestión de defectos**

### **Resumen**

Esta sección define el proceso completo mediante el cual los defectos encontrados durante la ejecución de pruebas serán reportados, clasificados, priorizados, asignados y cerrados. El proceso se gestiona en Azure DevOps, garantizando trazabilidad entre cada defecto y la historia de usuario, caso de prueba y sprint al que pertenece.

### **Clasificación de defectos**

***Por severidad***

La severidad indica el impacto técnico del defecto sobre el sistema, independientemente de su urgencia de negocio:

| *Nivel* | *Nombre* | *Descripción* | *Ejemplos* |
| :---- | :---- | :---- | :---- |
| SEV-1 | Crítico | El defecto impide la ejecución de un flujo completo del sistema o causa pérdida de datos | Pago creado sin registro en auditoría, credencial revocada que sigue autenticando, transición de estado inválida no bloqueada |
| SEV-2 | Alto | El defecto afecta una funcionalidad principal pero existe una alternativa temporal | Webhook no reintenta tras fallo, reembolso parcial no actualiza el monto disponible correctamente |
| SEV-3 | Medio | El defecto afecta una funcionalidad secundaria o produce resultados incorrectos en casos específicos | Filtro de fechas en listado de pagos no aplica correctamente, mensaje de error no incluye traceId |
| SEV-4 | Bajo | El defecto es cosmético, de documentación o no afecta la funcionalidad | Descripción incorrecta en Swagger, formato de fecha inconsistente en respuesta JSON |

***Por prioridad***

La prioridad indica la urgencia con la que el defecto debe ser corregido, definida en conjunto por el líder de calidad y el equipo de desarrollo:

| *Nivel* | *Nombre* | *Tiempo máximo de resolución* | *Condición* |
| :---- | :---- | :---- | :---- |
| P1 | Inmediata | Mismo día | Defecto bloqueante que impide continuar la ejecución de pruebas |
| P2 | Alta | Dentro del sprint en curso | Defecto que afecta una HU de prioridad Crítica o Alta |
| P3 | Media | Puede resolverse en el siguiente sprint | Defecto que afecta una HU de prioridad Media |
| P4 | Baja | Backlog de deuda técnica | Defecto cosmético o de documentación sin impacto funcional |

### **Ciclo de vida de un defecto**

Cada defecto registrado en Azure DevOps seguirá el siguiente flujo de estados:

| *Estado* | *Descripción* | *Responsable* |
| :---- | :---- | :---- |
| Nuevo | El defecto fue identificado y registrado en Azure DevOps | Calidad |
| Asignado | El QA Lead revisó el defecto, lo clasificó y lo asignó al desarrollador correspondiente | Calidad |
| En corrección | El desarrollador está trabajando en la corrección del defecto | Arquitectura |
| Listo para verificar | El desarrollador corrigió el defecto y lo desplegó en el ambiente de pruebas | Arquitectura |
| Cerrado | El QA Engineer verificó la corrección y confirmó que el defecto fue resuelto correctamente | Calidad |
| Reabierto | La corrección no resolvió el defecto o introdujo un nuevo problema | Calidad |

### **Plantilla de reporte de defecto**

Cada defecto registrado en Azure DevOps debe incluir los siguientes campos obligatorios:

| *Campo* | *Descripción* | *Ejemplo* |
| :---- | :---- | :---- |
| ID | Identificador único generado por Azure DevOps | BUG-042 |
| Título | Descripción breve y clara del defecto | "Credencial revocada permite autenticar solicitud de pago" |
| HU relacionada | Historia de usuario a la que pertenece el defecto | HU010 |
| Sprint | Sprint en el que fue encontrado | Sprint 2 |
| Severidad | Nivel de impacto técnico | SEV-1 — Crítico |
| Prioridad | Urgencia de corrección | P1 — Inmediata |
| Ambiente | Entorno donde fue reproducido | Sandbox / Producción |
| Precondiciones | Estado del sistema antes de reproducir el defecto | Comercio activo con credencial en estado REVOKED |
| Pasos para reproducir | Secuencia exacta de acciones para reproducir el defecto | 1\. Revocar credencial activa. 2\. Enviar solicitud POST /payments con esa credencial. 3\. Observar respuesta |
| Resultado esperado | Comportamiento correcto según los criterios de aceptación | El sistema rechaza la solicitud con HTTP 401 e informa que las credenciales no son válidas |
| Resultado obtenido | Comportamiento real observado | El sistema responde HTTP 200 y procesa el pago |
| Evidencia | Capturas de pantalla, logs o respuestas JSON adjuntos | Log de respuesta adjunto |
| Caso de prueba relacionado | ID del caso de prueba que detectó el defecto | CP-HU010-02 |
| Asignado | Desarrollador o módulo responsable de la corrección | Módulo de Seguridad |

### **Reglas de gestión de defectos**

**RGD-01 — Registro inmediato**  
Todo defecto identificado durante la ejecución de pruebas debe ser registrado en Azure DevOps en el mismo día en que fue encontrado, sin excepción.

**RGD-02 — No se cierra sin verificación**  
Ningún defecto puede pasar al estado Cerrado sin que un miembro del equipo de calidad haya verificado la corrección en el ambiente de pruebas. El desarrollador no puede cerrar sus propios defectos.

**RGD-03 — Defectos SEV-1 son bloqueantes**  
La presencia de un defecto con severidad SEV-1 abierto bloquea la aprobación del sprint y activa automáticamente el criterio de rechazo CR-01 definido en la sección 5\.

**RGD-04 — Trazabilidad obligatoria**  
Cada defecto debe estar vinculado a al menos una historia de usuario y un caso de prueba en Azure DevOps. Los defectos sin trazabilidad no serán procesados hasta que se complete esta información.

**RGD-05 — Prueba de regresión ante corrección**  
Cada vez que un defecto sea corregido, el QA Engineer debe ejecutar el caso de prueba original que lo detectó más al menos dos casos de prueba adyacentes del mismo módulo, para verificar que la corrección no introdujo nuevos problemas.

**RGD-06 — Defectos de seguridad tienen tratamiento prioritario**  
Los defectos relacionados con vulnerabilidades de seguridad (OWASP, JWT, credenciales, auditoría) se clasifican automáticamente como P1 independientemente de su severidad, dado el contexto financiero del sistema.

### **Métricas de seguimiento de defectos**

El QA Lead reportará las siguientes métricas en el tablero de Azure DevOps al cierre de cada sprint:

| *Métrica* | *Descripción* |
| :---- | :---- |
| Total de defectos encontrados | Número total de defectos registrados en el sprint |
| Defectos por severidad | Distribución de defectos por nivel SEV-1 a SEV-4 |
| Tasa de corrección | Porcentaje de defectos cerrados sobre el total encontrado al cierre del sprint |
| Defectos reabiertos | Número de defectos que regresaron al estado Reabierto tras una corrección |
| Densidad de defectos | Número de defectos por historia de usuario o por módulo |
| Defectos escapados | Defectos encontrados en un sprint posterior al que los introdujo |

## **Riesgos y contingencias**

### **Resumen**

Esta sección identifica los riesgos que pueden afectar la ejecución del plan de pruebas durante los tres sprints del proyecto, establece su nivel de impacto y probabilidad, y define las acciones de mitigación y contingencia correspondientes. La gestión de riesgos se realiza de forma continua por el QA Lead en coordinación con el equipo de Gestión de Proyectos, y los riesgos materializados se registran como impedimentos en Azure DevOps.

### **Matriz de riesgos**

***Escala de valoración***

**Probabilidad**

| *Nivel* | *Descripción* |
| :---- | :---- |
| Alta (3) | Es probable que ocurra en el sprint actual |
| Media (2) | Podría ocurrir por no es lo más probable |
| Baja (1) | Es poco probable que ocurra |

**Impacto**

| *Nivel* | *Descripción* |
| :---- | :---- |
| Alto (3) | Afecta el cumplimiento del sprint o la entrega académica |
| Medio (2) | Afecta parcialmente la cobertura de pruebas o la calidad del reporte |
| Bajo (1) | Genera retrasos menores sin afectar la entrega |

**Nivel de riesgo \= Probabilidad x Impacto**

| *Rango* | *Clasificación* |
| :---- | :---- |
| 7-9 | Crítico |
| 4-6 | Moderado |
| 1-3 | Bajo |

***Registro de riesgos***

| *ID* | *Riesgo* | *Categoría* | *Probabilidad* | *Impacto* | *Nivel* | *Mitigación* | *Contingencia* |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| RIE-01 | Entrega tardía del código por parte del equipo de desarrollo, impidiendo iniciar la ejecución de pruebas en el tiempo planificado | Gestión | Alta (3) | Alto (3) | 9 | Establecer fecha límite de entrega de código estable al equipo de QA con al menos 3 días de anticipación al cierre del sprint. Hacer seguimiento diario en el stand-up | Reducir el alcance de pruebas al conjunto mínimo de HU críticas del sprint. Documentar las HU no probadas como deuda de calidad para el siguiente sprint |
| RIE-02 | Inestabilidad o indisponibilidad del ambiente de pruebas en Render durante la ejecución | Infraestructura | Media (2) | Alto (3) | 6 | Verificar el ambiente al inicio de cada fase de ejecución. Mantener un script de re-despliegue documentado y probado. Configurar health checks automáticos | Ejecutar pruebas unitarias y de integración con Testcontainers en local mientras se restaura el ambiente. Documentar el tiempo de indisponibilidad para justificar reducción de cobertura si aplica |
| RIE-03 | Cambios en los criterios de aceptación de una HU durante el sprint, invalidando casos de prueba ya diseñados | Requisitos | Media (2) | Alto (3) | 6 | Congelar los criterios de aceptación al inicio de cada sprint tras el Sprint Planning. Cualquier cambio debe pasar por aprobación del QA Lead antes de ser implementado | Rediseñar únicamente los casos de prueba afectados por el cambio. Registrar el impacto del cambio en el informe de ejecución del sprint |
| RIE-04 | Incumplimiento del Quality Gate de cobertura de pruebas unitarias (\< 40%) al cierre del sprint | Calidad | Media (2) | Alto (3) | 6 | Monitorear la cobertura en SonarCloud desde la primera semana del sprint. Establecer como tarea de desarrollo la escritura de pruebas unitarias en paralelo a la implementación, no al final | Priorizar la escritura de pruebas unitarias para los módulos con menor cobertura. Si no se alcanza el 40% al cierre, documentar el plan de acción para el siguiente sprint |
| RIE-05 | Disponibilidad limitada de integrantes del equipo de calidad por carga académica o compromisos externos | Equipo | Media (2) | Medio (2) | 4 | Distribuir las responsabilidades de prueba de forma equitativa desde el inicio del sprint. Documentar los casos de prueba con suficiente detalle para que cualquier miembro del equipo pueda ejecutarlos | Reasignar las tareas del integrante no disponible entre los demás miembros del equipo de QA. Priorizar la ejecución de casos de prueba críticos sobre los de prioridad media y baja |
| RIE-06 | Defectos de severidad crítica (SEV-1) encontrados en etapas avanzadas del sprint, sin tiempo suficiente para corrección y re-verificación | Calidad | Media (2) | Alto (3) | 6 | Ejecutar pruebas de humo (smoke tests) sobre los flujos E2E principales al inicio de la fase de ejecución, antes de avanzar a pruebas detalladas | Escalar el defecto inmediatamente al equipo de desarrollo con clasificación P1. Documentar el defecto como bloqueante y registrar el impacto en el informe de sprint. Evaluar si la HU afectada puede postergarse al siguiente sprint |
| RIE-07 | Complejidad técnica del motor de idempotencia con Redis dificulta la escritura de pruebas de integración reproducibles | Técnico | Baja (1) | Alto (3) | 3 | Diseñar los casos de prueba de idempotencia con Testcontainers usando una instancia limpia de Redis por prueba para garantizar aislamiento | Usar mocks de Redis para pruebas unitarias del motor de idempotencia si la integración con el contenedor real presenta problemas de estabilidad |
| RIE-08 | Dificultad para automatizar escenarios Gherkin complejos (reembolsos acumulados, webhooks con reintentos) dentro del tiempo del sprint | Automatización | Media (2) | Medio (2) | 4 | Priorizar la automatización de escenarios de caso feliz en Sprint 2 y abordar los escenarios complejos en Sprint 3\. Dividir los escenarios complejos en pasos reutilizables desde el inicio | Ejecutar los escenarios complejos de forma manual con evidencia documentada si la automatización no se completa a tiempo. Registrar como deuda de automatización |
| RIE-09 | El microservicio Sandbox Mock no emula correctamente todos los escenarios de rechazo definidos en las notas técnicas | Técnico | Baja (1) | Medio (2) | 2 | Revisar y validar las reglas estáticas del Mock PSP (tarjeta 4242, 0051, timeout) al inicio del Sprint 1 antes de usarlo como dependencia en pruebas | Definir manualmente los datos de prueba adicionales necesarios en el Mock PSP. Coordinar con el equipo de arquitectura para ampliar las reglas de simulación |
| RIE-10 | Vulnerabilidades detectadas por SonarCloud o OWASP ZAP en etapas finales del proyecto sin tiempo para corrección | Seguridad | Baja (1) | Alto (3) | 3 | Ejecutar el análisis de SonarCloud desde el Sprint 1 y revisar los reportes semanalmente. Incorporar la revisión de seguridad como parte del Definition of Done de cada HU | Priorizar la corrección de vulnerabilidades críticas sobre cualquier otra actividad. Documentar las vulnerabilidades no críticas como deuda técnica con plan de acción para el análisis SAMM de cierre |

### **Seguimiento de riesgos**

El QA Lead en coordinación con el equipo de Gestión de Proyectos realizará las siguientes actividades de seguimiento:

| *Actividad* | *Frecuencia* | *Responsable* |
| :---- | :---- | :---- |
| Revisión del estado de riesgos activos | Al inicio de cada sprint | Equipo de Calidad \+ Gestión de Proyectos |
| Actualización de la matriz de riesgos ante nuevos hallazgos | Durante la retrospectiva de cada sprint | Equipo de Calidad |
| Registro de riesgos materializados como impedimentos | Inmediatamente al materializarse | Equipo de Calidad |
| Inclusión de acciones de mitigación en el Sprint Planning | Antes de iniciar cada sprint | Equipo de Calidad \+ Gestión de Proyectos |
| Reporte de riesgos en el informe final de calidad | Al cierre del Sprint 3 | Equipo de Calidad |

## **Entregables**

### **Resumen**

Esta sección define el conjunto completo de documentos, reportes y artefactos que el equipo de calidad debe producir como evidencia del proceso de aseguramiento de calidad a lo largo de los tres sprints del proyecto. Los entregables están alineados con los criterios mínimos exigidos por CodeF@ctory para el curso de Calidad de Software y con los entregables académicos definidos por sprint.

### **Entregables por sprint**

***Sprint 1***

| *ID* | *Entregable* | *Descripción* | *Responsable* | *Momento de entrega* |
| :---- | :---- | :---- | :---- | :---- |
| 1.1 | Plan de Aseguramiento de la Calidad (PAC) | Documento estratégico general de calidad para el proyecto, que incluye enfoque, estándares aplicados y métricas de seguimiento | Calidad | Inicio del Sprint 1 |
| 1.2 | Plan de Pruebas (este documento) | Documento completo basado en IEEE 829 que define alcance, estrategias, recursos, cronograma y criterios de aceptación | Calidad | Sprint 1 |
| 1.3 | Casos de prueba Sprint 1 | Casos de prueba documentados para HU001–HU006, incluyendo caso feliz y escenarios de excepción, con campos: ID, precondición, pasos, resultado esperado y resultado obtenido | Calidad | Fase F2 Sprint 1 |
| 1.4 | Archivos .feature Sprint 1 | Escenarios Gherkin en lenguaje BDD para las HU del Sprint 1, listos para su automatización con Cucumber | Calidad | Fase F2 Sprint 1 |
| 1.5 | Informe de análisis estático Sprint 1 | Reporte exportado de SonarCloud con los resultados de cobertura, deuda técnica, complejidad ciclomática y vulnerabilidades del Sprint 1 | Calidad | Fase F4 Sprint 1 |
| 1.6 | Informe de ejecución de pruebas Sprint 1 | Reporte consolidado de los resultados de ejecución: casos ejecutados, pasaron/fallaron, defectos encontrados y estado de cierre | Calidad | Cierre Sprint 1 |

***Sprint 2***

| *ID* | *Entregable* | *Descripción* | *Responsable* | *Momento de entrega* |
| :---- | :---- | :---- | :---- | :---- |
| 2.1 | Casos de prueba Sprint 2 | Casos de prueba documentados para HU007–HU015, incluyendo caso feliz, excepciones y casos de seguridad avanzada | Calidad | Fase F2 Sprint 2 |
| 2.2 | Archivos .feature Sprint 2 | Escenarios Gherkin actualizados y ampliados con las HU del Sprint 2 | Calidad | Fase F2 Sprint 2 |
| 2.3 | Reporte de pruebas de regresión Sprint 1 | Evidencia de la ejecución completa de la suite de regresión del Sprint 1 al inicio del Sprint 2, con resultado por caso | Calidad | Inicio Sprint 2 |
| 2.4 | Suite Cucumber Sprint 1–2 automatizada | Implementación automatizada de los escenarios Gherkin de los Sprints 1 y 2 integrada al pipeline de CI/CD en GitHub Actions | Calidad | Fase F4 Sprint 2 |
| 2.5 | Informe de pruebas de seguridad Sprint 2 | Reporte de los resultados de las pruebas de seguridad: RBAC, ABAC, MFA, JWT, controles OWASP, datos enmascarados y rate limiting | Calidad | Fase F4 Sprint 2 |
| 2.6 | Informe de análisis estático Sprint 2 | Reporte exportado de SonarCloud con evolución de métricas respecto al Sprint 1 | Calidad | Fase F4 Sprint 2 |
| 2.7 | Informe de ejecución de pruebas Sprint 2 | Reporte consolidado de resultados del Sprint 2: casos ejecutados, defectos encontrados, estado de correcciones y métricas de calidad | Calidad | Cierre Sprint 2 |

***Sprint 3***

| *ID* | *Entregable* | *Descripción* | *Responsable* | *Momento de entrega* |
| :---- | :---- | :---- | :---- | :---- |
| 3.1 | Casos de prueba Sprint 3 | Casos de prueba documentados para HU016–HU023, incluyendo reembolsos, reportes, exportación y administración de comercios | Calidad | Fase F2 Sprint 3 |
| 3.2 | Archivos .feature Sprint 3 | Escenarios Gherkin completos del Sprint 3 integrados a la suite existente | Calidad | Fase F2 Sprint 3 |
| 3.3 | Reporte de pruebas de regresión Sprint 1 y Sprint 2 | Evidencia de la ejecución completa de la suite de regresión acumulada de los Sprints 1 y 2 al inicio del Sprint 3 | Calidad | Inicio Sprint 3 |
| 3.4 | Suite Cucumber completa (23 HU) | Suite de pruebas de aceptación automatizadas con cobertura de los escenarios de caso feliz de las 23 historias de usuario, ejecutable en CI/CD | Calidad | Fase F3 Sprint 3 |
| 3.5 | Informe de pruebas de rendimiento | Resultados de las pruebas de rendimiento con K6/JMeter sobre los endpoints críticos, comparados contra los umbrales definidos en las notas técnicas | Calidad | Fase F3 Sprint 3 |
| 3.6 | Informe de análisis estático Sprint 3 | Reporte final de SonarCloud con el estado definitivo de los Quality Gates del proyecto | Calidad | Fase F4 Sprint 3 |
| 3.7 | Informe de ejecución de pruebas Sprint 3 | Reporte consolidado de resultados del Sprint 3 con estado de todas las HU probadas en el sprint | Calidad | Cierre Sprint 3 |
| 3.8 | Informe final de calidad del proyecto | Documento de cierre que consolida: resumen ejecutivo de calidad, evolución de métricas por sprint, defectos totales encontrados y cerrados, cumplimiento de Quality Gates, lecciones aprendidas del proceso de pruebas y recomendaciones para futuros proyectos | Calidad | Cierre Sprint 3 |

### **Resumen consolidado de entregables**

| *Sprint* | *Cantidad de Entregables* | *Entregable más crítico* |
| :---- | :---- | :---- |
| Sprint 1 | 6 | Plan de Pruebas (ENT-1.2) e Informe de ejecución Sprint 1 (ENT-1.6) |
| Sprint 2 | 7 | Suite Cucumber automatizada Sprint 1–2 (ENT-2.4) e Informe de seguridad (ENT-2.5) |
| Sprint 3 | 8 | Suite Cucumber completa 23 HU (ENT-3.4) e Informe final de calidad (ENT-3.8) |
| **Total** | 21 |  |

### **Criterios de calidad de los entregables**

Para que un entregable sea considerado válido y aceptado, debe cumplir con las siguientes condiciones:

**ECE-01 — Trazabilidad** Todo entregable debe estar vinculado a las historias de usuario, casos de prueba o sprints correspondientes, sin referencias genéricas o incompletas.

**ECE-02 — Completitud** Los informes de ejecución deben incluir el resultado de cada caso de prueba ejecutado, sin omitir casos fallidos o bloqueados.

**ECE-03 — Evidencia** Los defectos reportados y los resultados de pruebas deben estar respaldados por capturas de pantalla, logs exportados o respuestas JSON según corresponda.

**ECE-04 — Versionado** Todos los documentos deben incluir número de versión, fecha de última actualización y autor. Los archivos `.feature` y la suite Cucumber deben estar versionados en el repositorio GitHub del proyecto.

**ECE-05 — Disponibilidad** Los entregables académicos deben estar disponibles en Azure DevOps o en el repositorio GitHub antes de la fecha de entrega definida por el calendario académico del semestre.

