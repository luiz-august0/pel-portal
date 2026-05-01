package com.almeja.pel.portal.core.repository;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDependentRepository extends JpaRepository<UserDependentEntity, UUID> {

    Optional<UserDependentEntity> findByUserAndDependent(UserEntity user, UserEntity dependent);

    List<UserDependentEntity> findAllByUser(UserEntity user);

    Optional<UserDependentEntity> findByDependent(UserEntity dependent);

    void deleteById(UUID id);

}