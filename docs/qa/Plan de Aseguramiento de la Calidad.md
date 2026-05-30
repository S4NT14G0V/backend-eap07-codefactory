# **Plataforma de APIs de Pagos Embebidos tipo Stripe — Caso 8**

# 

**Plan de Aseguramiento de la Calidad**

Equipo Avanzado Presencial \#07 — CodeF@ctory — Universidad de Antioquia

Versión 3.0 · Sprints 1 – 3 · 24 HU · Plan de Aseguramiento de la Calidad

## **Historial de Revisiones**

| Fecha | Versión | Descripción | Autor |
| :---- | :---- | :---- | :---- |
| 06/04/2026 | 1.0 | Primera versión del plan de aseguramiento de la calidad, abordando los 3 sprints y 23 historias de usuario. | Alejandro Chavarria Mora |
| 13/04/2026 | 2.0 | Cambio en la justificación del estándar en la introducción, actores con roles y funciones actualizado, e implementación de metodología de versionamiento con TBD | Alejandro Chavarria Mora |
| 11/05/2026 | 3.0 | Cambios en la estructura del documento para coincidir con el formato establecido por la IEEE en su estándar 730 del 2014\. Agregado contenido relevante que no estaba incluído en la versión anterior, en particular definiciones, acrónimos y referencias utilizadas a lo largo de todo el documento, así como en la ejecución del plan. | Juan Diego Portillo |
|  |  |  |  |

## **Introducción**

### **Resumen**

El plan de aseguramiento de la calidad, se construye con base en dos estándares del IEEE, seleccionados debido a su pertinencia con el contexto del proyecto y las prácticas actuales de la industria del desarrollo de software.

* **IEEE 730-2014** (Software Quality Assurance Processes) que define los requisitos mínimos para la planificación, ejecución y supervisión de los procesos de aseguramiento de la calidad del software. Se adoptó este estándar debido a que:


1.  Proporciona una estructura reconocida internacionalmente para documentar las actividades de QA, lo que facilita la evaluación objetiva del cumplimiento por parte del cuerpo docente.  
2. Exige que cada actividad de calidad esté asociada a un responsable, un cronograma y un artefacto verificable, lo cual es directamente aplicable al contexto del proyecto en sus tres sprints bien delimitados.  
3. Es compatible con metodologías ágiles cuando se adapta correctamente, permitiendo combinar el rigor formal del estándar con la flexibilidad de Scrum.

   

* **IEEE-12207-2008** (Software Life Cycle Processes), el cual define un marco estructurado para los procesos del ciclo de vida del software, incluyendo procesos técnicos, de soporte, gestión y aseguramiento. Este estándar fue adoptado debido a que:


1. Permite alinear las actividades de aseguramiento de calidad con procesos formales asociados a la transversalidad con otros equipos y la resolución de problemas.  
2. Facilita la trazabilidad entre historias de usuario, criterios de aceptación, artefactos técnicos y actividades de QA definidas durante los sprints.  
3. Proporciona una base metodológica para integrar prácticas modernas de desarrollo ágil con procesos formales de calidad, particularmente relevantes en un sistema financiero transaccional con requisitos de seguridad, auditoría y resiliencia  
4. Define explícitamente procesos de soporte fundamentales para el proyecto, como gestión de configuración, verificación, validación, revisiones técnicas y auditorías, los cuales son necesarios debido a la criticidad del dominio de pagos embebidos y manejo de credenciales sensibles.

   

En conjunto, ambos estándares permiten establecer una estrategia integral de aseguramiento de calidad: el primero define cómo planificar, ejecutar y controlar las actividades SQA, mientras que el segundo proporciona el marco de procesos del ciclo de vida sobre el cual dichas actividades se aplican. Esta combinación permite que las actividades de calidad no se limiten únicamente a pruebas funcionales, sino que también incluyan gestión de configuración, control de cambios, revisiones técnicas, auditorías, validación de requisitos y monitoreo continuo de conformidad durante todo el ciclo de desarrollo. 

Debido a que el proyecto implementa una plataforma de pagos, el aseguramiento de calidad adquiere una relevancia crítica, especialmente en aspectos relacionados con:

* integridad transaccional  
* seguridad de credenciales  
* trazabilidad de eventos financieros  
* prevención de fraude  
* control de acceso  
* resiliencia ante fallos  
* consistencia de estados de pago

Por esta razón, el plan incorpora actividades específicas de validación funcional, pruebas de seguridad, análisis estático, auditoría de eventos y monitoreo de conformidad técnica.

### **Propósito**

Este plan tiene como propósito establecer la estrategia, los procesos, los estándares y las actividades de calidad que el equipo del proyecto seguirá durante el desarrollo de la Plataforma de APIs de Pagos Embebidos (Caso 8), en el marco del programa CodeF@ctory de la Universidad de Antioquia.

Se definen los mecanismos que garantizarán que el producto de software entregado cumpla con los requisitos funcionales y no funcionales establecidos en el backlog de historias de usuario, con los criterios mínimos exigidos para equipos de nivel avanzado, y con los estándares de calidad, seguridad y pruebas acordados por el equipo.

El enfoque de calidad definido en este plan es preventivo y continuo, integrando actividades de aseguramiento desde las etapas tempranas del ciclo de vida del software y no únicamente durante la fase de pruebas. Las actividades SQA estarán presentes durante la planificación, desarrollo, integración, despliegue y validación del sistema.

### **Alcance**

El presente plan abarca las actividades de aseguramiento de calidad aplicadas a todos los artefactos y entregables del proyecto distribuidos en 24 HU durante 3 sprints. El alcance cubre:

* Backend principal desarrollado en Spring Boot.  
* APIs REST transaccionales.  
* Módulo de identidad y comercios.  
* Módulo de procesamiento de pagos.  
* Módulo de auditoría y observabilidad.  
* Servicio Sandbox Mock.  
* Persistencia relacional y procedimientos almacenados.  
* Pipeline de integración continua.  
* Quality Gates y análisis estático.  
* Seguridad de APIs y mecanismos de autenticación/autorización.

Las actividades SQA incluyen análisis estático, validación de historias de usuario, pruebas unitarias, pruebas de integración, validación de seguridad y control de conformidad arquitectónica.

Este plan no cubre:

* certificaciones regulatorias financieras reales;  
* cumplimiento PCI-DSS formal;  
* pruebas de penetración externas;  
* despliegues productivos reales sobre entidades financieras;  
* integración con redes bancarias reales;  
* ni pruebas de carga a escala empresarial.

### **Definiciones y acrónimos**

#### **Definiciones**

**API Key:** Credencial utilizada para autenticar y autorizar el acceso de un comercio a los servicios expuestos por la plataforma de pagos.

**Idempotencia:** Propiedad mediante la cual múltiples solicitudes idénticas generan un único efecto transaccional, evitando cobros duplicados ante reintentos o fallos de red. 

**Máquina de Estados Transaccional:** Modelo de control que define las transiciones válidas entre estados de una transacción de pago, tales como creado, aprobado, rechazado y reembolsado.

**Sandbox:** Entorno aislado de pruebas que permite simular transacciones financieras sin afectar datos ni servicios reales.

**Quality Gate:** Conjunto de criterios automáticos de calidad que deben cumplirse para permitir la integración o liberación del software, especialmente importante en procesos de integración continua.

**Deuda Técnica:** Costo acumulado derivado de decisiones técnicas subóptimas que pueden afectar la mantenibilidad, calidad o evolución futura del sistema.

**Observabilidad:** Capacidad del sistema para permitir monitoreo, trazabilidad y diagnóstico mediante logs, métricas y eventos estructurados.

**MFA (Autenticación Multifactor):** Mecanismo de autenticación que requiere múltiples factores de validación para verificar la identidad de un usuario.

**RBAC:** Modelo de control de acceso basado en roles que restringe operaciones según permisos asignados al usuario.

**Trazabilidad:** Capacidad de relacionar historias de usuario, criterios de aceptación, código fuente, pruebas, defectos y artefactos de calidad durante todo el ciclo de vida del software.

#### **Acrónimos**

Los acrónimos hacen referencia a terminología asociada al contexto del desarrollo de software y en particular, al aseguramiento de la calidad, que por estar ubicado dentro de este contexto, deriva en palabras o expresiones originalmente en el idioma inglés.

| Acrónimo | Significado | Traducción al español |
| :---- | :---- | :---- |
| **API** | Application Programming Interface | Interfaz de programación de aplicaciones |
| **BDD** | Behavior Driven Development | Desarrollo dirigido por comportamiento |
| **CI/CD** | Continuous Integration / Continuous Delivery | Integración Continua / Entrega Continua |
| **DoD** | Definition of Done | Definición de terminado |
| **HU** | Historia de Usuario | N/A |
| **JWT** | JSON Web Token | Token web JSON |
| **MFA** | Multi-Factor Authentication | Autenticación multifactor |
| **OWASP** | Open Worldwide Application Security Project | Proyecto Abierto Mundial de Seguridad de Aplicaciones |
| **PAC** | Plan de Aseguramiento de la Calidad | N/A |
| **QA** | Quality Assurance | Aseguramiento de la calidad |
| **RBAC** | Role-Based Access Control | Control de acceso basado en roles |
| **REST** | Representational State Transfer | Transferencia de estado representacional |
| **SLA** | Service Level Agreement | Acuerdo de nivel de servicio |
| **SQA** | Software Quality Assurance | Aseguramiento de la Calidad del Software |
| **TBD** | Trunk-Based Development | Desarrollo basado en troncos |
| **UUID** | Universally Unique Identifier | Identificador único universal |

Todas las definiciones y acrónimos establecidos en este documento deberán utilizarse consistentemente en historias de usuario, documentación técnica, artefactos de pruebas, reportes de calidad y entregables asociados al proyecto, con el fin de mantener uniformidad terminológica y trazabilidad documental.

### **Referencias**

| ID  | Documento  | Tipo  | Versión  | Propósito  |
| :---- | :---- | :---- | :---- | :---- |
| REF-01  | IEEE Std 730-2014 – Software Quality Assurance Processes  | Normativa  | 2014  | Define estructura y actividades SQA  |
| REF-02  | ISO/IEC/IEEE 12207:2008 – Software Life Cycle Processes  | Normativa  | 2008  | Define procesos del ciclo de vida del software |
| REF-03  | OWASP ASVS / OWASP Top 10  | Normativa  | Vigente | Referencia para controles de seguridad  |
| REF-04  | OpenAPI Specification  | Normativa  | v3.x  | Contrato y documentación APIs  |
| REF-05  | Scrum Guide  | Informativa  | Vigente  | Marco ágil utilizado |
| REF-06 | Backlog de Historias de Usuario  | Interna | 3.0 | Requisitos funcionales y criterios BDD  |
| REF-07 | Plan de Pruebas | Interna | 1.0 | Estrategias de implementación y ejecución de pruebas |
| REF-08 | Criterios mínimos y entregables — Nivel Avanzado  | Interna (Fábrica Escuela) | 1.0 | Restricciones técnicas y quality gates  |
| REF-09 | Handbook de Casos  | Interna (Fábrica Escuela) | 2.0 | Descripción del proyecto y requisitos dados por los stakeholders del caso. |

## 

## **Visión General del Plan**

### 

### **Organización**

El equipo del proyecto está conformado por estudiantes del programa de Ingeniería de Sistemas de la Universidad de Antioquia, organizados en cuatro roles funcionales. El aseguramiento de la calidad es responsabilidad de los estudiantes del curso “Calidad de Software”, pero su ejecución efectiva requiere coordinación continua con los demás roles, dado que las actividades de prueba, análisis estático y automatización están directamente ligadas al código producido por el equipo de “Arquitectura de Software” y a los datos gestionados por el equipo de “Bases de Datos”, así como a la gestión del proyecto establecida por el equipo de “Gestión de Proyectos de Sistemas de Información”.

Los integrantes del equipo son los siguientes:

| Integrantes | Roles | Funciones asociadas al proceso de QA |
| :---- | :---- | :---- |
| Miguel Ángel Castiblanco Florez | Quality Assurance. | Diseño de casos de prueba iniciales. Ejecución de pruebas funcionales. Reporte de defectos en Azure DevOps. Configuración del entorno Docker Compose para pruebas de integración.  |
| Alejandro Chavarria Mora | Quality Assurance. | Implementación de pruebas unitarias (JUnit 5 / Mockito). Configuración y monitoreo de SonarCloud. Validación de cobertura y deuda técnica. Redacción inicial de los planes de calidad.  |
| Juan Diego Portillo Parada | Quality Assurance. | Automatización de pruebas de aceptación con Cucumber/Gherkin. Revisión de criterios Gherkin y refinamiento de las historias de usuario, así como de los planes de calidad. |
| Samuel Puerta Patiño | Quality Assurance, Gestión del Proyecto. | Coordinación entre el equipo de Calidad y Gestión de Proyectos. Auditorías de proceso (DoD/DoR), elaboración de casos de prueba así como la redacción de informes de calidad por sprint. Seguimiento de métricas ágiles de calidad en Azure DevOps. |
| Santiago Correa Marulanda | Arquitecto y gestor de bases de datos. | Proveer código testeable con separación de capas. Revisar arquitectura desde perspectiva de testeabilidad. Colaborar en configuración CI/CD para análisis estático. |
| Jeronimo Duque Alzate  | Arquitecto y gestor de bases de datos. | Diseño de endpoints REST documentados con OpenAPI/Swagger. Implementación de pruebas unitarias propias del módulo. Soporte en pruebas de integración contra BD. |
| Marlon Giraldo Ramirez | Arquitecto y gestor de bases de datos. | Implementación del módulo de auditoría e inmutabilidad. Definición de índices y consultas que soportan los reportes de calidad. Soporte técnico en ejecución de pruebas de integración. |
| Santiago Alexander Losada Muñoz | Gestión del proyecto. | Gestión del backlog y sprint backlog en Azure DevOps. Facilitación de ceremonias ágiles. Seguimiento del Burndown Chart y métricas del proyecto. |
| Juan Esteban Obando Soto | Gestión del proyecto. | Gestión del plan de releases. Coordinación de la retrospectiva y documentación de acciones de mejora. Seguimiento de riesgos de calidad. |
| Santiago Trespalacios Bolivar | Gestión del proyecto. | Aplicación y análisis del instrumento SAMM. Gestión de lecciones aprendidas. Cierre del proyecto en Azure DevOps. |

Dentro del equipo de Calidad de Software no se establece una jerarquía formal, sino una distribución de responsabilidades por actividad como se especifica en la siguiente sección. Samuel Puerta Patiño actúa como punto de articulación entre el equipo de calidad y el de gestión de proyectos, dado que participa en ambos roles.

La autoridad sobre el proceso de QA recae en el equipo de Calidad de Software, que tiene la potestad de bloquear la integración de código que no cumpla los Quality Gates definidos en la Sección de Quality Gates Definidos. Las decisiones sobre arquitectura que afecten la testeabilidad del sistema deben ser consensuadas entre el equipo de Arquitectura y el de Calidad.

#### **Responsabilidades por equipo**

El detalle de las actividades a desarrollar puede ser encontrado en la sección “[*Actividades, resultados y tareas*](#actividades,-resultados-y-tareas)”, sin embargo, en esta sección, queremos hacer énfasis en la responsabilidad del equipo de QA con relación a las actividades de revisión o auditoría en el proyecto.

| Actividad de revisión o auditoría | Responsable principal | Participantes |
| :---- | :---- | :---- |
| Revisión de requisitos (DoR) | Equipo de Calidad | Equipo de Gestión \+ Arquitectura |
| Revisión de diseño de casos de prueba | Equipo de Calidad | Equipo de Arquitectura |
| Revisión conjunta de sprint (Joint Review) | Equipo de Gestión | Todo el equipo junto con el equipo de Fábrica Escuela |
| Auditoría del proceso | Equipo de Gestión \+ Calidad | Todo el equipo |
| Auditoría de código estático (SonarCloud) | Equipo de Calidad | Equipo de Arquitectura |
| Revisión de cierre del proyecto | Todo el equipo | Equipo de Fábrica Escuela |

No existen agencias externas de regulación formales en el contexto académico del proyecto, sin embargo, los tutores del programa CodeF@ctory, actúan como evaluadores, cuya aprobación es requerida en la revisión conjunta de cada sprint.

#### **Responsabilidades por Sprint**

Las siguientes actividades de QA se ejecutarán a lo largo de los tres sprints del proyecto. Cada actividad está sincronizada con los hitos del proyecto y su respectivo entregable y tiene al menos un rol responsable asignado.

##### **Sprint 1**

| Actividad | Descripción | Responsable |
| :---- | :---- | :---- |
| Elaboración del Plan de Aseguramiento de la Calidad | Redacción y aprobación del plan de aseguramiento de la calidad (PAC) para los tres sprints | Calidad de Software |
| Elaboración del Plan de Pruebas | Definición de la estrategia detallada de pruebas para los tres sprints | Calidad de Software |
| Refinamiento de HU en Gherkin (Sprint 1\) | Revisión y ajuste de los criterios de aceptación de las HU priorizadas en el Sprint 1 (HU001–HU005) | Calidad de Software |
| Diseño de casos de prueba (Sprint 1\) | Definición de casos de prueba funcionales para las HU del Sprint 1, incluyendo caso feliz y excepciones | Calidad de Software |
| Revisión conjunta con Arquitectura | Verificación de que el diseño del sistema es testeable y que los endpoints expuestos permiten la ejecución de pruebas de integración | Calidad de Software \+ Arquitectura de Software |

##### **Sprint 2**

| Actividad | Descripción | Responsable |
| :---- | :---- | :---- |
| Ajuste de HU en Gherkin (Sprint 2\) | Revisión y refinamiento de criterios de aceptación de las HU006 hasta la HU015 | Calidad de Software |
| Refinamiento del plan de aseguramiento de la calidad | Revisión y ajustes de acuerdo a los cambios y necesidades observados después del desarrollo del Sprint 1 | Calidad de Software |
| Refinamiento del plan de pruebas | Revisión y ajustes de acuerdo a los cambios en la estrategia de pruebas y a la retroalimentación obtenida del equipo de Fábrica Escuela en el Sprint 1 | Calidad de Software |
| Diseño y ejecución de casos de prueba (Sprints 1 y 2\) | Creación y ejecución de casos de prueba para todas las HU de los sprints 1 y 2 | Calidad de Software |
| Configuración de SonarCloud | Configuración del análisis estático sobre el repositorio en GitHub con los Quality Gates definidos | Calidad de Software \+ Arquitectura de Software |
| Implementación de pruebas unitarias | Escritura de pruebas unitarias con patrón AAA y validación de cobertura (≥ 40%) | Calidad de Software \+ Arquitectura de Software |
| Validación de complejidad ciclomática y cognitiva | Revisión de los reportes de SonarCloud y apertura de issues para reducir complejidad fuera de umbral | Calidad de Software |
| Validación de deuda técnica | Seguimiento de la deuda técnica reportada por SonarCloud para mantenerse dentro del umbral de 2 días | Calidad de Software |
| Auditoría de proceso (Sprint 2\) | Revisión del cumplimiento del DoD y DoR en las HU entregadas durante el Sprint 2 | Calidad de Software \+ Gestión de Proyectos |

##### **Sprint 3**

| Actividad | Descripción | Responsable |
| :---- | :---- | :---- |
| Automatización de pruebas de aceptación (E2E) | Implementación de pruebas automatizadas sobre los criterios de aceptación Gherkin de las HU priorizadas | Calidad de Software |
| Ejecución de pruebas automatizadas E2E | Ejecución sobre el objeto de prueba desplegado y registro de resultados | Calidad de Software |
| Auditoría de proceso (Sprint 3\) | Verificación del cumplimiento del DoD en las HU del Sprint 3 y revisión de acciones de mejora derivadas de retrospectivas anteriores | Calidad de Software \+ Gestión de Proyectos |
| Revisión de cierre de calidad | Definición de casos de prueba funcionales para las HU del Sprint 3, incluyendo caso feliz y excepciones | Calidad de Software |
| Revisión conjunta con Arquitectura | Consolidación del informe final de calidad: cobertura, deuda técnica, vulnerabilidades y resultados de pruebas automatizadas | Calidad de Software |

### **Riesgos del producto de software**

Debido a la naturaleza crítica del proyecto —una plataforma de pagos embebidos con procesamiento transaccional, autenticación segura y trazabilidad financiera—, el aseguramiento de calidad deberá priorizar aquellas funcionalidades cuyo fallo pueda comprometer la integridad de las transacciones, la seguridad de la información o la estabilidad operativa del sistema.

En concordancia con IEEE 730-2014, las actividades SQA se ejecutarán de manera proporcional al nivel de riesgo identificado para cada componente o funcionalidad del producto. Por esta razón, se establece la siguiente matriz de riesgos, la cual identifica los principales riesgos técnicos, funcionales y de seguridad del proyecto, junto con su impacto, probabilidad, severidad y estrategia de mitigación esperada. Una versión detallada de la matriz de riesgo con enfoque a las estrategias de mitigación por medio de pruebas puede encontrarse en el Plan de Pruebas (REF-07).

Esta matriz servirá como mecanismo de priorización para las actividades de validación, pruebas, revisiones técnicas y controles de calidad ejecutados durante los sprints del proyecto.

| Riesgo | Impacto | Probabilidad | Severidad | Mitigación |
| :---- | :---- | :---- | :---- | :---- |
| Cobros duplicados | Alto | Medio | Crítico | Idempotencia y pruebas |
| Vulnerabilidad JWT | Alto | Medio | Alto | Validaciones de seguridad |
| Falla MFA | Alto | Bajo | Alto | Pruebas en autenticación |
| Transición inválida estados | Alto | Medio | Crítico | Pruebas en la máquina de estados |
| Fuga credenciales API | Alto | Bajo | Crítico | Pruebas en la gestión de credenciales y secretos |
| Logs sin trazabilidad | Medio | Medio | Alto | Auditoría estructurada |
| Timeout sandbox | Medio | Alto | Medio | Pruebas de sistema para el entorno de pruebas |

### **Herramientas**

La priorización por Sprint de las herramientas utilizadas para las actividades de aseguramiento de la calidad está descrita a detalle en la subsección [*“Propósito y priorización de herramientas”*](#propósito-y-priorización-de-herramientas). A continuación se presenta un resumen consolidado, agrupado por categoría, de las herramientas utilizadas:

**Gestión y trazabilidad:** Azure DevOps se utiliza como herramienta central para la gestión del backlog, el registro y seguimiento de defectos, la configuración del tablero de métricas ágiles y el almacenamiento de los acuerdos de equipo, DoD y DoR.

**Control de versiones e integración continua:** GitHub con Trunk-Based Development (TBD) almacena el repositorio del proyecto y GitHub Actions ejecuta automáticamente el pipeline de CI/CD, que incluye la compilación, la ejecución de pruebas unitarias y el análisis estático con SonarCloud en cada integración a la rama principal.

**Análisis estático y calidad del código:** SonarQube Cloud realiza el análisis estático continuo del código fuente, reportando cobertura de pruebas, deuda técnica, complejidad ciclomática y cognitiva, vulnerabilidades y code smells. Los Quality Gates configurados en SonarQube Cloud actúan como puerta de calidad automática en el pipeline.

**Pruebas unitarias e integración:** JUnit 5 y Mockito se utilizan para la implementación de pruebas unitarias en el backend Spring Boot. RestAssured o Postman se utilizan para pruebas de integración sobre los endpoints REST. Docker Compose levanta el entorno de integración con backend y base de datos real en contenedor.

**Pruebas de aceptación:** Cucumber integrado con Spring Boot permite la automatización de los escenarios de aceptación escritos en Gherkin. Los reportes de ejecución de Cucumber proporcionan evidencia trazable del cumplimiento de los criterios de aceptación de cada HU.

**Seguridad:** El análisis SAMM se aplica al inicio y al cierre del proyecto como instrumento de evaluación de madurez en prácticas de seguridad del software.

#### **Propósito y priorización de herramientas** {#propósito-y-priorización-de-herramientas}

| Herramienta / Técnica | Propósito | Momento de uso |
| :---- | :---- | :---- |
| Azure DevOps | Gestión del backlog, registro de defectos, seguimiento de acciones correctivas y métricas ágiles | Continuo en los tres sprints |
| GitHub / GitHub Actions | Control de versiones, integración continua y ejecución automática del pipeline de análisis y pruebas | Continuo desde Sprint 1 |
| SonarCloud | Análisis estático de código, medición de cobertura, deuda técnica, complejidad y vulnerabilidades | Desde Sprint 2, continuo |
| Junit 5 \+ Mockito | Implementación y ejecución de pruebas unitarias en el backend Spring Boot con patrón AAA | Sprint 2 y Sprint 3 |
| Cucumber / Gherkin | Escritura y automatización de escenarios de prueba de aceptación derivados de los criterios de las HU | Sprint 1 (escritura), Sprint 3 (automatización) |
| Postman / RestAssured | Pruebas de integración y de aceptación sobre los endpoints REST de la plataforma | Sprint 2 y Sprint 3 |
| Docker Compose | Levantamiento del entorno de pruebas de integración con backend y base de datos real o en contenedor | Sprint 2 y Sprint 3 |
| Checklist de revisión (DoD / DoR) | Verificación estructurada del cumplimiento de criterios de completitud y preparación de cada HU | Al inicio y cierre de cada sprint |
| Revisión por pares (Peer Review) | Revisión de código entre miembros del equipo antes de la integración a la rama principal | Continuo desde sprint 1 |
| Análisis SAMM | Evaluación de la madurez del equipo en prácticas de seguridad del software al inicio y cierre del proyecto | Sprint 1 y Sprint 3 |

Todas las herramientas definidas en esta sección forman parte obligatoria del ecosistema de calidad del proyecto y deberán utilizarse consistentemente durante el desarrollo, validación y despliegue de los entregables. 

Los registros de cada revisión (actas, resultados de checklists, reportes de SonarCloud y resultados de pruebas) serán almacenados en el repositorio del proyecto en GitHub, en una carpeta dedicada a documentación de calidad, y referenciados desde Azure DevOps para mantener la trazabilidad completa entre los hallazgos y las acciones tomadas.

### **Estándares, prácticas y convenciones**

#### **Estándares y marcos de referencia**

Los siguientes estándares y marcos de referencia establecen las bases metodológicas, técnicas y de calidad utilizadas durante el desarrollo del proyecto. Su adopción permite garantizar alineación con buenas prácticas de la industria en procesos de aseguramiento de calidad, desarrollo seguro y arquitectura de software.

| Estándar | Aplicación en el Proyecto |
| :---- | :---- |
| IEEE 730-2014 | Estándar base para la definición, planificación y ejecución de las actividades de aseguramiento de calidad del software (SQA), incluyendo revisiones, validaciones, control de conformidad y seguimiento de métricas de calidad durante los sprints |
| IEEE 12207-2008 | Marco de referencia para los procesos del ciclo de vida del software, utilizado para estructurar actividades de desarrollo, verificación, validación, auditoría, gestión de configuración y resolución de problemas dentro del proyecto |
| OpenAPI / Swagger | Estándar de documentación y contrato de las APIs REST expuestas por la plataforma, con versionado |
| ISO/IEC 25010 | Modelo de calidad utilizado como referencia para atributos de calidad del producto, incluyendo seguridad, mantenibilidad, confiabilidad y eficiencia.  |
| OWASP Top 10 | Referencia para validación de vulnerabilidades comunes en APIs, autenticación, manejo de sesiones y protección de datos sensibles.  |

#### **Convenciones de desarrollo** 

Las siguientes convenciones definen criterios técnicos y lineamientos de implementación utilizados por el equipo de desarrollo para garantizar consistencia estructural, mantenibilidad, legibilidad y desacoplamiento en los componentes del sistema desarrollados durante el proyecto. 

| Convención | Aplicación en el Proyecto |
| :---- | :---- |
| Monolito Modular | Separación explícita de dominios funcionales (identidad, transacciones, auditoría y sandbox) para reducir acoplamiento y facilitar mantenibilidad del sistema  |
| Principios SOLID | Aplicación de principios de diseño orientados a mejorar extensibilidad, reutilización y desacoplamiento de componentes del backend desarrollado en Spring Boot  |
| Repository Pattern | Abstracción de acceso a datos mediante repositorios para desacoplar lógica de negocio de la capa de persistencia relacional  |
| Logs Estructurados JSON  | Generación de logs estructurados y consistentes para trazabilidad de transacciones, auditoría de eventos y monitoreo del sistema  |
| SonarQube Quality Rules | Aplicación automática de reglas de calidad estática relacionadas con complejidad, duplicación, vulnerabilidades y mantenibilidad del código fuente  |

#### **Convenciones de pruebas** 

Las siguientes prácticas y convenciones orientan las actividades de validación funcional, pruebas automatizadas y aseguramiento de calidad del software. Su objetivo es garantizar trazabilidad entre requisitos, historias de usuario, criterios de aceptación y resultados de validación ejecutados durante cada sprint. 

| Convención | Aplicación en el Proyecto |
| :---- | :---- |
| Gherkin / BDD | Definición de criterios de aceptación mediante escenarios Given / When / Then para garantizar trazabilidad entre historias de usuario y facilitar las validaciones funcionales  |
| Patrón AAA (Arrange-Act-Assert) | Estructuración uniforme de pruebas unitarias para mejorar legibilidad, mantenibilidad y consistencia en los casos de prueba  |
| Integración Continua (CI/CD)  | Ejecución automática de pruebas, análisis estático y validaciones de calidad antes de integrar cambios al repositorio principal  |
| Cobertura Mínima de Pruebas  | Validación de cobertura mínima establecida por los Quality Gates definidos para el proyecto y monitoreados mediante SonarQube  |
| Authentication & Authorization Testing  | Validación de controles JWT, MFA y RBAC para garantizar autenticación segura y restricciones correctas de acceso a endpoints  |

#### **Convenciones de seguridad** 

Las siguientes convenciones y lineamientos de seguridad establecen los controles mínimos requeridos para proteger la autenticación, autorización, integridad transaccional y manejo de credenciales dentro de la plataforma de pagos embebidos. 

| Convención | Aplicación en el Proyecto |
| :---- | :---- |
| JWT Expirable Tokens | Utilizados para autenticación segura de usuarios y comercios dentro de las APIs de la plataforma. Su uso busca reducir riesgos asociados a secuestro de sesión, reutilización de credenciales y accesos persistentes no autorizados  |
| RBAC  | Modelo de autorización utilizado para restringir operaciones y endpoints según los roles asignados a cada usuario o comercio, aplicando el principio de mínimo privilegio en funcionalidades administrativas, transaccionales y de auditoría  |
| MFA  | Mecanismo obligatorio para accesos administrativos y operaciones sensibles del sistema, incorporando múltiples factores de autenticación con el objetivo de fortalecer la protección de las cuentas. |
| Principle of Least Privilege | Convención de seguridad aplicada para garantizar que cada usuario, servicio o componente posea únicamente los permisos estrictamente necesarios para ejecutar sus funciones dentro de la plataforma  |
| Secrets Management  | Práctica orientada a proteger credenciales sensibles, API Keys, secretos JWT y configuraciones críticas, evitando su almacenamiento directo en el código fuente o repositorios del proyecto  |
| Validación de Payloads  | Validación estricta de datos de entrada en endpoints REST mediante reglas de formato, obligatoriedad, tipos de datos y restricciones de negocio, reduciendo riesgos de inyección, manipulación de parámetros y solicitudes inválidas  |
| Structured Security Logs  | Generación de logs estructurados en formato consistente (JSON) para registrar eventos de autenticación, autorización, errores críticos, cambios de estado transaccional y eventos relevantes de seguridad, facilitando auditoría y trazabilidad  |
| Token Rotation  | Estrategia de renovación periódica de tokens y credenciales de acceso para reducir exposición ante filtraciones o reutilización de sesiones comprometidas  |
| Análisis Estático de Seguridad (SAST) | Revisión automática del código fuente mediante herramientas de análisis estático para detectar vulnerabilidades, malas prácticas y riesgos de seguridad antes del despliegue  |
| Control de Acceso por Endpoint  | Restricción explícita de operaciones REST según permisos, roles y contexto de autenticación definidos para cada actor del sistema  |

#### **Convenciones de gestión y trazabilidad** 

Las siguientes prácticas definen mecanismos de organización, seguimiento y control utilizados para mantener trazabilidad entre historias de usuario y los cambios en el código.

| Convención | Aplicación en el Proyecto |
| :---- | :---- |
| Conventional Commits | Convención para mensajes de commit en GitHub, facilitando la trazabilidad entre código y HU en Azure DevOps |
| Definition of Done (DoD) | Criterio de completitud que toda HU debe cumplir antes de considerarse terminada, incluyendo pruebas escritas y ejecutadas |
| Definition of Ready (DoR) | Criterio que toda HU debe cumplir antes de ser incluida en un sprint, incluyendo criterios de aceptación en Gherkin completos |

### **Esfuerzos, recursos y programación**

Las actividades de aseguramiento de calidad serán ejecutadas de manera incremental durante los tres Sprints del proyecto, priorizando las funcionalidades clasificadas como críticas desde el punto de vista transaccional, de seguridad y auditoría. La planificación de esfuerzo SQA se encuentra alineada con el backlog de historias de usuario, la estrategia de pruebas definida para el proyecto y los Quality Gates establecidos mediante SonarQube.

#### **Recursos para el aseguramiento de la calidad**

| Recurso | Aplicación dentro del Proyecto |
| :---- | :---- |
| QA Engineers | Validación funcional, seguimiento de defectos, trazabilidad y control de conformidad |
| Arquitectura | Revisión de decisiones técnicas y validación de cumplimiento arquitectónico |
| SonarQube / SonarCloud | Análisis estático, métricas y Quality Gates definidos para el proyecto |
| Azure DevOps | Gestión de backlog, trazabilidad y seguimiento de actividades |
| Docker Compose | Consistencia de ambientes de integración y pruebas |
| PostgreSQL | Persistencia y validación de integridad transaccional |
| Spring Boot | Framework backend principal |
| Postman / RestAssured | Validación manual y automatizada de APIs REST |

#### **Distribución de actividades por Sprint**

| Sprint | Actividades SQA Prioritarias |
| :---- | :---- |
| **Sprint 1** | Validación de onboarding, autenticación, APIs iniciales y generación de credenciales |
| **Sprint 2** | Validación de seguridad, auditoría, trazabilidad, antifraude y observabilidad |
| **Sprint 3** | Validación de reporting, reembolsos, resiliencia y entorno Sandbox |

#### **Alcance de las pruebas por Sprint**

##### **Sprint 1**

Las pruebas del Sprint 1 se enfocan en las funcionalidades del mínimo producto viable de la aplicación.. El equipo de QA diseña los casos de prueba para las HU001 a HU005, cubriendo los escenarios del caso feliz y las excepciones definidas en los criterios de aceptación en formato Gherkin.

##### **Sprint 2**

Las pruebas del Sprint 2 abarcan la implementación y ejecución de la aplicación y se amplía su alcance. Se escriben y ejecutan las pruebas unitarias del backend con cobertura verificada por SonarCloud. Se ejecutan los casos de prueba diseñados para las HU de los sprints 1 y 2 (HU001–HU015), sujeto a modificaciones según el alcance logrado por el equipo en cuanto al cumplimiento de las HU. Se activa el análisis estático continuo y se verifica el cumplimiento de los Quality Gates definidos.

##### **Sprint 3**

Las pruebas del Sprint 3 se centran en la automatización y el cierre. Se configuran las pruebas de integración sobre Docker Compose. Se automatizan los escenarios de aceptación Gherkin de las HU priorizadas mediante Cucumber. Se ejecutan las pruebas E2E sobre el objeto de prueba desplegado en la nube. Se genera el informe final de calidad consolidando cobertura, resultados de pruebas automatizadas y estado de los Quality Gates.

#### **Cronograma de revisiones y auditorías**

| Actividad | Tipo | Sprint  | Momento de ejecución | Artefactos revisados |
| :---- | :---- | :---- | :---- | :---- |
| Revisión de requisitos — HU Sprint 1 | Requirements Review | 1 | Inicio del sprint 1 (Sprint planning) | HU001–HU006 en Gherkin |
| Revisión de diseño de casos de prueba — Sprint 1 | Test Design Review | 1 | Semana 2 \- sprint 1 | Casos de prueba HU001–HU005 |
| Revisión conjunta de Sprint 1 | Joint Review | 1 | Cierre sprint 1 | Funcionalidades HU001–HU005 desplegadas |
| Auditoría de proceso — Sprint 1 | Process Audit | 1 | Cierre sprint 1 (Retrospectiva) | DoD, DoR, backlog Azure DevOps, acuerdos de equipo |
| Revisión de requisitos — HU Sprint 2 | Requirements Review | 2 | Inicio del Sprint 2 (Sprint Planning) | HU007–HU015 en Gherkin |
| Auditoría de código estático — configuración inicial | SonarCloud Audit | 2 | Semana 1 del Sprint 2 | Repositorio GitHub, configuración Quality Gates |
| Revisión de diseño de casos de prueba — Sprint 2 | Test Design Review | 2 | Semana 1 del Sprint 2 | Casos de prueba HU006–HU015 |
| Revisión de cobertura y quality gates | SonarCloud Audit | 2 | Semana 2 del Sprint 2 | Reporte SonarCloud, cobertura, deuda técnica, complejidad |
| Revisión conjunta de Sprint 2 | Joint Review | 2 | Cierre del Sprint 2 | Funcionalidades HU006–HU015 desplegadas |
| Auditoría de proceso — Sprint 2 | Process Audit | 2 | Cierre del Sprint 2 (Retrospectiva) | DoD, backlog, métricas ágiles Azure DevOps |
| Revisión de requisitos — HU Sprint 3 | Requirements Review | 3 | Inicio del Sprint 3 (Sprint Planning) | HU016–HU024 en Gherkin |
| Revisión de pruebas automatizadas E2E | Test Design Review | 3 | Semana 1 del Sprint 3 | Escenarios automatizados sobre criterios Gherkin |
| Auditoría de código estático — cierre | SonarCloud Audit | 3 | Semana 2 del Sprint 3 | Reporte final SonarCloud |
| Revisión conjunta de Sprint 3 | Joint Review | 3 | Cierre del Sprint 3 | Plataforma completa desplegada |
| Auditoría de proceso — Sprint 3 | Process Audit | 3 | Cierre del Sprint 3 (Retrospectiva) | DoD, métricas, acciones de mejora aplicadas |
| Revisión de cierre del proyecto | Post-mortem Review | 3 | Última semana del proyecto | Informe de calidad, SAMM comparativo, lecciones aprendidas |

#### **Hitos de Calidad** 

| Hito | Criterio de Validación |
| :---- | :---- |
| Cierre de Sprint | Cumplimiento de Definition of Done y Quality Gates definidos y en cumplimiento |
| Integración de Código | Cumplimiento de la cobertura mínima y análisis estático satisfactorio |
| Validación de HU | Cumplimiento de criterios BDD/Gherkin para todas las 24 HU definidas |
| Release Interno | Ausencia de vulnerabilidades en estados “Blocker”, “High” o “Medium”, de acuerdo con las definiciones de SonarQube Cloud. |
| Validación Arquitectónica | Conformidad de cumplimiento con la arquitectura modular definida, sin afectar el cumplimiento de los Quality Gates definidos. |

## 

## **Actividades, resultados y tareas** {#actividades,-resultados-y-tareas}

### **Aseguramiento del producto**

#### **Conformidad de los planes**

La estrategia del plan de pruebas del proyecto sigue un enfoque por capas, alineado con la arquitectura de Monolito Modular definida para la plataforma. El detalle completo de los casos de prueba, entornos, datos de prueba y criterios de entrada y salida se encuentra en el  Plan de Pruebas (REF-07). Se ejecutarán cuatro tipos de prueba a lo largo de los tres sprints:

* **Pruebas unitarias:** verifican el comportamiento aislado de cada componente del backend (servicios, validadores, máquina de estados, reglas de negocio) sin dependencias externas. Se implementan con JUnit 5 y Mockito, siguiendo el patrón AAA (Arrange-Act-Assert). La cobertura mínima requerida es del 40%, medida por SonarCloud.

* **Análisis estático de código:** evalúa la calidad interna del código fuente de forma continua mediante SonarCloud, verificando el cumplimiento de los Quality Gates definidos en la Sección de Quality Gates Definidos en cada integración al repositorio.

* **Pruebas de aceptación automatizadas:** verifican el cumplimiento de los criterios de aceptación definidos en lenguaje Gherkin para cada historia de usuario. Se implementan con Cucumber integrado al proyecto Spring Boot y se ejecutan sobre el objeto de prueba desplegado. Su automatización es el entregable principal del Sprint 3 en materia de calidad.

* **Pruebas de integración:** verifican la interacción entre los módulos del sistema y la base de datos real o en contenedor. Se ejecutan sobre el entorno levantado con Docker Compose y validan que los flujos completos de negocio (registro de comercio, generación de credenciales, creación y procesamiento de transacciones, auditoría) funcionen correctamente de extremo a extremo a nivel de backend.

##### **Criterios de Entrada y Salida de las Pruebas**

**Criterios de entrada** (condiciones necesarias para iniciar la ejecución de pruebas sobre una HU):

- La HU tiene criterios de aceptación en Gherkin completos y aprobados por el equipo de Calidad.  
- La HU cumple el Definition of Ready (DoR).  
- El código de la HU está integrado en la rama main y el pipeline de CI/CD se ejecutó sin errores de compilación.  
- El entorno de pruebas está disponible y la base de datos de pruebas está inicializada con los datos necesarios.

**Criterios de salida** (condiciones necesarias para considerar las pruebas de una HU como completadas):

- Todos los casos de prueba diseñados para la HU han sido ejecutados.  
- Los casos de prueba del escenario feliz pasan exitosamente.  
- Los casos de prueba de excepción producen los comportamientos esperados definidos en los criterios Gherkin.  
- Los defectos críticos o mayores encontrados han sido corregidos y re-verificados.  
- La HU cumple el Definition of Done (DoD), incluyendo pruebas unitarias escritas y cobertura verificada.


#### **Conformidad del producto**

### 

### **Atributos de Calidad del Producto**

**Funcionalidad y corrección**

El sistema debe implementar correctamente las 23 historias de usuario priorizadas en los tres sprints, cumpliendo todos los criterios de aceptación descritos en lenguaje Gherkin. En particular, la máquina de estados de las transacciones (CREATED → PROCESSING → APPROVED | REJECTED | FAILED | REFUNDED) debe comportarse de forma determinista y sin transiciones inválidas. Los reembolsos parciales no deben superar el monto original del pago.

**Seguridad**

Dado que la plataforma maneja información financiera sensible de terceros, la seguridad es un atributo crítico. Se deben cumplir los siguientes objetivos:

- Autenticación multifactor (MFA/TOTP) obligatoria para administradores de plataforma.  
- Control de acceso basado en roles (RBAC) por endpoint, complementado con reglas ABAC básicas.  
- Cero vulnerabilidades críticas reportadas por SonarCloud.  
- Políticas robustas de contraseñas, revocación de tokens en menos de 5 segundos y protección contra los controles OWASP básicos (inyección, manejo seguro de secretos, validación de entradas).  
- Registro inmutable de eventos de seguridad (logins fallidos, cambios críticos, transiciones de estado).

**Confiabilidad y trazabilidad**

Cada evento significativo de una transacción debe quedar registrado de forma inmutable en la bitácora de auditoría, con hash encadenado que permita verificar la integridad de la secuencia. El patrón Outbox debe garantizar la atomicidad entre el cambio de estado en base de datos y el envío de notificaciones. Los webhooks deben implementar reintentos con backoff exponencial ante fallos de entrega.

**Rendimiento**

La respuesta de creación de una transacción debe ser inferior a 300 ms en el percentil 99 bajo carga normal. La validación de credenciales en caché (Redis) debe reducir la carga sobre la base de datos. La revocación de credenciales debe ser efectiva en menos de 5 segundos.

**Mantenibilidad**

La deuda técnica del proyecto no debe superar un plazo de 2 días según el análisis de SonarCloud. La complejidad ciclomática debe mantenerse por debajo de 50 y la complejidad cognitiva debe controlarse activamente. El código debe seguir una separación de capas clara acorde al estilo de Monolito Modular definido en el documento de arquitectura.

**Cobertura y automatización de pruebas**

La cobertura de pruebas unitarias debe ser mayor o igual al 40%, medida y reportada desde SonarCloud. Los escenarios de aceptación derivados de los criterios Gherkin deben automatizarse progresivamente a lo largo de los sprints.

### **\* Revisión de Arquitectura**

**(Architecture Review)** Verifica que el diseño del sistema sea consistente con el estilo de Monolito Modular definido, que los módulos estén correctamente separados, que los endpoints expuestos sean testeables y que las decisiones de seguridad (RBAC, ABAC, MFA, manejo de tokens) estén reflejadas en el diseño. El artefacto principal es el documento de arquitectura y los diagramas C4 o UML correspondientes.

#### **Aceptación del producto**

**Revisión Conjunta Cliente-Desarrollador (Joint Review)** Revisión al cierre de cada sprint entre el equipo de desarrollo y el Product Owner o tutor del proyecto, en la que se demuestran las funcionalidades implementadas y se verifica el cumplimiento de los criterios de aceptación. Corresponde a la ceremonia de Sprint Review.

#### **Ciclo de vida del soporte del producto**

#### **Métricas del producto**

### **\*Quality Gates Definidos**

Los siguientes umbrales constituyen los criterios mínimos de calidad que deben cumplirse antes de considerar un entregable como aceptable para integración o despliegue:

### 

| Indicador | Umbral mínimo aceptable |
| :---- | :---- |
| Cobertura de Pruebas Unitarias | \>40% |
| Deuda Técnica | \< 2 días |
| Complejidad Ciclomática |  \< 50 |
| Severidad de Issues | Minor or Better |
| Vulnerabilidades Críticas | 0 |

Estos Quality Gates se configuran directamente en SonarCloud sobre el repositorio del proyecto en GitHub, y su cumplimiento es condición necesaria para la aprobación de cada sprint por parte del equipo de calidad.

### **\*Métricas de Producto**

Estas métricas se obtienen principalmente a través de SonarCloud y de los reportes de ejecución de pruebas, y son monitoreadas de forma continua desde el Sprint 2 en adelante.

| Métrica | Descripción | Umbral / Objetivo | Fuente |
| :---- | :---- | :---- | :---- |
| Cobertura de pruebas unitarias | Porcentaje de líneas o ramas del código backend cubiertas por pruebas unitarias automatizadas | \> 40% | SonarCloud |
| Deuda técnica | Tiempo estimado para corregir todos los code smells identificados en el análisis estático | \< 2 días | SonarCloud |
| Complejidad ciclomática | Número de caminos independientes a través del código fuente por método o clase | \< 50 por módulo | SonarCloud |
| Complejidad cognitiva | Medida de qué tan difícil es entender el flujo del código, según el modelo de SonarCloud | Seguimiento y reducción continua | SonarCloud |
| Vulnerabilidades críticas | Número de vulnerabilidades de seguridad clasificadas como críticas por el análisis estático | 0 | SonarCloud |
| Severidad de Issues | Clasificación del peor nivel de severidad de los issues abiertos en el análisis estático | Minor o mejor | SonarCloud |
| Densidad de bugs | Número de bugs detectados por SonarCloud por cada 1.000 líneas de código | Seguimiento sprint a sprint | SonarCloud |
| Tasa de paso de pruebas unitarias | Porcentaje de pruebas unitarias que pasan exitosamente sobre el total de pruebas implementadas | 100% en integración | Reporte de Pruebas |
| Tasa de paso de pruebas E2E | Porcentaje de escenarios Gherkin automatizados que pasan exitosamente | \> 90% al cierre del Sprint 3 | Reporte de automatización |

**Auditoría de Código Estático (SonarCloud)** Análisis automatizado del código fuente del backend para verificar el cumplimiento de los Quality Gates definidos en la Sección de Quality Gates Definidos. Se ejecuta de forma continua mediante el pipeline de CI/CD en GitHub Actions.

### **Aseguramiento del proceso**

### 

### **\- Revisión y auditoría**

Esta sección define los tipos de revisión y auditoría que se llevarán a cabo durante el proyecto, el cronograma asociado a los hitos de cada sprint, los responsables de cada actividad, los procedimientos para el reporte y resolución de problemas identificados, y las herramientas que se utilizarán

##### **Tipos de Revisión y Auditoría**

### **\- Gestión de la Configuración y Metodología de Versionamiento**

### **Justificación de Trunk-Based Development (TBD)**

El equipo adopta Trunk-Based Development (TBD) como metodología de versionamiento del código fuente. Esta decisión no es arbitraria: se fundamenta en tres razones directamente relacionadas con el contexto del proyecto.

**Primera razón — compatibilidad con integración continua:** TBD exige que los desarrolladores integren sus cambios al trunk (rama principal) al menos una vez al día. Esto es perfectamente compatible con el pipeline de CI/CD configurado en GitHub Actions, que ejecuta automáticamente el análisis de SonarCloud y las pruebas unitarias en cada push. Con una estrategia de ramas de larga duración (como GitFlow), los problemas de calidad se detectarían tardíamente, al momento del merge; con TBD, se detectan el mismo día que se introduce el código problemático.

**Segunda razón — reducción del riesgo de deuda de integración:** el proyecto tiene un plazo de tres sprints con 23 historias de usuario interdependientes (por ejemplo, HU001 → HU002 → HU003 → HU004 forman la ruta crítica del primer sprint). Con ramas de vida larga, los conflictos de integración entre módulos (Identidad, Transacciones, Auditoría) podrían acumularse y generar trabajo de resolución que comprometa la calidad del producto. TBD minimiza este riesgo al mantener el trunk siempre integrable.

**Tercera razón — validación continua de los Quality Gates:** la condición de que los 5 Quality Gates de SonarCloud estén en verde es un requisito de aprobación de cada sprint. Con TBD y el pipeline CI/CD, el estado de los Quality Gates es visible en tiempo real para todo el equipo. Si un commit rompe un Quality Gate, el equipo lo sabe en minutos, no en días.

### **Estrategias de Ramas**

La implementación de TBD en este proyecto sigue las siguientes convenciones:

- **Rama principal (trunk):** main. Es la única rama de larga duración. Siempre debe estar en estado integrable (compilable, con pruebas unitarias pasando y Quality Gates cumplidos).  
- **Feature branches de corta duración:** ramas de trabajo individuales con prefijo feat/, fix/ o chore/, con tiempo de vida máximo de 1–2 días antes de integrarse al trunk mediante pull request.  
- **Pull requests hacia main:** requieren aprobación de al menos un miembro del equipo (peer review) y que el pipeline de CI/CD sea completado sin errores, incluyendo el cumplimiento de los Quality Gates de SonarCloud.  
- **Release tags:** al cierre de cada sprint se etiqueta el commit de entrega en main con el formato vX.Y donde X es el número de sprint y Y es el número de release dentro del sprint.

No se usan ramas de larga duración como “develop”, “release” o “hotfix”. Esta simplificación reduce la carga de gestión de ramas y mantiene al equipo enfocado en integrar código de calidad frecuentemente en lugar de gestionar fusiones complejas.

### **Relación con el Aseguramiento de la Calidad**

Desde la perspectiva del aseguramiento de la calidad, el equipo de Calidad verifica que ningún código sea integrado a main sin haber pasado el pipeline CI/CD completo. El cumplimiento de los Quality Gates en SonarCloud es condición necesaria para aprobar un pull request. Esta regla es enfocada mediante la configuración de Branch Protection Rules en GitHub, que bloquean automáticamente los merges cuando el pipeline falla.

La gestión completa de la configuración (variables de entorno, secretos, versionado de APIs con OpenAPI/Swagger, política de Docker images) está definida en el Plan de Gestión de Configuración elaborado por el equipo de Arquitectura.

#### **Conformidad para el ciclo de vida de los procesos**

**Revisión de Requisitos (Requirements Review)** Tiene como objetivo verificar que las historias de usuario estén correctamente refinadas antes de ser incluidas en un sprint, con criterios de aceptación completos en lenguaje Gherkin, dependencias identificadas y estimación en story points asignada. Esta revisión corresponde a la verificación del cumplimiento del Definition of Ready (DoR). Los artefactos revisados son el backlog de HU, los criterios de aceptación Gherkin y las notas técnicas y de seguridad de cada HU.

**Revisión de Diseño de Pruebas (Test Design Review)** Verifica que los casos de prueba diseñados cubran adecuadamente los escenarios del caso feliz y las excepciones definidas en los criterios de aceptación Gherkin de cada HU. Incluye la revisión de la trazabilidad entre cada caso de prueba y su HU correspondiente.

**Auditoría de Proceso** Verifica que el equipo esté siguiendo los procesos acordados: uso del DoD y DoR, gestión del backlog en Azure DevOps, realización de ceremonias ágiles, registro de acciones de mejora derivadas de retrospectivas y cumplimiento de los acuerdos de equipo. Esta auditoría se realiza de forma interna al cierre de cada sprint.

#### **Conformidad con los ambientes**

#### **Conformidad de los procesos del subcontratista**

No aplica, debido a que el proyecto no contempla participación de proveedores externos o subcontratistas y se limita únicamente a la revisión por parte del equipo de Fábrica Escuela.

#### **Métricas del proceso**

Estas métricas evalúan la ejecución del proceso de calidad a lo largo de los sprints y son revisadas en las ceremonias de revisión y retrospectivas.

| Métrica | Descripción | Objetivo | Frecuencia de medición |
| :---- | :---- | :---- | :---- |
| HU con criterios Gherkin completos | Porcentaje de historias de usuario del sprint que tienen criterios de aceptación escritos y revisados en Gherkin antes del inicio del sprint | 100% de las HU del sprint | Al inicio de cada sprint |
| Casos de prueba diseñados vs. ejecutados | Número de casos de prueba diseñados comparado con los efectivamente ejecutados durante el sprint | \> 90% de ejecución sobre los diseños | Al cierre de cada sprint |
| Defectos encontrados por sprint | Número de defectos identificados durante la ejecución de pruebas en cada sprint | Seguimiento y tendencia decreciente | Al cierre de cada sprint |
| Defectos corregidos vs reportados | Porcentaje de defectos reportados que fueron corregidos y verificados dentro del mismo sprint | \> 80% de cierre en el sprint en que se reportan | Al cierre de cada sprint |
| Tiempo promedio de resolución de defectos | Tiempo promedio entre la apertura de un defecto y su cierre verificado | \< 3 días hábiles | Continua |
| Cumplimiento del DoD | Porcentaje de HU entregadas en el sprint que cumplen todos los criterios del Definition of Done al momento de su revisión | 100% | Al cierre de cada sprint |
| Quality Gates cumplidos | Número de Quality Gates de SonarCloud que se cumplen sobre el total definido (5 gates) | 5/5 a partir del Sprint 2 | Por cada análisis de SonarCloud |

#### **Conocimiento y habilidades del personal**

Para que el equipo pueda ejecutar efectivamente las actividades definidas en este plan, se identifican las siguientes necesidades de capacitación.

| Tema | Dirigido a | Momento sugerido | Modalidad |
| :---- | :---- | :---- | :---- |
| Escritura de criterios de aceptación en Gherkin (BDD) | Todo el equipo | Inicio del Sprint 1 | Sesión práctica conjunta |
| Configuración y uso de SonarCloud con GitHub Actions | Calidad \+ Arquitectura | Sprint 2 \- Semana 1 | Sesión práctica conjunta |
| Implementación de pruebas unitarias con JUnit 5 y Mockito (patrón AAA) | Arquitectura \+ Calidad | Sprint 2 \- Semana 1 | Sesión práctica conjunta |
| Automatización de pruebas con Cucumber en Spring Boot | Equipo de Calidad | Sprint 3 \- Semana 1 | Autoestudio |
| Pruebas de integración con RestAssured y Docker Compose | Calidad \+ Arquitectura | Sprint 2 \- Semana 2 | Sesión práctica conjunta |
| Uso de Azure DevOps para registro y seguimiento de defectos | Todo el equipo | Inicio del Sprint 1 | Inducción liderada por Scrum Master |

Las capacitaciones de tipo práctico se realizarán en sesiones de trabajo conjunto entre los equipos involucrados, aprovechando los espacios de sincronización del proyecto. No se requiere contratación de capacitación externa; el conocimiento necesario está distribuido entre los integrantes del equipo y complementado con la documentación oficial de cada herramienta.

## 

## **Consideraciones adicionales**

#### **Revisión del contrato**

#### **Métricas de calidad del proyecto**

Estas métricas son gestionadas principalmente por el equipo de Gestión de Proyectos en Azure DevOps, pero el equipo de Calidad las consume para correlacionar el avance del proyecto con el estado de la calidad.

| Métrica | Descripción | Objetivo | Fuente |
| :---- | :---- | :---- | :---- |
| Burndown Chart del proyecto | Seguimiento del trabajo restante vs el tiempo disponible a nivel de proyecto completo | Tendencia decreciente sostenida | Azure DevOps |
| Burndown Chart por sprint | Seguimiento del trabajo restante dentro de cada sprint | Completar el 100% del sprint backlog comprometido | Azure DevOps |
| Velocidad del equipo | Story points completados por sprint, incluyendo las HU que superaron los quality gates de calidad | Estabilización o crecimiento sprint a sprint  | Azure DevOps |
| Story Points de calidad comprometidos vs completados | Story points de las tareas de calidad (pruebas, análisis, automatización) comprometidas en el sprint backlog vs las efectivamente completadas | \> 90% de cumplimiento | Azure DevOps |
| Índice de retrabajo | Proporción de HU que regresaron al estado “en proceso” después de haberse marcado como terminadas, por defectos encontrados en revisión o pruebas | \< 15% del total de HU del sprint | Azure DevOps |

**Revisión de Cierre del Proyecto (Post-mortem)** Al finalizar el Sprint 3, se realiza una revisión integral del proyecto que incluye el análisis del cumplimiento de los objetivos de calidad, las lecciones aprendidas, el análisis comparativo del instrumento SAMM (inicio vs. cierre) y las recomendaciones para proyectos futuros.

#### **Renuncias y desviaciones**

#### **Repetición de tareas**

#### **Riesgos al realizar el proceso de aseguramiento de la calidad**

La gestión integral de riesgos del proyecto está definida en el Plan de Gestión de Riesgos, documento elaborado por el equipo de Gestión de Proyectos. Desde la perspectiva del aseguramiento de la calidad, se identifican los siguientes riesgos específicos que podrían afectar el cumplimiento de los objetivos de calidad del proyecto:

| Riesgo | Probabilidad | Impacto | Estrategia de Mitigación |
| :---- | :---- | :---- | :---- |
| Cobertura de pruebas unitarias por debajo del 40% al cierre del Sprint 2 | Media | Alto | Iniciar la escritura de pruebas unitarias desde el Sprint 1 en paralelo con el desarrollo; monitorear la cobertura semanalmente desde la configuración de SonarCloud |
| Incumplimiento de Quality Gates en SonarCloud que bloquee la integración | Media | Alto | Revisar el reporte de SonarCloud después de cada integración; asignar tiempo explícito en el sprint backlog para reducción de deuda técnica y complejidad |
| Criterios de aceptación Gherkin incompletos o ambiguos al inicio de un sprint | Alta | Medio | Aplicar el DoR de forma estricta en el Sprint Planning; el equipo de Calidad revisa y aprueba todos los criterios Gherkin antes de iniciar el sprint |
| Retraso en la automatización de pruebas E2E del Sprint 3 por complejidad técnica | Media | Alto | Iniciar el prototipado de la infraestructura de automatización con Cucumber desde el Sprint 2; priorizar la automatización de los escenarios más críticos (HU001, HU003, HU005) |
| Defectos críticos encontrados en revisión tardía que impidan el despliegue | Baja | Crítico | Ejecutar revisiones de código y pruebas de integración de forma continua durante el sprint, no solo al final; no acumular deuda de pruebas |
| Falta de disponibilidad del entorno de pruebas de integración (Docker Compose) | Media | Medio | Documentar el proceso de levantamiento del entorno desde el Sprint 1; cada miembro del equipo de Calidad debe poder levantarlo de forma independiente |
| Rotación o ausencia temporal de un integrante del equipo de Calidad | Media | Medio | Asegurar que el conocimiento sobre las herramientas y procesos de calidad esté distribuido entre los cuatro integrantes del equipo; documentar los procedimientos en el repositorio |

Cualquier riesgo materializado que afecte los objetivos de calidad debe ser reportado al equipo de Gestión de Proyectos de forma inmediata para su registro en Azure DevOps y la definición de acciones de contingencia.

#### **Estrategias de comunicación**

#### **Proceso de no conformidad**

##### **Resolución de problemas y acciones correctivas**

Cuando se identifique un problema durante una revisión o auditoría, ya sea un defecto funcional, un incumplimiento de Quality Gate, una desviación del proceso o un hallazgo de seguridad, se seguirá el siguiente procedimiento:

El problema se registra como un ítem en Azure DevOps, con descripción clara, sprint en que se detectó, artefacto afectado, severidad estimada y responsable asignado para su corrección. La severidad se clasifica en tres niveles: crítico (bloquea la integración o el despliegue y debe resolverse en el mismo sprint), mayor (afecta funcionalidad o calidad significativamente y debe resolverse antes del cierre del sprint) y menor (mejora o ajuste que puede planificarse en el sprint siguiente).

El responsable asignado analiza la causa raíz del problema y propone una acción correctiva. El equipo de Calidad verifica que la corrección resuelve efectivamente el problema antes de cerrar el ítem. Las acciones correctivas y sus resultados quedan documentados como insumo para la retrospectiva del sprint y para el informe de calidad. Los problemas no resueltos al cierre de un sprint deben ser escalados al equipo de Gestión para su replanificación explícita en el sprint siguiente.

##### **Resolución de Problemas y Medidas Correctivas**

La gestión de problemas identificados durante las actividades de aseguramiento de la calidad se rige por el proceso descrito en la Sección de Resolución de Problemas y Acciones Correctivas de este documento. El detalle completo del procedimiento de resolución de problemas, incluyendo flujos de escalamiento, plantillas de reporte y criterios de priorización, se encuentra en el Plan de Resolución de Problemas, documento que será elaborado por el equipo de Gestión en coordinación con el equipo de Calidad.

Como principio general, todo defecto o hallazgo identificado durante una prueba, revisión o auditoría debe ser registrado en Azure DevOps antes del cierre de la jornada en que se detecta. Ningún defecto de severidad crítica puede quedar abierto al momento de realizar la Sprint Review. El equipo de Calidad es responsable de verificar que las correcciones aplicadas resuelven efectivamente el problema reportado antes de cerrar el ítem correspondiente.

## **Registros de Calidad**

#### **Análisis, identificación, recopilación, archivo, mantenimiento y disposición de registros**

Los siguientes documentos deben ser producidos durante el proyecto para garantizar la trazabilidad del proceso de calidad y el cumplimiento de los requisitos del programa:

| Documento | Sprint de Entrega | Responsable |
| :---- | :---- | :---- |
| Plan de Aseguramiento de la Calidad (PAC) (Este documento) | Sprint 1 | Calidad |
| Plan de Pruebas | Sprint 1 | Calidad |
| Casos de Pruebas Sprint 1 | Sprint 1 | Calidad |
| HU refinadas en Gerkin \- Sprint 1 | Sprint 1 | Calidad |
| HU refinadas en Gerkin \- Sprint 2 | Sprint 2 | Calidad |
| Casos de Pruebas Sprint 1 y 2 con ejecución | Sprint 2 | Calidad |
| Reporte Análisis Estático (SonarCloud) | Sprint 2 | Calidad |
| Reporte de cobertura de pruebas unitarias | Sprint 2 | Calidad |
| Reporte de complejidad ciclomática y cognitiva | Sprint 2 | Calidad |
| Reporte de deuda técnica | Sprint 2 | Calidad |
| Pruebas de Aceptación automatizadas (E2E) | Sprint 3 | Calidad |
| Informe de cierre de calidad | Sprint 3 | Calidad |
| Software Development Plan (SDP) | Sprint 1 | Gestión \+ Arquitectura |
| Documento de Arquitectura | Sprint 1 | Arquitectura |
| Plan de Releases  | Sprint 2 | Gestión |
| Análisis SAMM (inicio y cierre) | Sprint 1 & 3 | Gestión  |
| Definition of Done (DoD) y Definition of Ready (DoR) | Sprint 1 | Gestión |

La adecuación y completitud de estos documentos será evaluada en las revisiones conjuntas programadas al cierre de cada sprint.

Los registros de calidad son los artefactos que evidencian la ejecución de las actividades de aseguramiento de la calidad y el estado del producto en cada momento del proyecto. Los siguientes registros serán mantenidos durante el proyecto:

| Registro | Descripción | Almacenamiento | Periodo de retención |
| :---- | :---- | :---- | :---- |
| Casos de prueba y resultados de ejecución | Documento con los casos de prueba diseñados y los resultados de cada ejecución (pasó / falló / bloqueado) | Repositorio GitHub — carpeta /docs/quality | Duración del proyecto |
| Registro de defectos | Ítems de Azure DevOps correspondientes a defectos reportados, con estado, severidad, responsable y resolución | Azure DevOps | Duración del proyecto |
| Actas de revisión y auditoría | Registro escrito de cada revisión y auditoría realizada, con hallazgos, acuerdos y acciones | Repositorio GitHub — carpeta /docs/quality/reviews | Duración del proyecto |
| Reportes de pruebas automatizadas Cucumber | Reportes HTML generados por Cucumber tras cada ejecución de pruebas de aceptación | Repositorio GitHub — carpeta /docs/quality/e2e | Duración del proyecto |

Todos los registros deben ser accesibles para el tutor del programa y para cualquier miembro del equipo en cualquier momento del proyecto. La carpeta /docs/quality en el repositorio GitHub es el repositorio central de evidencias de calidad del proyecto.

#### **Disponibilidad de los registros**

### **Recolección y Reporte de Métricas**

Las métricas de producto se recolectarán de forma automática mediante los análisis periódicos de SonarCloud integrados al pipeline de CI/CD en GitHub Actions, configurado desde el Sprint 2\. Las métricas de proceso se consolidarán manualmente por el equipo de Calidad al cierre de cada sprint, como parte del informe de calidad del sprint. Las métricas de proyecto serán mantenidas por el equipo de Gestión en el tablero de Azure DevOps y compartidas con todo el equipo en la reunión de revisión de sprint.

Al cierre del proyecto, el equipo de Calidad elaborará un informe consolidado que presente la evolución de las métricas clave a lo largo de los tres sprints, identificando tendencias, logros y oportunidades de mejora para proyectos futuros.

| Registro | Descripción | Almacenamiento | Periodo de retención |
| :---- | :---- | :---- | :---- |
| Reportes de SonarCloud | Capturas y exportaciones de los reportes de análisis estático por sprint | Repositorio GitHub — carpeta /docs/quality/sonar | Duración del proyecto |
| Informe de calidad por sprint | Documento consolidado con el estado de métricas, defectos y acciones correctivas al cierre de cada sprint | Repositorio GitHub — carpeta /docs/quality | Duración del proyecto |
| Informe de cierre de calidad | Informe final con análisis comparativo de métricas, lecciones aprendidas y recomendaciones | Repositorio GitHub — carpeta /docs/quality | Duración del proyecto |

## 

