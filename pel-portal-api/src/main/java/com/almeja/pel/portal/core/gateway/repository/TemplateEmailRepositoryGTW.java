package com.almeja.pel.portal.core.gateway.repository;

import com.almeja.pel.portal.core.domain.entity.TemplateEmailEntity;
import com.almeja.pel.portal.core.domain.enums.EnumTemplateEmail;

import java.util.Optional;

public interface TemplateEmailRepositoryGTW {

    Optional<TemplateEmailEntity> findFirstByTemplateEmail(EnumTemplateEmail template);

}