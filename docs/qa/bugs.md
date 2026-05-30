# Registro de Bugs — QA

**Estado actual (2026-05-30):** 13 bugs abiertos detectados por ejecución de pruebas Serenity BDD (20 tests, 13 fallas).
**Nota:** Todas las fallas corresponden a funcionalidad no implementada en el backend. Las pruebas (features y step definitions) están correctamente escritas según los criterios de aceptación; el backend no expone los endpoints, filtros o formatos esperados.

## Plantilla para nuevo bug

- **Título:**
- **HU(s) relacionadas:**
- **Componente:** (API / Servicio / Repository / Filter / etc.)
- **Prioridad:** (P0/Critical, P1/High, P2/Medium, P3/Low)
- **Severidad:** (Blocker, Major, Minor)
- **Estado:** Open
- **Reportado por (Reporter):**
- **Entorno (Environment):** (local / CI / staging / commit SHA)
- **Reproducible:** (Sí / No)
- **ID Issue / Work Item (enlace):** (Azure DevOps work item o GitHub Issue/PR)
- **Descripción breve:**
- **Pasos para reproducir:**
  1.
  2.
- **Datos de prueba (payload / headers / idempotency key):**
- **Resultado esperado:**
- **Resultado actual:**
- **Logs / Stacktrace:** (incluir fragmento o enlace a `target/surefire-reports/...`)
- **Test asociado (si aplica):** (nombre del test)
- **Archivo de test:** (ruta en repo, p.ej. src/test/java/...)
- **Adjuntos:** capturas, dumps, respuestas HTTP, enlaces a reportes (JaCoCo / Surefire)
- **Asignado a:**
- **Fecha detectada:**
- **Resolución / Comentarios de cierre:**
- **Almacenamiento y retención:** (ubicación del registro, p.ej. Azure DevOps / docs/quality, periodo de retención según PAC)

> Nota: El PAC establece que el registro de defectos debe mantenerse en Azure DevOps (preferible) y que todos los registros deben ser accesibles durante la duración del proyecto. Si se utiliza GitHub Issues, incluya el enlace y anote la ubicación de copia/registro en Azure o en `/docs/quality`.

## Proceso recomendado al detectar un fallo en pruebas automáticas

1. Crear un Work Item en **Azure DevOps** (preferible). Si no es posible, crear un **GitHub Issue** y añadir el campo `ID Issue / Work Item` con el enlace al issue; notificar al QA Lead para que se registre el work item en ADO.
2. Rellenar la plantilla anterior con todos los campos obligatorios y adjuntar `target/surefire-reports/<file>.txt`, `target/site/jacoco/index.html` y fragmentos de stacktrace.
3. Etiquetar con `bug`, `test-failure` y la HU correspondiente (`HU00X`). Añadir etiqueta `ado-mapped: true|false` según corresponda.
4. Enlazar el issue/work item desde `docs/qa/traceability-matrix-sprint1.md` en la columna *Bugs* y anotar el responsable y la sprint prevista para la corrección.
5. Actualizar el work item con estado, prioridad y resolución; conservar el registro en Azure DevOps o en la carpeta `/docs/quality/defects/` del repositorio para auditoría y retención.

## Ejemplo: Bug HU005 (MFA — Email OTP)

- **Título:** HU005 — MFA Email OTP: falta cobertura unitaria/E2E para orquestación OTP
- **HU(s) relacionadas:** HU005
- **Componente:** Módulo de Identidad / Servicio MFA (orquestación de envío y verificación OTP)
- **Prioridad:** P1/High
- **Severidad:** Major
- **Estado:** Open
- **Reportado por (Reporter):** QA Automation
- **Entorno (Environment):** CI (mvnw -B test) — commit: <commit-sha>
- **Reproducible:** Sí
- **ID Issue / Work Item (enlace):** (pendiente) 
- **Descripción breve:** No se encontraron pruebas unitarias ni E2E que verifiquen el flujo de challenge/verify del OTP por email; gap detectado en matriz de trazabilidad y cobertura.
- **Pasos para reproducir:**
  1. Ejecutar suite de tests `./mvnw -B test`.
  2. Revisar `traceability-matrix-sprint1.md` y buscar HU005 — comprobar que no hay test asociado.
  3. Revisar `target/site/jacoco/index.html` para zonas no cubiertas.
- **Datos de prueba (payload):** { "email": "test@example.com", "action": "challenge" }
- **Resultado esperado:** Existencia de pruebas unitarias que simulan envío y validación de OTP; E2E que validen flujo completo.
- **Resultado actual:** No existen tests específicos; cobertura insuficiente en flujo MFA.
- **Logs / Stacktrace:** N/A (issue de ausencia de pruebas)
- **Test asociado (si aplica):** Ninguno
- **Archivo de test:** -
- **Adjuntos:** target/site/jacoco/index.html (captura), enlace a [docs/qa/traceability-matrix-sprint1.md](traceability-matrix-sprint1.md)
- **Asignado a:** TBD
- **Fecha detectada:** 2026-05-13
- **Resolución / Comentarios de cierre:** Pendiente de implementación de tests unitarios y E2E; abrir PR con tests y actualizar matriz.
- **Almacenamiento y retención:** Registrar Work Item en Azure DevOps y conservarla durante la duración del proyecto (ver PAC).

## Bugs Registrados

---

### BUG-001: HU006 CP-S2-001 — ID de transacción en formato UUID en vez de patrón `txn_*`

- **Título:** HU006 — ID de transacción generado como UUID en vez de `txn_*`
- **HU(s) relacionadas:** HU003, HU006
- **Componente:** Transactions / Domain / ID Generation
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Reportado por (Reporter):** QA Automation — Serenity BDD
- **Entorno (Environment):** local (`mvn clean test -Dtest=SerenityTestRunner`) — commit: pendiente
- **Reproducible:** Sí
- **ID Issue / Work Item (enlace):** (pendiente)
- **Descripción breve:** El backend genera IDs de transacción con formato UUID (`7e6dc0e0-e81b-459b-ad80-61d1d6d42f22`) en lugar de seguir el patrón `txn_*` especificado en los criterios de aceptación.
- **Pasos para reproducir:**
  1. Ejecutar `mvn clean test -Dtest=SerenityTestRunner`.
  2. El escenario @HU006 @CP-S2-001 @alegria ejecuta POST `/api/v1/transactions` con credenciales válidas.
  3. La respuesta HTTP 201 incluye `id: "7e6dc0e0-e81b-459b-ad80-61d1d6d42f22"`.
  4. La aserción `elCampoDebeCoincidirConPatron("id", "txn_*")` falla.
- **Datos de prueba (payload / headers / idempotency key):**
  ```json
  {"merchantId": "{merchantId}", "amount": 25000}
  ```
  Headers: `X-Merchant-Id`, `X-Public-Id`, `X-Secret`
- **Resultado esperado:** El campo `id` debe coincidir con el patrón `txn_*` (ej: `txn_live_a1b2c3d4`).
- **Resultado actual:** El campo `id` es un UUID v4: `7e6dc0e0-e81b-459b-ad80-61d1d6d42f22`.
- **Logs / Stacktrace:**
  ```
  org.opentest4j.AssertionFailedError: El campo 'id' con valor '7e6dc0e0-e81b-459b-ad80-61d1d6d42f22' debería coincidir con 'txn_*' ==> expected: <true> but was: <false>
  at TransactionSteps.elCampoDebeCoincidirConPatron(TransactionSteps.java:201)
  ```
- **Test asociado (si aplica):** SerenityTestRunner.Transacción exitosa con credenciales válidas
- **Archivo de test:** `src/test/resources/features/HU006_validacion_credenciales.feature:19`
- **Adjuntos:** `target/surefire-reports/`
- **Asignado a:** TBD
- **Fecha detectada:** 2026-05-30
- **Resolución / Comentarios de cierre:** (pendiente)
- **Almacenamiento y retención:** Según PAC

---

### BUG-002: HU007 CP-S2-005 — GET /merchant-portal/profile devuelve 403 en vez de 200

- **Título:** HU007 — Consulta de perfil propio devuelve 403 (CredentialValidationFilter bloquea)
- **HU(s) relacionadas:** HU007
- **Componente:** Security / CredentialValidationFilter
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Reportado por (Reporter):** QA Automation — Serenity BDD
- **Entorno (Environment):** local
- **Reproducible:** Sí
- **ID Issue / Work Item (enlace):** (pendiente)
- **Descripción breve:** El endpoint `GET /api/v1/merchant-portal/profile` devuelve HTTP 403 (Forbidden) en lugar de 200 OK cuando se invoca con credenciales válidas. El `CredentialValidationFilter` bloquea la petición antes de que llegue al controlador.
- **Pasos para reproducir:**
  1. Ejecutar `mvn clean test -Dtest=SerenityTestRunner`.
  2. El escenario @HU007 @CP-S2-005 @alegria ejecuta autenticación con credenciales activas.
  3. Envía GET `/api/v1/merchant-portal/profile`.
  4. La respuesta es HTTP 403 con body vacío.
- **Datos de prueba:** Headers: `X-Merchant-Id`, `X-Public-Id`, `X-Secret`, CSRF cookie + header.
- **Resultado esperado:** HTTP 200 OK con perfil del comercio (businessName, email, status).
- **Resultado actual:** HTTP 403 Forbidden, body vacío.
- **Logs / Stacktrace:**
  ```
  org.opentest4j.AssertionFailedError: Código de estado esperado: 200 pero fue: 403 | Body:
  at TransactionSteps.laRespuestaDebeTenerCodigo(TransactionSteps.java:167)
  ```
- **Test asociado:** SerenityTestRunner.Consulta exitosa del propio perfil
- **Archivo de test:** `src/test/resources/features/HU007_consulta_perfil.feature:18`
- **Adjuntos:** `target/surefire-reports/`
- **Asignado a:** TBD
- **Fecha detectada:** 2026-05-30
- **Resolución / Comentarios de cierre:** (pendiente)
- **Almacenamiento y retención:** Según PAC

---

### BUG-003: HU007 CP-S2-006 — GET /merchant-portal/profile sin auth devuelve 403 en vez de 401

- **Título:** HU007 — Rechazo sin autenticación devuelve 403 genérico en vez de 401
- **HU(s) relacionadas:** HU007
- **Componente:** Security / CredentialValidationFilter
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Reportado por (Reporter):** QA Automation
- **Entorno (Environment):** local
- **Reproducible:** Sí
- **ID Issue / Work Item (enlace):** (pendiente)
- **Descripción breve:** El endpoint `GET /api/v1/merchant-portal/profile` sin autenticación devuelve HTTP 403 (Forbidden) en lugar de 401 (Unauthorized). El código de estado 401 es el estándar para indicar que se requiere autenticación.
- **Pasos para reproducir:**
  1. Ejecutar pruebas Serenity.
  2. Escenario @HU007 @CP-S2-006 @seguridad: comercio no autenticado.
  3. Envía GET `/api/v1/merchant-portal/profile` sin headers de autenticación.
  4. Respuesta: HTTP 403 en vez de 401.
- **Datos de prueba:** Sin headers de autenticación.
- **Resultado esperado:** HTTP 401 Unauthorized.
- **Resultado actual:** HTTP 403 Forbidden, body vacío.
- **Logs / Stacktrace:**
  ```
  org.opentest4j.AssertionFailedError: Código de estado esperado: 401 pero fue: 403 | Body:
  ```
- **Test asociado:** SerenityTestRunner.Rechazo por falta de autenticación
- **Archivo de test:** `src/test/resources/features/HU007_consulta_perfil.feature:27`
- **Asignado a:** TBD
- **Fecha detectada:** 2026-05-30

---

### BUG-004: HU008 CP-S2-007 — PATCH /merchant-portal/profile devuelve 403 en vez de 200

- **Título:** HU008 — Actualización de perfil devuelve 403 en vez de 200
- **HU(s) relacionadas:** HU008
- **Componente:** Security / CredentialValidationFilter
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** El endpoint `PATCH /api/v1/merchant-portal/profile` devuelve HTTP 403 antes de llegar al controlador. El `CredentialValidationFilter` no reconoce la ruta y la bloquea.
- **Pasos para reproducir:**
  1. Autenticarse con credenciales activas.
  2. Enviar PATCH `/api/v1/merchant-portal/profile` con payload `{"businessName": "Mi Tienda Actualizada", "contactPhone": "+573001234567"}`.
  3. Respuesta: HTTP 403 en vez de 200.
- **Resultado esperado:** HTTP 200 OK con perfil actualizado.
- **Resultado actual:** HTTP 403 Forbidden.
- **Logs / Stacktrace:**
  ```
  org.opentest4j.AssertionFailedError: Código de estado esperado: 200 pero fue: 403 | Body:
  ```
- **Test asociado:** SerenityTestRunner.Actualización exitosa de datos permitidos
- **Archivo de test:** `src/test/resources/features/HU008_actualizacion_perfil.feature:18`
- **Fecha detectada:** 2026-05-30

---

### BUG-005: HU008 CP-S2-008 — PATCH profile con email diferente devuelve 403 en vez de 400

- **Título:** HU008 — Validación de campos inmutables no alcanza por 403 del filter
- **HU(s) relacionadas:** HU008
- **Componente:** Security / CredentialValidationFilter / MerchantPortalController
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** Al intentar cambiar el email (campo inmutable) en PATCH `/api/v1/merchant-portal/profile`, el `CredentialValidationFilter` bloquea con 403 antes de que la validación de negocio (400) ocurra.
- **Pasos para reproducir:**
  1. Autenticarse con credenciales activas.
  2. Enviar PATCH `/api/v1/merchant-portal/profile` con email diferente.
  3. Respuesta: HTTP 403 en vez de 400 + errorCode VALIDATION_ERROR.
- **Resultado esperado:** HTTP 400 Bad Request, errorCode: VALIDATION_ERROR.
- **Resultado actual:** HTTP 403 Forbidden.
- **Test asociado:** SerenityTestRunner.Rechazo al intentar cambiar email
- **Archivo de test:** `src/test/resources/features/HU008_actualizacion_perfil.feature:31`
- **Fecha detectada:** 2026-05-30

---

### BUG-006: HU012 CP-S2-014 — PATCH /transactions/{id}/complete (APROBADO) devuelve 403

- **Título:** HU012 — Endpoint PATCH /transactions/{id}/complete no implementado o bloqueado
- **HU(s) relacionadas:** HU011, HU012
- **Componente:** Transactions / API + Security / CredentialValidationFilter
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** El endpoint `PATCH /api/v1/transactions/{transactionId}/complete` no está disponible. El `CredentialValidationFilter` o la falta de ruta en el controlador causa HTTP 403.
- **Pasos para reproducir:**
  1. Crear transacción en estado CREATED.
  2. Enviar PATCH `/api/v1/transactions/{id}/complete` con `{"result": "APPROVED", "authorizationCode": "AUTH-12345"}`.
  3. Respuesta: HTTP 403 en vez de 200.
- **Resultado esperado:** HTTP 200 OK, status=COMPLETED, result=APPROVED.
- **Resultado actual:** HTTP 403 Forbidden.
- **Test asociado:** SerenityTestRunner.Pago aprobado exitosamente
- **Archivo de test:** `src/test/resources/features/HU012_resultado_pago.feature:19`
- **Fecha detectada:** 2026-05-30

---

### BUG-007: HU012 CP-S2-015 — PATCH /transactions/{id}/complete (RECHAZADO) devuelve 403

- **Título:** HU012 — Mismo bug que BUG-006: endpoint no implementado para transacción rechazada
- **HU(s) relacionadas:** HU011, HU012
- **Componente:** Transactions / API
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** Idéntico a BUG-006. El endpoint `PATCH /api/v1/transactions/{id}/complete` con resultado REJECTED también devuelve 403.
- **Resultado esperado:** HTTP 200 OK, status=COMPLETED, result=REJECTED.
- **Resultado actual:** HTTP 403 Forbidden.
- **Test asociado:** SerenityTestRunner.Pago rechazado por fondos insuficientes
- **Archivo de test:** `src/test/resources/features/HU012_resultado_pago.feature:33`
- **Fecha detectada:** 2026-05-30

---

### BUG-008: HU012 CP-S2-016 — PATCH /transactions/{id}/complete (ya completada) devuelve 403

- **Título:** HU012 — Validación de estado terminal no implementada (409 en vez de 403)
- **HU(s) relacionadas:** HU011, HU012
- **Componente:** Transactions / API + State Machine
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** Al intentar completar una transacción ya completada, el sistema debe devolver 409 Conflict con errorCode BUSINESS_RULE_VIOLATION, pero al no existir el endpoint se obtiene 403.
- **Resultado esperado:** HTTP 409 Conflict, errorCode: BUSINESS_RULE_VIOLATION.
- **Resultado actual:** HTTP 403 Forbidden.
- **Test asociado:** SerenityTestRunner.Intento de completar transacción ya completada
- **Archivo de test:** `src/test/resources/features/HU012_resultado_pago.feature:47`
- **Fecha detectada:** 2026-05-30

---

### BUG-009: HU014 CP-S2-019 — Campo `action` es null en eventos de auditoría de seguridad

- **Título:** HU014 — Auditoría de seguridad: campo `action` no se persiste (null)
- **HU(s) relacionadas:** HU013, HU014
- **Componente:** Audit / Observability
- **Prioridad:** P1/High
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** Cuando se registra un evento de auditoría por intento de acceso no autorizado (credenciales falsas), el campo `action` es `null`. El backend no persiste el tipo de acción en los eventos de auditoría de seguridad.
- **Pasos para reproducir:**
  1. Autenticarse como comercio verificado.
  2. Enviar POST `/api/v1/transactions` con credenciales falsas.
  3. La respuesta es 401 (correcto).
  4. Consultar el evento de auditoría — el campo `action` es `null`.
- **Resultado esperado:** El campo `action` debe ser `"ACCESS_DENIED"`.
- **Resultado actual:** El campo `action` es `null`.
- **Logs / Stacktrace:**
  ```
  org.opentest4j.AssertionFailedError: El campo 'action' no debe ser nulo ==> expected: not <null>
  at TransactionSteps.elCampoDebeSer(TransactionSteps.java:179)
  ```
- **Test asociado:** SerenityTestRunner.Auditoría de intento de acceso no autorizado
- **Archivo de test:** `src/test/resources/features/HU014_auditoria.feature:25`
- **Fecha detectada:** 2026-05-30

---

### BUG-010: HU014 CP-S2-020 — Escenario de integridad de auditoría no tiene estado previo preparado

- **Título:** HU014 — Escenario de integridad de auditoría requiere comercio y credenciales activas en Background
- **HU(s) relacionadas:** HU013, HU014
- **Componente:** Audit / Test feature setup
- **Prioridad:** P3/Low
- **Severidad:** Minor
- **Estado:** Open
- **Descripción breve:** El escenario CP-S2-020 (Integridad de eventos de auditoría) no prepara comercio ni credenciales en el Background. El step `seRealizanMultiplesOperaciones()` llama a `seCreaUnaTransaccionExitosamente()` que requiere `merchantId` y `publicId`, lanzando `fail("Se requiere comercio y credenciales activas")`. El Background del feature necesita incluir `Given un comercio verificado con credenciales activas`.
- **Pasos para reproducir:**
  1. Ejecutar escenario @HU014 @CP-S2-020 @borde.
  2. Background: solo `Given el sistema de pagos está listo` + `Given el sistema de auditoría activo`.
  3. `When se realizan múltiples operaciones` → `seCreaUnaTransaccionExitosamente()` → fail porque no hay merchantId.
- **Resultado esperado:** El escenario debe ejecutarse sin fallos — crear transacciones y verificar inmutabilidad.
- **Resultado actual:** `fail("Se requiere comercio y credenciales activas")`.
- **Nota:** Puede resolverse agregando `Given un comercio verificado con credenciales activas` al Background del feature.
- **Test asociado:** SerenityTestRunner.Integridad de eventos de auditoría
- **Archivo de test:** `src/test/resources/features/HU014_auditoria.feature:32`
- **Fecha detectada:** 2026-05-30

---

### BUG-011: HU015 CP-S2-021 — GET /api/v1/transactions devuelve 403 (listado paginado)

- **Título:** HU015 — Endpoint GET /api/v1/transactions no implementado o bloqueado (listado paginado)
- **HU(s) relacionadas:** HU015
- **Componente:** Transactions / API + Security / CredentialValidationFilter
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** El endpoint `GET /api/v1/transactions` para listado paginado no está implementado o el `CredentialValidationFilter` lo bloquea. Se recibe HTTP 403 y además el log muestra `HttpRequestMethodNotSupportedException: Request method 'GET' is not supported`.
- **Pasos para reproducir:**
  1. Autenticarse con credenciales activas.
  2. Crear transacciones para el comercio.
  3. Enviar GET `/api/v1/transactions?page=0&size=10`.
  4. Respuesta: HTTP 403, body vacío.
- **Resultado esperado:** HTTP 200 OK con lista paginada de transacciones (content, page, size, totalElements).
- **Resultado actual:** HTTP 403 Forbidden + `HttpRequestMethodNotSupportedException: Request method 'GET' is not supported`.
- **Logs / Stacktrace:**
  ```
  WARN .w.s.m.s.DefaultHandlerExceptionResolver: Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' is not supported]
  ```
- **Test asociado:** SerenityTestRunner.Listado paginado de transacciones
- **Archivo de test:** `src/test/resources/features/HU015_listado_pagos.feature:18`
- **Fecha detectada:** 2026-05-30

---

### BUG-012: HU015 CP-S2-022 — GET /api/v1/transactions?status=CREATED devuelve 403

- **Título:** HU015 — Filtrado por estado no disponible (mismo bug que BUG-011)
- **HU(s) relacionadas:** HU015
- **Componente:** Transactions / API
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** Misma causa que BUG-011: el endpoint GET `/api/v1/transactions` no está implementado, por lo que el filtrado por estado también falla con 403.
- **Resultado esperado:** HTTP 200 OK con transacciones filtradas por status=CREATED.
- **Resultado actual:** HTTP 403 Forbidden.
- **Test asociado:** SerenityTestRunner.Filtrado por estado de transacción
- **Archivo de test:** `src/test/resources/features/HU015_listado_pagos.feature:27`
- **Fecha detectada:** 2026-05-30

---

### BUG-013: HU015 CP-S2-023 — GET /api/v1/transactions devuelve 403 (aislamiento entre comercios)

- **Título:** HU015 — Aislamiento de datos entre comercios no validable por falta de endpoint
- **HU(s) relacionadas:** HU015
- **Componente:** Transactions / API
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** No se puede validar el aislamiento de datos entre comercios porque el endpoint GET `/api/v1/transactions` no está implementado (HTTP 403). La prueba espera poder consultar transacciones y verificar que no se filtran datos de otros comercios.
- **Resultado esperado:** HTTP 200 OK con transacciones solo del comercio autenticado.
- **Resultado actual:** HTTP 403 Forbidden — `"No se pudo validar aislamiento: respuesta vacía (código 403)"`.
- **Test asociado:** SerenityTestRunner.Aislamiento de datos entre comercios
- **Archivo de test:** `src/test/resources/features/HU015_listado_pagos.feature:35`
- **Fecha detectada:** 2026-05-30
