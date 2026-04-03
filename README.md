# AppStripe - Motor de Pagos Embebidos (Caso 8)

Plataforma de pagos B2B tipo Stripe orientada a exponer APIs seguras para que aplicaciones externas puedan crear, validar y trazar transacciones de pago.

Este repositorio contiene el backend principal en Spring Boot bajo un enfoque de monolito modular, con separacion estricta por dominios.

## Objetivo del proyecto

Construir un motor de pagos embebidos que permita:

- Registrar y gestionar comercios B2B.
- Emitir y validar credenciales seguras de acceso a API.
- Procesar transacciones con reglas de negocio y estado controlado.
- Asegurar trazabilidad y observabilidad para auditoria.

## Alcance funcional

### Dominios del sistema

1. Identidad y Comercios.
2. Transacciones (motor de pagos).
3. Auditoria y Observabilidad.

### Decisiones base confirmadas

- Arquitectura: monolito modular.
- Persistencia: MySQL.
- Seguridad de APIs transaccionales: API Key + Secret hash.
- MFA para accesos sensibles: Email OTP.
- Estado inicial de una transaccion en Sprint 1: CREATED.

## Arquitectura y documentacion

Diagramas disponibles:

- Contexto C4 (Nivel 1): [docs/architecture/c4-context.md](docs/architecture/c4-context.md)
- Contenedores C4 (Nivel 2): [docs/architecture/c4-containers.md](docs/architecture/c4-containers.md)
- Vistas iniciales Sprint 1 (paquetes + componentes + interfaces): [docs/architecture/sprint1-package-and-components.md](docs/architecture/sprint1-package-and-components.md)

## Enfoque de implementacion

El proyecto se aborda por incrementos, priorizando arquitectura limpia, seguridad y trazabilidad desde el inicio.

### Sprint 1 - Fundaciones arquitectonicas y seguridad base

Historias objetivo: HU001 a HU006.

Entregables clave:

- Registro de comercio.
- Generacion de credenciales API.
- Creacion de transaccion.
- Asignacion de estado inicial CREATED.
- MFA por Email OTP.
- Validacion de credenciales por solicitud.

### Sprint 2 - Maduracion del flujo transaccional

Foco esperado:

- Integracion con Sandbox Mock.
- Despliegue y hardening operativo.
- Evolucion de estados y consistencia transaccional.

### Sprint 3 - Consolidacion y observabilidad avanzada

Foco esperado:

- Auditoria reforzada.
- Reporteria y tableros.
- Cierre de calidad, pruebas y estabilizacion final.

## Principios tecnicos del repositorio

- Separacion por capas y por dominio.
- Contratos claros entre aplicacion y adaptadores.
- Manejo de errores estandarizado (`errorCode`, `message`, `details`, `traceId`).
- Validacion estricta de payloads.
- Pruebas unitarias con patron AAA.

## Estructura actual

```text
src/
	main/
		java/com/codefactory/appstripe/
		resources/
	test/
		java/com/codefactory/appstripe/
docs/
	architecture/
```

## Stack tecnologico

- Java 17
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Bean Validation
- Maven Wrapper

## Como ejecutar el proyecto

### Prerrequisitos

- JDK 17+
- Maven (opcional, se recomienda wrapper)

### Ejecucion local

Windows:

```bat
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

### Ejecutar pruebas

Windows:

```bat
mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

## Estado actual

Actualmente el repositorio contiene la base del proyecto y la documentacion arquitectonica inicial para guiar la construccion del backend por sprints.

## Equipo

Proyecto academico de Fabrica Escuela CodeFactory (EAP07), orientado a buenas practicas de backend, arquitectura y seguridad aplicada a APIs de pagos.