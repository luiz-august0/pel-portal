package com.almeja.pel.portal.core.repository;

import com.almeja.pel.portal.core.domain.entity.TemplateEmailEntity;
import com.almeja.pel.portal.core.domain.enums.EnumTemplateEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemplateEmailRepository extends JpaRepository<TemplateEmailEntity, UUID> {

    Optional<TemplateEmailEntity> findFirstByTemplateEmail(EnumTemplateEmail template);

}