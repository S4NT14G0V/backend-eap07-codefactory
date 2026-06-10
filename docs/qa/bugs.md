# Registro de Bugs — QA

**Estado actual (2026-06-10):** 12 bugs abiertos detectados por ejecución de pruebas Serenity BDD (38 tests, 12 fallas).
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

## Bugs Abiertos

---

### BUG-014: HU006 — Credenciales sin permiso no generan error

- **Título:** HU006 — Backend no valida scopes de credenciales (permisos insuficientes)
- **HU(s) relacionadas:** HU006
- **Componente:** Security / CredentialValidationFilter
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Reportado por (Reporter):** QA Automation — Serenity BDD
- **Entorno (Environment):** local — commit: pendiente
- **Reproducible:** Sí
- **ID Issue / Work Item (enlace):** (pendiente)
- **Descripción breve:** El backend no permite crear credenciales con permisos específicos (ej: solo READ). Todas las credenciales se crean con permiso PAYMENTS, por lo que no se puede probar el escenario de "credenciales sin permiso para transaccionar".
- **Pasos para reproducir:**
  1. Ejecutar `mvn test -Dtest=SerenityTestRunner`.
  2. Escenario @HU006 @error "Solicitud de pago no exitosa debido a permisos insuficientes".
  3. Se generan credenciales vía POST `/api/v1/admin/credentials/generate` (siempre PAYMENTS).
  4. Se intenta crear transacción → HTTP 201 (éxito).
- **Resultado esperado:** HTTP 4xx (credencial sin permiso).
- **Resultado actual:** HTTP 201 (credencial tiene permiso PAYMENTS).
- **Test asociado:** SerenityTestRunner.Solicitud de pago no exitosa debido a permisos insuficientes
- **Archivo de test:** `src/test/resources/features/HU006_validacion_credenciales.feature:29`
- **Fecha detectada:** 2026-06-10
- **Resolución / Comentarios de cierre:** Pendiente — requiere soporte de scopes en `CredentialApplicationService.generateCredentials()`

---

### BUG-015: HU008 — Endpoint de actualización bancaria no existe

- **Título:** HU008 — PUT /api/v1/merchants/{id}/bank-account no implementado
- **HU(s) relacionadas:** HU008
- **Componente:** Identity / API (MerchantPortal)
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** El endpoint para actualizar datos bancarios del comercio no está implementado en ningún controlador. Las pruebas reciben HTTP 401.
- **Pasos para reproducir:**
  1. Autenticarse con credenciales activas.
  2. Enviar PUT `/api/v1/merchants/{id}/bank-account` con datos bancarios.
  3. Respuesta: HTTP 401.
- **Resultado esperado:** HTTP 200/202.
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.Actualización de datos bancarios queda en estado de verificación pendiente
- **Archivo de test:** `src/test/resources/features/HU008_actualizacion_perfil.feature:28`
- **Fecha detectada:** 2026-06-10

---

### BUG-016: HU008 — Backend no rechaza modificación de campos inmutables

- **Título:** HU008 — Backend no retorna mensaje "contactar a soporte" al intentar modificar businessId
- **HU(s) relacionadas:** HU008
- **Componente:** Identity / CommerceApplicationService
- **Prioridad:** P2/Medium
- **Severidad:** Minor
- **Estado:** Open
- **Descripción breve:** El backend acepta silenciosamente cambios a businessId (lo ignora) y no retorna un mensaje sugiriendo contactar a soporte. La prueba espera que la respuesta incluya esa sugerencia.
- **Pasos para reproducir:**
  1. Autenticarse con credenciales activas.
  2. Enviar PATCH `/api/v1/merchant-portal/profile` con `{"businessId": "RFC_MODIFICADO_999"}`.
  3. Respuesta: HTTP 200 sin mensaje de soporte.
- **Resultado esperado:** HTTP 400/422 con mensaje "contactar a soporte".
- **Resultado actual:** HTTP 200 OK con perfil actualizado (businessId se ignora).
- **Test asociado:** SerenityTestRunner.No es posible modificar datos de identificación del comercio
- **Archivo de test:** `src/test/resources/features/HU008_actualizacion_perfil.feature:37`
- **Fecha detectada:** 2026-06-10

---

### BUG-017: HU008 — Filtro de credenciales no distingue roles de usuario

- **Título:** HU008 — CredentialValidationFilter otorga ROLE_MERCHANT a cualquier API key válida, ignorando el rol JWT
- **HU(s) relacionadas:** HU008
- **Componente:** Security / CredentialValidationFilter
- **Prioridad:** P1/High
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** Un usuario autenticado como DEVELOPER puede modificar el perfil del comercio porque el `CredentialValidationFilter` otorga autoridad `ROLE_MERCHANT` a cualquier request con API keys válidas, sobrescribiendo el rol real del usuario (DEVELOPER).
- **Pasos para reproducir:**
  1. Login como developer@paycore.com.
  2. Enviar PATCH `/api/v1/merchant-portal/profile` con API keys válidas + Bearer token.
  3. Respuesta: HTTP 200 (el filtro da ROLE_MERCHANT).
- **Resultado esperado:** HTTP 403 (desarrollador no tiene permiso).
- **Resultado actual:** HTTP 200 (actualización exitosa).
- **Test asociado:** SerenityTestRunner.Usuario con el rol de Desarrollador no puede modificar el perfil del comercio
- **Archivo de test:** `src/test/resources/features/HU008_actualizacion_perfil.feature:44`
- **Fecha detectada:** 2026-06-10

---

### BUG-018: HU008 — Endpoint de actualización bancaria (formato inválido) no existe

- **Título:** HU008 — Validación de formato de cuenta bancaria no implementada (endpoint no existe)
- **HU(s) relacionadas:** HU008
- **Componente:** Identity / API
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** Mismo problema que BUG-015. El endpoint para datos bancarios no existe, por lo que no se puede probar validación de formato inválido.
- **Resultado esperado:** HTTP 400/422 con indicación de campos inválidos.
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.Intento de actualización con datos bancarios en formato inválido
- **Archivo de test:** `src/test/resources/features/HU008_actualizacion_perfil.feature:50`
- **Fecha detectada:** 2026-06-10

---

### BUG-019: HU010 — Endpoint de rotación de credenciales no existe

- **Título:** HU010 — POST /api/v1/admin/credentials/rotate no implementado
- **HU(s) relacionadas:** HU010
- **Componente:** Identity / CredentialController
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** El endpoint para rotar credenciales no existe en el backend. Las pruebas reciben HTTP 401.
- **Pasos para reproducir:**
  1. Autenticarse como admin.
  2. Enviar POST `/api/v1/admin/credentials/rotate`.
  3. Respuesta: HTTP 401.
- **Resultado esperado:** HTTP 201 con nuevas credenciales + período de gracia.
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.Rotación exitosa con periodo de gracia para migración
- **Archivo de test:** `src/test/resources/features/HU010_rotacion_credenciales.feature:22`
- **Fecha detectada:** 2026-06-10

---

### BUG-020: HU010 — Validación de límite de rotación no implementada

- **Título:** HU010 — Validación de límite máximo de credenciales activas no implementada
- **HU(s) relacionadas:** HU010
- **Componente:** Identity / CredentialApplicationService
- **Prioridad:** P2/Medium
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** El endpoint de rotación no existe, por lo que no se puede probar la validación de límite máximo de credenciales activas.
- **Resultado esperado:** HTTP 400/409 con mensaje de límite alcanzado.
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.No es posible rotar credenciales cuando se ha alcanzado el límite máximo
- **Archivo de test:** `src/test/resources/features/HU010_rotacion_credenciales.feature:37`
- **Fecha detectada:** 2026-06-10

---

### BUG-021: HU013 — Endpoint de auditoría (inmutabilidad de registros) no existe

- **Título:** HU013 — GET/DELETE /api/v1/audit/transactions/{id}/events no implementado
- **HU(s) relacionadas:** HU013
- **Componente:** Audit / API
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** No existe un controlador REST para la bitácora de auditoría. Los endpoints `/api/v1/audit/*` no están implementados, las pruebas reciben HTTP 401.
- **Pasos para reproducir:**
  1. Crear transacción y cambiar estado.
  2. Intentar DELETE `/api/v1/audit/transactions/{id}/events/evt_001`.
  3. Respuesta: HTTP 401.
- **Resultado esperado:** HTTP 403/404 (rechazar modificación).
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.Cada cambio de estado de un pago genera un registro de auditoría automático
- **Archivo de test:** `src/test/resources/features/HU013_registro_auditoria.feature:19`
- **Fecha detectada:** 2026-06-10

---

### BUG-022: HU013 — Endpoint de integridad de auditoría no existe

- **Título:** HU013 — GET /api/v1/audit/transactions/{id}/integrity no implementado
- **HU(s) relacionadas:** HU013
- **Componente:** Audit / API
- **Prioridad:** P1/High
- **Severidad:** Major
- **Estado:** Open
- **Descripción breve:** El endpoint para verificar integridad de la bitácora de auditoría no está implementado (HTTP 401).
- **Resultado esperado:** HTTP 200 indicando integridad verificada.
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.La integridad de la bitácora de auditoría puede ser verificada
- **Archivo de test:** `src/test/resources/features/HU013_registro_auditoria.feature:28`
- **Fecha detectada:** 2026-06-10

---

### BUG-023: HU013 — Endpoint de consulta de eventos de auditoría no existe

- **Título:** HU013 — GET /api/v1/audit/transactions/{id}/events no implementado
- **HU(s) relacionadas:** HU013
- **Componente:** Audit / API
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** El endpoint para consultar la lista cronológica de eventos de auditoría no está implementado (HTTP 401).
- **Resultado esperado:** HTTP 200 con lista de eventos.
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.Un administrador autorizado puede consultar la bitácora de eventos de un pago
- **Archivo de test:** `src/test/resources/features/HU013_registro_auditoria.feature:35`
- **Fecha detectada:** 2026-06-10

---

### BUG-024: HU013 — Endpoint de modificación/eliminación de auditoría no existe

- **Título:** HU013 — PUT/DELETE /api/v1/audit/* no implementado (protección de inmutabilidad)
- **HU(s) relacionadas:** HU013
- **Componente:** Audit / API
- **Prioridad:** P1/High
- **Severidad:** Blocker
- **Estado:** Open
- **Descripción breve:** El endpoint para intentar modificar/eliminar registros de auditoría no existe (HTTP 401). No se puede verificar que el sistema rechace estas operaciones.
- **Resultado esperado:** HTTP 403/404 (rechazar modificación).
- **Resultado actual:** HTTP 401.
- **Test asociado:** SerenityTestRunner.No es posible modificar ni eliminar un registro de la bitácora
- **Archivo de test:** `src/test/resources/features/HU013_registro_auditoria.feature:42`
- **Fecha detectada:** 2026-06-10

---

### BUG-025: HU017 — Reembolso parcial excesivo retorna 409 en vez de 400/422

- **Título:** HU017 — InvalidTransactionStateException mapeada a 409 Conflict en vez de 400 Bad Request
- **HU(s) relacionadas:** HU017
- **Componente:** Common / GlobalExceptionHandler
- **Prioridad:** P3/Low
- **Severidad:** Minor
- **Estado:** Open
- **Descripción breve:** Al intentar reembolsar más del monto disponible, el dominio lanza `InvalidTransactionStateException` que se mapea a HTTP 409 Conflict. La prueba espera 400 o 422.
- **Pasos para reproducir:**
  1. Crear transacción aprobada.
  2. Reembolsar parcialmente.
  3. Intentar reembolsar más del monto disponible.
  4. Respuesta: HTTP 409.
- **Resultado esperado:** HTTP 400/422.
- **Resultado actual:** HTTP 409.
- **Test asociado:** SerenityTestRunner.No es posible reembolsar más del monto disponible
- **Archivo de test:** `src/test/resources/features/HU017_reembolso_parcial.feature:29`
- **Fecha detectada:** 2026-06-10

---
