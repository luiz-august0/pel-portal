package com.almeja.pel.portal.infra.repository;

import com.almeja.pel.portal.core.domain.entity.TemplateEmailEntity;
import com.almeja.pel.portal.core.gateway.repository.TemplateEmailRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TemplateEmailRepository extends JpaRepository<TemplateEmailEntity, UUID>, TemplateEmailRepositoryGTW {
}