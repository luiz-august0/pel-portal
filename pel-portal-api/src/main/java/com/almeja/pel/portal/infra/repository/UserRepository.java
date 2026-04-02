package com.almeja.pel.portal.infra.repository;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>, UserRepositoryGTW {

    Optional<UserEntity> findByCpf(String cpf);

    boolean existsByCpfAndIdIsNot(String cpf, UUID id);

}
