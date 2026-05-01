package com.almeja.pel.portal.core.repository;

import com.almeja.pel.portal.core.domain.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, UUID> {

}
