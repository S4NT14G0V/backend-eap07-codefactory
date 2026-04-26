**Montaje y configuración de SonarCloud / SonarQube**

Resumen rápido:
- Plataforma recomendada: SonarCloud (integración con GitHub). También puede desplegarse SonarQube en Docker si se prefiere un servidor local.
- Objetivo: habilitar análisis continuo, configurar Quality Gate y validar métricas: complejidad ciclomática, complejidad cognitiva y deuda técnica.

Pasos (ordenados):

1) Crear proyecto en SonarCloud
- Accede a https://sonarcloud.io y conecta tu cuenta con GitHub (o usa SonarQube en tu servidor si prefieres).
- Crea una organización y registra el proyecto (elige el repositorio GitHub). Anota `organization` y `project key`.

2) Generar token de análisis
- En SonarCloud: My Account -> Security -> Generate Token. Guarda el token.

3) Añadir secretos en GitHub
- En el repositorio GitHub: Settings -> Secrets -> Actions, añade:
  - `SONAR_TOKEN` = (el token generado)
  - `SONAR_ORG` = (organization key)
  - `SONAR_PROJECT_KEY` = (project key)

4) Archivos añadidos al repo (ya presentes):
- `sonar-project.properties` (raíz): fichero con `sonar.projectKey`, `sonar.organization` y ruta de reportes JaCoCo.
- `.github/workflows/sonarcloud.yml`: workflow para ejecutar `mvn clean verify sonar:sonar` en pushes/PRs.

5) Generar reportes de cobertura (JaCoCo)
- Se añadió `jacoco-maven-plugin` en `pom.xml`. El pipeline ejecuta `mvn verify` y JaCoCo genera el XML para que Sonar lo lea.
- Ruta esperada del informe JaCoCo: `target/jacoco/jacoco.xml` (configurable).

6) Configurar Quality Gate en SonarCloud
- En SonarCloud: Quality Gates -> Create -> Add conditions. Recomendaciones:
  - `Coverage on New Code` >= 80%
  - `Cognitive Complexity` (on New Code) — establecer umbral razonable, p.ej. < 15 para nuevo código
  - `Complexity` / `Function Complexity` — evitar funciones con complejidad cyclomatic > 10
  - `Technical Debt Ratio` (Maintainability) < 5%
- Guarda el Quality Gate y asígnalo al proyecto.

7) Ejecutar análisis e interpretar métricas
- Haz push a `main` o abre un PR para que el workflow corra. En SonarCloud verás:
  - Complexity (complejidad ciclomática) por archivo y función
  - Cognitive Complexity por función
  - Technical Debt Ratio y estimación en días
  - Coverage y cobertura en código nuevo

8) Validación de los entregables (puntos 4,6,7,8)
- Punto 4 (configuración de análisis): verás el análisis automático en SonarCloud tras configurar secrets y hacer push.
- Punto 6 (complejidad ciclomática): usa el tablero de Measures -> Complexity y el reporte por funciones.
- Punto 7 (complejidad cognitiva): Measures -> Cognitive Complexity, revisa hotspots con mayor complejidad.
- Punto 8 (deuda técnica): Measures -> Technical Debt Ratio / SQALE, comprueba valores y remediaciones sugeridas.

9) Si el Quality Gate falla
- Revisa la lista de Issues en SonarCloud y prioriza arreglos: reducir complejidad, simplificar funciones, añadir tests, mejorar cobertura.

Comandos útiles (local):
```bash
# Ejecutar análisis local con token (ejecuta desde la raíz del repo)
mvn -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=YOUR_TOKEN -Dsonar.organization=YOUR_ORG -Dsonar.projectKey=YOUR_PROJECT_KEY sonar:sonar

# Ejecutar build y tests (genera informe JaCoCo)
mvn clean verify
```

Notas finales:
- Hay pasos manuales requeridos en SonarCloud (crear proyecto, token, Quality Gate). No puedo completar esos pasos por API sin tus credenciales.
- Puedo ahora:
  - Actualizar thresholds recomendados en un Quality Gate template (documentar valores exactos) o
  - Crear reglas de exclusión si quieres omitir paquetes de test o generated code.
