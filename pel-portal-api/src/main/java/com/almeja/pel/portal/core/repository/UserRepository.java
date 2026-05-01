package com.almeja.pel.portal.core.repository;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByCpf(String cpf);

    boolean existsByEmailAndIdIsNot(String email, UUID id);

    boolean existsByCpfAndIdIsNot(String cpf, UUID id);

    Optional<UserEntity> findById(UUID id);

}
