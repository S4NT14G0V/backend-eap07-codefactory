package com.codefactory.appstripe.security.infrastructure.persistence.repository;

import com.codefactory.appstripe.security.infrastructure.persistence.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserSpringRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByInvitationToken(String token);
    boolean existsByEmail(String email);
}
