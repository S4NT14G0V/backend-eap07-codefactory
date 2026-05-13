# Registro de Bugs — QA

**Estado actual (2026-05-13T00:33:55-05:00):** No hay bugs abiertos detectados por la ejecución automatizada de pruebas unitarias.

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

## Registro actual

- Ningún bug abierto detectado por la ejecución automatizada (2026-05-13T00:33:55-05:00).
