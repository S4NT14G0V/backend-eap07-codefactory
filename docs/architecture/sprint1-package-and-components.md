# Sprint 1 - Package and Component Views (Initial Architecture)

Alcance estricto Sprint 1: HU001, HU002, HU003, HU004, HU005, HU006.

Decisiones confirmadas:
- Estado inicial de pago: CREATED.
- MFA: Email OTP.
- Seguridad transaccional: API Key + Secret hash.
- Dashboards: externos, sin detalle interno.
- Convencion de puertos/adaptadores: application.port e infrastructure.adapter.

## Package Diagram (Sprint 1)

```mermaid
flowchart TB
  A[com.codefactory.appstripe]

  subgraph ID[identity]
    ID_API[api]
    ID_APP[application]
    ID_PORT[application.port]
    ID_DOM[domain]
    ID_INF_ADP[infrastructure.adapter]
    ID_INF_PER[infrastructure.persistence]
  end

  subgraph SEC[security]
    SEC_API[api]
    SEC_APP[application]
    SEC_PORT[application.port]
    SEC_DOM[domain]
    SEC_INF_ADP[infrastructure.adapter]
    SEC_INF_PER[infrastructure.persistence]
  end

  subgraph TX[transactions]
    TX_API[api]
    TX_APP[application]
    TX_PORT[application.port]
    TX_DOM[domain]
    TX_INF_ADP[infrastructure.adapter]
    TX_INF_PER[infrastructure.persistence]
  end

  subgraph SH[shared]
    SH_KERNEL[kernel]
    SH_ERR[error]
    SH_AUDIT[audit]
  end

  A --> ID
  A --> SEC
  A --> TX
  A --> SH

  ID_API --> ID_APP
  SEC_API --> SEC_APP
  TX_API --> TX_APP

  ID_APP --> ID_PORT
  SEC_APP --> SEC_PORT
  TX_APP --> TX_PORT

  ID_APP --> ID_DOM
  SEC_APP --> SEC_DOM
  TX_APP --> TX_DOM

  ID_INF_ADP --> ID_PORT
  ID_INF_PER --> ID_PORT
  SEC_INF_ADP --> SEC_PORT
  SEC_INF_PER --> SEC_PORT
  TX_INF_ADP --> TX_PORT
  TX_INF_PER --> TX_PORT

  TX_APP --> SEC_PORT
  TX_APP --> ID_PORT

  ID_APP --> SH_KERNEL
  SEC_APP --> SH_KERNEL
  TX_APP --> SH_KERNEL
  ID_APP --> SH_ERR
  SEC_APP --> SH_ERR
  TX_APP --> SH_ERR
  TX_APP --> SH_AUDIT
```

## Component Diagram with Interfaces (Sprint 1)

```mermaid
flowchart LR
  subgraph API[API Layer]
    C1[CommerceController]
    C2[CredentialController]
    C3[MfaController]
    C4[TransactionController]
    F1[CredentialValidationFilter]
  end

  subgraph APP[Application Layer]
    I1[[ICommerceApplicationService]]
    I2[[ICredentialApplicationService]]
    I3[[IMfaApplicationService]]
    I4[[ITransactionApplicationService]]
    I5[[ICredentialValidationService]]

    S1[CommerceApplicationService]
    S2[CredentialApplicationService]
    S3[MfaApplicationService]
    S4[TransactionApplicationService]
    S5[CredentialValidationService]
  end

  subgraph PORTS[Application Ports]
    P1[[ICommerceRepositoryPort]]
    P2[[IApiCredentialRepositoryPort]]
    P3[[IMfaChallengeRepositoryPort]]
    P4[[ITransactionRepositoryPort]]
    G1[[IApiKeyGeneratorGatewayPort]]
    G2[[IEmailOtpGatewayPort]]
  end

  subgraph ADAPTERS[Infrastructure Adapters]
    A1[CommerceRepositoryAdapter]
    A2[ApiCredentialRepositoryAdapter]
    A3[MfaChallengeRepositoryAdapter]
    A4[TransactionRepositoryAdapter]
    A5[ApiKeyGeneratorAdapter]
    A6[EmailOtpGatewayAdapter]
    DB[(MySQL)]
  end

  C1 --> I1
  C2 --> I2
  C3 --> I3
  C4 --> I4
  C4 --> I5
  F1 --> I5

  I1 --> S1
  I2 --> S2
  I3 --> S3
  I4 --> S4
  I5 --> S5

  S1 --> P1
  S2 --> P2
  S2 --> G1
  S3 --> P3
  S3 --> G2
  S4 --> P4
  S4 --> I5
  S5 --> P2

  A1 --> P1
  A2 --> P2
  A3 --> P3
  A4 --> P4
  A5 --> G1
  A6 --> G2

  A1 --> DB
  A2 --> DB
  A3 --> DB
  A4 --> DB
```

## Interface Matrix (Sprint 1)

| Interface | Modulo | Responsabilidad | Operaciones iniciales |
|---|---|---|---|
| ICommerceApplicationService | identity | Caso de uso de comercios | registerMerchant, getMerchantById |
| ICredentialApplicationService | identity | Credenciales API por comercio | generateApiCredential, revokeApiCredential |
| IMfaApplicationService | security | Orquestacion de MFA por email OTP | createChallenge, verifyChallenge |
| ITransactionApplicationService | transactions | Orquestacion transaccional | createTransaction, assignInitialStatusCreated, getTransactionById |
| ICredentialValidationService | security | Validacion por solicitud API | validateApiKeyAndSecretHash |
| ICommerceRepositoryPort | identity | Persistencia de comercios | save, findById, existsByBusinessId |
| IApiCredentialRepositoryPort | identity/security | Persistencia de credenciales API | save, findByKeyHash, findActiveByMerchantId, updateLastUsedAt |
| IMfaChallengeRepositoryPort | security | Persistencia de desafios OTP | saveChallenge, findActiveByPrincipal, invalidateChallenge |
| ITransactionRepositoryPort | transactions | Persistencia de pagos | save, findById, existsByIdempotencyKey |
| IApiKeyGeneratorGatewayPort | identity | Generador y hash seguro de credenciales | generateApiKey, generateSecret, hashSecret |
| IEmailOtpGatewayPort | security | Envio de OTP por correo | sendOtpEmail |

## Traceability HU -> Components

- HU001 Registro de comercio: CommerceController -> ICommerceApplicationService -> ICommerceRepositoryPort.
- HU002 Credenciales API: CredentialController -> ICredentialApplicationService -> IApiCredentialRepositoryPort + IApiKeyGeneratorGatewayPort.
- HU003 Crear transaccion: TransactionController -> ITransactionApplicationService -> ITransactionRepositoryPort.
- HU004 Estado inicial CREATED: TransactionApplicationService.assignInitialStatusCreated.
- HU005 MFA Email OTP: MfaController -> IMfaApplicationService -> IMfaChallengeRepositoryPort + IEmailOtpGatewayPort.
- HU006 Validacion por solicitud: CredentialValidationFilter/TransactionController -> ICredentialValidationService -> IApiCredentialRepositoryPort.
