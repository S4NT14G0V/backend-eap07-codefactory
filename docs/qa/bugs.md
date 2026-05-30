# Registro de Bugs — QA

**Estado actual (2026-05-30):** 7 bugs abiertos detectados por ejecución de pruebas Serenity BDD (11 tests, 7 fallas).
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

### BUG-002: HU007 CP-S2-004 — GET /merchant-portal/profile devuelve 403 en vez de 200

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
  2. El escenario @HU007 @CP-S2-004 @camino-feliz ejecuta autenticación con credenciales activas.
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

### BUG-004: HU008 CP-S2-006 — PATCH /merchant-portal/profile devuelve 403 en vez de 200

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
- **Almacenamiento y retención:** Según PAC

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

### BUG-013: HU015 CP-S2-021 — GET /api/v1/transactions devuelve 403 (aislamiento entre comercios)

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
- **Archivo de test:** `src/test/resources/features/HU015_listado_pagos.feature:27`
- **Fecha detectada:** 2026-05-30
