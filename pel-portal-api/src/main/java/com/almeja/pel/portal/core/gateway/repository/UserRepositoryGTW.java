package com.almeja.pel.portal.core.gateway.repository;

import com.almeja.pel.portal.core.domain.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryGTW {

    UserEntity save(UserEntity user);

    Optional<UserEntity> findByCpf(String cpf);

    boolean existsByEmailAndIdIsNot(String email, UUID id);
    
    boolean existsByCpfAndIdIsNot(String cpf, UUID id);

    Optional<UserEntity> findById(UUID id);

}
