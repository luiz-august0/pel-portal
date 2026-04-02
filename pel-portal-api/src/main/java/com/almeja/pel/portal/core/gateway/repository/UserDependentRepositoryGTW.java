package com.almeja.pel.portal.core.gateway.repository;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDependentRepositoryGTW {

    UserDependentEntity save(UserDependentEntity userDependentEntity);

    Optional<UserDependentEntity> findByUserAndDependent(UserEntity user, UserEntity dependent);

    List<UserDependentEntity> findAllByUser(UserEntity user);

    Optional<UserDependentEntity> findByDependent(UserEntity dependent);

    void deleteById(UUID id);

}
