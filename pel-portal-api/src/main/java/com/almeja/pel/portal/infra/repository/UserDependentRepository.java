package com.almeja.pel.portal.infra.repository;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDependentRepository extends JpaRepository<UserDependentEntity, UUID>, UserDependentRepositoryGTW {
}