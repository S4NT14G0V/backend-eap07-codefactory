package com.codefactory.appstripe.identity.application;

import com.codefactory.appstripe.identity.application.port.IApiCredentialRepositoryPort;
import com.codefactory.appstripe.identity.application.port.ICommerceRepositoryPort;
import com.codefactory.appstripe.identity.application.port.IMerchantAuditPort;
import com.codefactory.appstripe.identity.application.port.INotificationPort;
import com.codefactory.appstripe.identity.domain.ApiCredential;
import com.codefactory.appstripe.identity.domain.ApiCredentialPermission;
import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantAuditAction;
import com.codefactory.appstripe.identity.domain.MerchantAuditEvent;
import com.codefactory.appstripe.identity.domain.MerchantStatus;
import com.codefactory.appstripe.security.application.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Servicio de aplicación del módulo de Identidad y Comercios.
 * Orquesta los casos de uso: registro de comercios, perfil, actualización
 * y las acciones administrativas de aprobación y suspensión (HU Admin).
 */
@Service
@RequiredArgsConstructor
public class CommerceApplicationService {

    private final ICommerceRepositoryPort commerceRepository;
    private final IApiCredentialRepositoryPort credentialRepository;
    private final IMerchantAuditPort merchantAuditPort;
    private final INotificationPort notificationPort;
    private final AuthenticationService authenticationService;

    // =========================================================================
    // HU Registro (flujo de comercio)
    // =========================================================================

    /**
     * Registra un nuevo comercio en la plataforma.
     * El comercio queda en estado {@code PENDING_VERIFICATION}: no puede operar
     * hasta que un administrador lo apruebe explícitamente.
     */
    public Merchant registerMerchant(String businessName, String businessId,
                                     String email, String businessType) {
        if (commerceRepository.existsByBusinessId(businessId)) {
            throw new IllegalStateException("Ya existe un comercio con el número de identificación fiscal indicado");
        }
        if (commerceRepository.existsByEmail(email)) {
            throw new IllegalStateException("Ya existe un comercio con el correo electrónico indicado");
        }

        Merchant merchant = Merchant.builder()
                .id("mch_" + UUID.randomUUID().toString().replace("-", ""))
                .businessName(businessName)
                .businessId(businessId)
                .email(email)
                .businessType(businessType)
                // El comercio inicia en PENDING_VERIFICATION; requiere aprobación del admin
                .status(MerchantStatus.PENDING_VERIFICATION)
                .permission(ApiCredentialPermission.PAYMENTS)
                .build();

        Merchant savedMerchant = commerceRepository.save(merchant);

        // Crear usuario de acceso al portal (cuenta desactivada hasta que el admin apruebe)
        authenticationService.createMerchantUser(email, savedMerchant.getId());

        return savedMerchant;
    }

    // =========================================================================
    // HU Perfil (flujo de comercio autenticado)
    // =========================================================================

    /**
     * Recupera el perfil de un comercio a partir de su ID (extraído del JWT).
     */
    public Merchant getMerchantProfile(String merchantId) {
        if (merchantId == null || merchantId.isBlank()) {
            throw new IllegalStateException("El token no tiene un comercio asociado");
        }
        return commerceRepository.findById(merchantId)
                .orElseThrow(() -> new NoSuchElementException("Comercio no encontrado"));
    }

    /**
     * Actualiza los datos editables del perfil de un comercio.
     * Los campos nulos conservan el valor original.
     */
    public Merchant updateMerchant(String merchantId, String newName,
                                   String newEmail, String newType) {
        Merchant oldMerchant = getMerchantProfile(merchantId);

        String businessName = newName != null ? newName : oldMerchant.getBusinessName();
        String email        = newEmail != null ? newEmail : oldMerchant.getEmail();
        String businessType = newType != null ? newType : oldMerchant.getBusinessType();

        Merchant updated = Merchant.builder()
                .id(oldMerchant.getId())
                .businessId(oldMerchant.getBusinessId())
                .status(oldMerchant.getStatus())
                .permission(oldMerchant.getPermission())
                .businessName(businessName)
                .email(email)
                .businessType(businessType)
                .build();

        return commerceRepository.save(updated);
    }

    // =========================================================================
    // HU Admin — Consultas
    // =========================================================================

    /**
     * Retorna los comercios que están esperando revisión del administrador.
     * Usado en el panel de "bandeja de verificación".
     */
    public List<Merchant> listPendingMerchants() {
        return commerceRepository.findByStatus(MerchantStatus.PENDING_VERIFICATION);
    }

    /**
     * Retorna todos los comercios de la plataforma (panel general del admin).
     */
    public List<Merchant> listAllMerchants() {
        return commerceRepository.findAll();
    }

    // =========================================================================
    // HU Admin — Acciones administrativas
    // =========================================================================

    /**
     * Aprueba un comercio en estado PENDING_VERIFICATION.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Verifica que el comercio exista y esté en estado PENDING_VERIFICATION.</li>
     *   <li>Cambia el estado a {@code ACTIVE}.</li>
     *   <li>Registra el evento en la bitácora de auditoría.</li>
     *   <li>Envía notificación por correo al comercio (mock de consola).</li>
     * </ol>
     *
     * @param merchantId ID del comercio a aprobar
     * @param adminEmail e-mail del administrador que ejecuta la acción (extraído del JWT)
     * @return el comercio actualizado con estado ACTIVE
     */
    public Merchant approveMerchant(String merchantId, String adminEmail) {
        Merchant merchant = commerceRepository.findById(merchantId)
                .orElseThrow(() -> new NoSuchElementException("Comercio no encontrado: " + merchantId));

        // Regla de negocio: solo se pueden aprobar comercios en PENDING_VERIFICATION
        if (merchant.getStatus() != MerchantStatus.PENDING_VERIFICATION) {
            throw new IllegalStateException(
                    "Solo los comercios en estado PENDING_VERIFICATION pueden ser aprobados. " +
                    "Estado actual: " + merchant.getStatus()
            );
        }

        // 1. Cambiar estado a ACTIVE
        Merchant approved = Merchant.builder()
                .id(merchant.getId())
                .businessName(merchant.getBusinessName())
                .businessId(merchant.getBusinessId())
                .email(merchant.getEmail())
                .businessType(merchant.getBusinessType())
                .permission(merchant.getPermission())
                .status(MerchantStatus.ACTIVE)
                .build();
        Merchant saved = commerceRepository.save(approved);

        // 2. Registrar en la bitácora de auditoría
        merchantAuditPort.publish(MerchantAuditEvent.builder()
                .id(UUID.randomUUID().toString())
                .merchantId(merchantId)
                .adminEmail(adminEmail)
                .action(MerchantAuditAction.APPROVED)
                .reason(null) // La aprobación no requiere motivo
                .occurredAt(Instant.now())
                .build());

        // 3. Notificar al comercio por e-mail (mock)
        notificationPort.sendMerchantApprovalEmail(merchant.getEmail(), merchant.getBusinessName());

        return saved;
    }

    /**
     * Suspende un comercio activo por actividad irregular.
     *
     * <p>Flujo:</p>
     * <ol>
     *   <li>Verifica que el comercio exista y no esté ya suspendido.</li>
     *   <li>Revoca (inhabilita) todas las API Keys activas del comercio.</li>
     *   <li>Cambia el estado a {@code SUSPENDED}.</li>
     *   <li>Registra el evento en la bitácora con el motivo.</li>
     *   <li>Notifica al comercio (mock).</li>
     * </ol>
     *
     * @param merchantId ID del comercio a suspender
     * @param adminEmail e-mail del administrador que ejecuta la acción
     * @param reason     motivo de la suspensión (obligatorio)
     * @return el comercio actualizado con estado SUSPENDED
     */
    public Merchant suspendMerchant(String merchantId, String adminEmail, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("El motivo de suspensión es obligatorio");
        }

        Merchant merchant = commerceRepository.findById(merchantId)
                .orElseThrow(() -> new NoSuchElementException("Comercio no encontrado: " + merchantId));

        // Regla de negocio: no se puede suspender un comercio ya suspendido
        if (merchant.getStatus() == MerchantStatus.SUSPENDED) {
            throw new IllegalStateException("El comercio ya se encuentra suspendido");
        }

        // 1. Revocar todas las API Keys activas del comercio de forma inmediata
        List<ApiCredential> activeCredentials =
                credentialRepository.findByMerchantIdAndActiveTrue(merchantId);

        for (ApiCredential credential : activeCredentials) {
            credential.revoke();
            credentialRepository.save(credential);
        }

        // 2. Cambiar estado a SUSPENDED
        Merchant suspended = Merchant.builder()
                .id(merchant.getId())
                .businessName(merchant.getBusinessName())
                .businessId(merchant.getBusinessId())
                .email(merchant.getEmail())
                .businessType(merchant.getBusinessType())
                .permission(merchant.getPermission())
                .status(MerchantStatus.SUSPENDED)
                .build();
        Merchant saved = commerceRepository.save(suspended);

        // 3. Registrar en la bitácora de auditoría con el motivo
        merchantAuditPort.publish(MerchantAuditEvent.builder()
                .id(UUID.randomUUID().toString())
                .merchantId(merchantId)
                .adminEmail(adminEmail)
                .action(MerchantAuditAction.SUSPENDED)
                .reason(reason)
                .occurredAt(Instant.now())
                .build());

        // 4. Notificar al comercio (mock)
        notificationPort.sendMerchantSuspensionEmail(merchant.getEmail(), merchant.getBusinessName(), reason);

        return saved;
    }

    /**
     * Recupera la bitácora completa de decisiones administrativas de un comercio.
     *
     * @param merchantId ID del comercio
     * @return lista de eventos auditados (más reciente primero)
     */
    public List<MerchantAuditEvent> getMerchantAuditLog(String merchantId) {
        // Verificar que el comercio exista antes de consultar la bitácora
        commerceRepository.findById(merchantId)
                .orElseThrow(() -> new NoSuchElementException("Comercio no encontrado: " + merchantId));

        return merchantAuditPort.findByMerchantId(merchantId);
    }
}
