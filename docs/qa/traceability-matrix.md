# Matriz de Trazabilidad

Última actualización: **2026-05-30T00:43:00-05:00**

Resumen ejecutivo: tabla de trazabilidad que relaciona todas las Historias de Usuario (HU) del proyecto con los tests automatizados, reportes y artefactos QA.

Estado de ejecución de pruebas Serenity BDD (última corrida): **11 tests Serenity, 7 fallos (0 errores)**. 4 Serenity tests pasaron, 7 fallaron. Las 7 fallas corresponden a funcionalidad backend no implementada (ver bugs en `docs/qa/bugs.md`). Informe JaCoCo: `target/site/jacoco/index.html`.

Estado de cobertura global (extraído de `target/jacoco/jacoco.xml`):

- Instrucciones: 454 / 1120 ≈ **40.5%**
- Líneas: 141 / 331 ≈ **42.6%**

Objetivo: cumplir los requisitos del PAC (Cobertura unitarias ≥ 40% en SonarCloud, Deuda técnica < 2 días, Vulnerabilidades críticas = 0) y mantener la trazabilidad HU → test → reporte → issue/WorkItem.

| HU | Descripción | Módulo / Componente | Tests relevantes | Resultado (última ejecución) | Cobertura (líneas %) | Reportes | Bugs | Owner | Estado |
|---|---|---|---|---:|---:|---|---|---|---|
| HU001 | Registro de comercio | Identity / CommerceApplicationService | com.codefactory.appstripe.identity.application.CommerceApplicationServiceTest (8 tests) | Passed | - | target/surefire-reports/com.codefactory.appstripe.identity.application.CommerceApplicationServiceTest.txt#L1 | - | TBD | Done |
| HU002 | Credenciales API (generar/revocar) | Identity / CredentialApplicationService | com.codefactory.appstripe.identity.application.CredentialApplicationServiceTest (5 tests) | Passed | - | target/surefire-reports/com.codefactory.appstripe.identity.application.CredentialApplicationServiceTest.txt#L1 | - | TBD | Done |
| HU003 | Crear transacción | Transactions / Controller + Service | TransactionControllerTest (2), TransactionApplicationServiceTest (2) | Passed | - | target/surefire-reports/com.codefactory.appstripe.transactions.api.TransactionControllerTest.txt#L1, target/surefire-reports/com.codefactory.appstripe.transactions.application.TransactionApplicationServiceTest.txt#L1 | - | TBD | Done |
| HU004 | Estado inicial CREATED en transacción | Transactions / Domain | TransactionApplicationServiceTest | Passed | - | target/surefire-reports/com.codefactory.appstripe.transactions.application.TransactionApplicationServiceTest.txt#L1 | - | TBD | Done |
| HU005 | MFA Email OTP (orquestación) | Identity / MFA service (orchestration) | *No se encontró test unitario específico en la suite actual* | No cubierto | - | - | Pendiente (ver ejemplo en `bugs.md`) | TBD | Open (tests missing) |
| HU006 | Validación por solicitud (API Key + secret hash) | Security / CredentialValidationFilter | CP-S2-001 · CP-S2-003 (Serenity BDD) | 1 PASS, **1 FAIL** (BUG-001) | - | `HU006_validacion_credenciales.feature` | BUG-001 | TBD | Partial |
| HU007 | Consulta de perfil de comercio | Identity / Commerce Profile | CP-S2-004 (GET /merchant-portal/profile) | **0 PASS, 1 FAIL** (BUG-002) | - | `HU007_consulta_perfil.feature` | BUG-002 | TBD | Open |
| HU008 | Actualización de perfil / configuración bancaria | Identity / Commerce Profile | CP-S2-006 (PATCH profile) | **0 PASS, 1 FAIL** (BUG-004) | - | `HU008_actualizacion_perfil.feature` | BUG-004 | TBD | Open |
| HU009 | Separación operativa de entornos (sandbox/producción) | Infra / DevOps | - | Pending / Awaiting HU implementation | - | - | - | TBD | Pending |
| HU010 | Revocación inmediata de credenciales | Identity / Credential Service | CP-S2-010 (revoke credential) · CP-S2-011 (revoked rejects requests) | **2 PASS** | - | `HU010_revocacion_credenciales.feature` | - | TBD | Done |
| HU011 | Rotación de credenciales sin interrupción | Identity / Credential Service | - | Pending / Awaiting HU implementation | - | - | - | TBD | Pending |
| HU012 | Procesamiento del resultado del pago (aprobado/rechazado/fallido) | Transactions / Payment Result | CP-S2-014 (approved) · CP-S2-015 (rejected) | **0 PASS, 2 FAIL** (BUG-006, BUG-007) | - | `HU012_resultado_pago.feature` | BUG-006, BUG-007 | TBD | Open |
| HU013 | Configuración de canales de notificación (webhooks) | Transactions / Notifier | - | Pending / Awaiting HU implementation | - | - | - | TBD | Pending |
| HU014 | Registro automático de eventos de auditoría | Audit / Observability | CP-S2-018 (audit on create) | **1 PASS** | - | `HU014_auditoria.feature` | - | TBD | Open |
| HU015 | Consulta del listado de pagos con filtros | Transactions / Query API | CP-S2-020 (paginated listing) · CP-S2-021 (isolation) | **0 PASS, 2 FAIL** (BUG-011, BUG-013) | - | `HU015_listado_pagos.feature` | BUG-011, BUG-013 | TBD | Open |

## Cobertura y artefactos globales

- Informe JaCoCo (HTML): `target/site/jacoco/index.html`
- Datos raw JaCoCo: `target/jacoco.exec`
- Reportes Surefire (tests): `target/surefire-reports/` (por clase)
- SonarCloud: se recomienda integrar con `sonar:sonar` y aplicar Quality Gates del PAC (Cobertura >40%, Deuda técnica <2 días, Vulnerabilidades críticas = 0).

## Bugs Activos (7 bugs — ver `docs/qa/bugs.md`)

| Bug ID | HU | CP | Descripción | Componente | Prioridad |
|--------|:--:|:--:|-------------|------------|:---------:|
| BUG-001 | HU006 | CP-S2-001 | ID de transacción en UUID en vez de `txn_*` | Transactions / Domain | P2/Medium |
| BUG-002 | HU007 | CP-S2-004 | GET /merchant-portal/profile devuelve 403 en vez de 200 | Security / Filter | P1/High |
| BUG-004 | HU008 | CP-S2-006 | PATCH profile devuelve 403 en vez de 200 | Security / Filter | P1/High |
| BUG-006 | HU012 | CP-S2-014 | PATCH /transactions/{id}/complete devuelve 403 en vez de 200 | Transactions / API | P1/High |
| BUG-007 | HU012 | CP-S2-015 | PATCH /transactions/{id}/complete (REJECTED) devuelve 403 | Transactions / API | P1/High |
| BUG-011 | HU015 | CP-S2-020 | GET /api/v1/transactions devuelve 403 (listado paginado) | Transactions / API | P1/High |
| BUG-013 | HU015 | CP-S2-021 | GET /transactions devuelve 403 (aislamiento entre comercios) | Transactions / API | P2/Medium |

## Notas y acciones recomendadas

- **7 bugs abiertos** detectados en ejecución Serenity BDD del 2026-05-30. Todos corresponden a funcionalidad backend no implementada o mal configurada.
- HU005 (MFA): priorizar creación de tests unitarios y E2E para flujo challenge/verify.
- HU010 (Revocación): **COMPLETADO** — 2 pruebas Serenity pasaron correctamente.
- Los 7 bugs deben registrarse como Work Items en Azure DevOps y asignarse al equipo de desarrollo.
- Las pruebas (features/step definitions) están correctamente escritas; el backend no implementa los endpoints, filtros o formatos esperados.
- Integrar ejecución de tests y reporte JaCoCo en CI (GitHub Actions o ADO) y subir resultados a SonarCloud para cumplir Quality Gates.

---

Archivo generado automáticamente en apoyo al PAC — revisa y completa las descripciones/owners faltantes.
