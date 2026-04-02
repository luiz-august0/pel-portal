package com.almeja.pel.portal.infra.repository;

import com.almeja.pel.portal.core.domain.entity.AddressEntity;
import com.almeja.pel.portal.core.gateway.repository.AddressRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID>, AddressRepositoryGTW {
}
