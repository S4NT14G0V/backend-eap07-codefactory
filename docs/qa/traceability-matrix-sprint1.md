# Matriz de Trazabilidad — Sprint 1

Última ejecución de tests: **2026-05-13T00:33:55-05:00**

Resumen: todas las pruebas unitarias pasan en la ejecución indicada (24 tests, 0 fallos). Informe de cobertura generado por JaCoCo: target/site/jacoco/index.html.

| HU | Descripción | Tests relevantes | Resultado (última ejecución) | Reportes | Bugs |
|---|---|---|---:|---|---|
| HU001 | Registro de comercio | com.codefactory.appstripe.identity.application.CommerceApplicationServiceTest (8 tests) | Passed | target/surefire-reports/com.codefactory.appstripe.identity.application.CommerceApplicationServiceTest.txt#L1 | - |
| HU002 | Credenciales API (generar/revocar) | com.codefactory.appstripe.identity.application.CredentialApplicationServiceTest (5 tests) | Passed | target/surefire-reports/com.codefactory.appstripe.identity.application.CredentialApplicationServiceTest.txt#L1 | - |
| HU003 | Crear transacción | com.codefactory.appstripe.transactions.api.TransactionControllerTest (2 tests), com.codefactory.appstripe.transactions.application.TransactionApplicationServiceTest (2 tests) | Passed | target/surefire-reports/com.codefactory.appstripe.transactions.api.TransactionControllerTest.txt#L1, target/surefire-reports/com.codefactory.appstripe.transactions.application.TransactionApplicationServiceTest.txt#L1 | - |
| HU004 | Estado inicial CREATED en transacción | com.codefactory.appstripe.transactions.application.TransactionApplicationServiceTest | Passed | target/surefire-reports/com.codefactory.appstripe.transactions.application.TransactionApplicationServiceTest.txt#L1 | - |
| HU005 | MFA Email OTP (orquestación) | *No se encontró test unitario específico en la suite actual* | No cubierto | - | Pendiente: añadir tests E2E/unitarios |
| HU006 | Validación por solicitud (API Key + secret hash) | com.codefactory.appstripe.security.infrastructure.filter.CredentialValidationFilterTest (6 tests) | Passed | target/surefire-reports/com.codefactory.appstripe.security.infrastructure.filter.CredentialValidationFilterTest.txt#L1 | - |

Cobertura y artefactos generados:

- Informe JaCoCo (HTML): target/site/jacoco/index.html
- Datos raw JaCoCo: target/jacoco.exec#L1
- Resumen de tests (Surefire): target/surefire-reports/

Notas y observaciones:

- Todas las pruebas unitarias ejecutadas pasan en la ejecución indicada.
- HU005 (MFA) no tiene cobertura unitaria en la suite actual: planear pruebas unitarias y de integración (E2E) para flujo de challenge/verify.
- Se recomienda añadir tests que validen la protección de endpoints transaccionales con CredentialValidationFilter en integración.

Siguientes pasos recomendados:

- Añadir y automatizar tests para HU005 (MFA).
- Crear issues para cualquier hallazgo manual o de pruebas E2E.
- Integrar ejecución de tests y generación de reportes en CI (GitHub Actions/ADO) y publicar artefactos (surefire/jacoco).
