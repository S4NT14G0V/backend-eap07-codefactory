# Informe de Ingenieria de Datos - Sprint 1

## Objetivo

Presentar, de forma simple, los entregables 1 y 3 del Sprint 1:

1. Definicion de entidades a partir de reglas de negocio.
2. Modelo logico con entidades y relaciones (MER).

## Alcance

Este informe incluye solo el estado actual implementado del sistema.
No incluye consultas de negocio ni modelo fisico detallado.

## Entregable 1 - Definicion de entidades

### Entidad Merchant

Representa al comercio dentro de la plataforma.

Datos principales:

- id
- businessName
- businessId
- email
- businessType
- status

Reglas principales:

- No se permite repetir businessId.
- No se permite repetir email.
- El estado de alta del comercio es VERIFIED.

### Entidad ApiCredential

Representa las credenciales de acceso API de cada comercio.

Datos principales:

- id
- publicId
- secretHash
- merchantId
- active

Reglas principales:

- Solo comercios en estado VERIFIED pueden generar credenciales.
- Un comercio puede tener como maximo 3 credenciales activas.
- Se guarda hash de la clave secreta, no la clave en texto plano.

### Entidad Transaction

Representa una operacion de pago.

Datos principales:

- id
- merchantId
- amount
- status

Reglas principales:

- Toda transaccion inicia en estado CREATED.
- La transaccion puede pasar a PROCESSING.
- Si la transaccion ya esta finalizada, no puede volver a PROCESSING.

Estados de transaccion:

- CREATED
- PROCESSING
- APPROVED
- REJECTED
- FAILED

### Entidad User

Representa un usuario interno del sistema con soporte de doble factor.

Datos principales:

- username
- password
- twoFactorSecret
- twoFactorEnabled

Regla principal:

- twoFactorEnabled indica si el doble factor esta activo para ese usuario.

## Entregable 3 - Modelo logico (MER)

El modelo logico actual incluye estas entidades:

- Merchant
- ApiCredential
- Transaction
- User

Relaciones del modelo:

- Un Merchant puede tener muchas ApiCredential.
- Un Merchant puede tener muchas Transaction.
- User se mantiene independiente en el modelo actual.

Reglas de integridad logica:

- businessId y email de Merchant son unicos.
- publicId de ApiCredential es unico.
- ApiCredential y Transaction pertenecen a un Merchant.
- status de Merchant y Transaction debe respetar su dominio de estados.

## Conclusion

Para Sprint 1, el modelo de datos actual cubre la base funcional con cuatro entidades principales.
La estructura permite operar registro de comercios, emision de credenciales y ciclo de vida de transacciones en forma consistente.
El MER es simple: dos relaciones principales con Merchant como entidad central y User como entidad separada.
