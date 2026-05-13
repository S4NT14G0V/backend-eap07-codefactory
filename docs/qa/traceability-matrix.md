# Matriz de Trazabilidad

Última actualización: **2026-05-13T00:33:55-05:00**

Resumen ejecutivo: tabla de trazabilidad que relaciona todas las Historias de Usuario (HU) del proyecto con los tests automatizados, reportes y artefactos QA. Se incluyen HUs con cobertura conocida y placeholders para HUs sin definición o pruebas aún pendientes.

Estado de ejecución de pruebas (última corrida): **24 tests, 0 fallos**. Informe JaCoCo: `target/site/jacoco/index.html`.

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
| HU006 | Validación por solicitud (API Key + secret hash) | Security / CredentialValidationFilter | CredentialValidationFilterTest (6 tests) | Passed | - | target/surefire-reports/com.codefactory.appstripe.security.infrastructure.filter.CredentialValidationFilterTest.txt#L1 | - | TBD | Done |
| HU007 | Consulta de perfil de comercio | Identity / Commerce Profile | CP-S2-004 (GET /merchant-portal/profile) · CP-S2-005 (access control) | Partial — CP-S2-004: PASS; CP-S2-005: NO EJECUTABLE | - | [docs/qa/Casos de prueba.txt](docs/qa/Casos%20de%20prueba.txt#L691) | - | TBD | Partial |
| HU008 | Actualización de perfil / configuración bancaria | Identity / Commerce Profile | CP-S2-006 (PATCH /merchants/{id}/profile) · CP-S2-007 (immutable fields) | Not executed / NO EJECUTABLE | - | [CP-S2-006](docs/qa/Casos%20de%20prueba.txt#L723), [CP-S2-007](docs/qa/Casos%20de%20prueba.txt#L738) | - | TBD | Open |
| HU009 | Separación operativa de entornos (sandbox/producción) | Infra / DevOps | CP-S2-008 (reject sandbox creds in prod) · CP-S2-009 (sandbox simulated payment) | Pending / Awaiting HU implementation | - | [CP-S2-008](docs/qa/Casos%20de%20prueba.txt#L760), [CP-S2-009](docs/qa/Casos%20de%20prueba.txt#L780) | - | TBD | Pending |
| HU010 | Revocación inmediata de credenciales | Identity / Credential Service | CP-S2-010 (revoke credential) · CP-S2-011 (revoked rejects requests) | PASS (both CP-S2-010, CP-S2-011 passed) | - | [CP-S2-010](docs/qa/Casos%20de%20prueba.txt#L815), [CP-S2-011](docs/qa/Casos%20de%20prueba.txt#L835) | - | TBD | Done |
| HU011 | Rotación de credenciales sin interrupción | Identity / Credential Service | CP-S2-012 (rotate with grace period) · CP-S2-013 (auto-revoke after grace) | Pending / Awaiting HU implementation | - | [CP-S2-012](docs/qa/Casos%20de%20prueba.txt#L840), [CP-S2-013](docs/qa/Casos%20de%20prueba.txt#L860) | - | TBD | Pending |
| HU012 | Procesamiento del resultado del pago (aprobado/rechazado/fallido) | Transactions / Payment Result | CP-S2-014 (approved path) · CP-S2-015 (rejected — insufficient funds) | Pending / Awaiting HU implementation | - | [CP-S2-014](docs/qa/Casos%20de%20prueba.txt#L881), [CP-S2-015](docs/qa/Casos%20de%20prueba.txt#L901) | - | TBD | Pending |
| HU013 | Configuración de canales de notificación (webhooks) | Transactions / Notifier | CP-S2-016 (register webhook) · CP-S2-017 (retries & mark INACTIVE) | Pending / Awaiting HU implementation | - | [CP-S2-016](docs/qa/Casos%20de%20prueba.txt#L922), [CP-S2-017](docs/qa/Casos%20de%20prueba.txt#L942) | - | TBD | Pending |
| HU014 | Registro automático de eventos de auditoría | Audit / Observability | CP-S2-018 (immutable audit entries) · CP-S2-019 (integrity detection) | Pending / Awaiting HU implementation | - | [CP-S2-018](docs/qa/Casos%20de%20prueba.txt#L963), [CP-S2-019](docs/qa/Casos%20de%20prueba.txt#L983) | - | TBD | Pending |
| HU015 | Consulta del listado de pagos con filtros | Transactions / Query API | CP-S2-020 (filtered listing) · CP-S2-021 (isolation by merchant) | Pending / Awaiting HU implementation | - | [CP-S2-020](docs/qa/Casos%20de%20prueba.txt#L1006), [CP-S2-021](docs/qa/Casos%20de%20prueba.txt#L1027) | - | TBD | Pending |

## Cobertura y artefactos globales

- Informe JaCoCo (HTML): `target/site/jacoco/index.html`
- Datos raw JaCoCo: `target/jacoco.exec`
- Reportes Surefire (tests): `target/surefire-reports/` (por clase)
- SonarCloud: se recomienda integrar con `sonar:sonar` y aplicar Quality Gates del PAC (Cobertura >40%, Deuda técnica <2 días, Vulnerabilidades críticas = 0).

## Notas y acciones recomendadas

- HU005 (MFA): priorizar creación de tests unitarios y E2E para flujo challenge/verify; abrir Work Item en Azure DevOps y enlazarlo desde la columna *Bugs*.
- Completar descripciones y tests para HU007..HU024; asignar **Owner** por HU y añadir responsabilidad en la matriz.
- Publicar esta matriz en `/docs/quality/traceability/` y asegurarse de que los registros de defectos se mantengan en Azure DevOps (o con copia en `/docs/quality/defects/`).
- Integrar ejecución de tests y reporte JaCoCo en CI (GitHub Actions o ADO) y subir resultados a SonarCloud para cumplir Quality Gates.

---

Archivo generado automáticamente en apoyo al PAC — revisa y completa las descripciones/owners faltantes.
