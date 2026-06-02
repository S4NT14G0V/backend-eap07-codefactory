package com.codefactory.appstripe.identity.application.port;

import com.codefactory.appstripe.identity.domain.Merchant;
import com.codefactory.appstripe.identity.domain.MerchantStatus;

import java.util.List;
import java.util.Optional;

public interface ICommerceRepositoryPort {
    // Para la HU2 necesitamos encontrarlo por su ID interno
    Optional<Merchant> findById(String id);

    // Otros métodos que pide tu documentación para la HU1
    Merchant save(Merchant merchant);

    boolean existsByBusinessId(String businessId);

    boolean existsByEmail(String email);

    /**
     * Recupera los comercios que se encuentran en un estado específico.
     * Usado por el admin para listar comercios en PENDING_VERIFICATION.
     */
    List<Merchant> findByStatus(MerchantStatus status);

    /** Recupera todos los comercios registrados en la plataforma. */
    List<Merchant> findAll();
}