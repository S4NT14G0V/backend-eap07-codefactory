# Registro de Bugs — QA

**Estado actual (2026-05-13T00:33:55-05:00):** No hay bugs abiertos detectados por la ejecución automatizada de pruebas unitarias.

## Plantilla para nuevo bug

- **Título:** 
- **HU(s) relacionadas:** 
- **Componente:** (API / Servicio / Repository / Filter / etc.)
- **Prioridad:** (P0/Critical, P1/High, P2/Medium, P3/Low)
- **Severidad:** (Blocker, Major, Minor)
- **Estado:** Open
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
- **Adjuntos:** capturas, dumps, respuestas HTTP
- **Asignado a:**
- **Fecha detectada:**

## Proceso recomendado al detectar un fallo en pruebas automáticas

1. Crear un issue en el Bug Tracker (GitHub Issues / JIRA) usando la plantilla anterior.
2. Etiquetar con `bug`, `test-failure` y la HU correspondiente (`HU00X`).
3. Adjuntar el `target/surefire-reports/<file>.txt` y el fragmento del stacktrace.
4. Enlazar el issue desde `docs/qa/traceability-matrix-sprint1.md` en la columna *Bugs*.
5. Asignar prioridad y responsable; planear corrección en sprint/PR.

## Registro actual

- Ningún bug abierto detectado por la ejecución automatizada (2026-05-13T00:33:55-05:00).
