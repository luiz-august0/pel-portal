package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class GetDependentDocumentUC {

    @Inject
    VerifyDependentService verifyDependentService;
    @Inject
    DocumentRepositoryGTW documentRepositoryGTW;

    public DocumentEntity execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        return documentRepositoryGTW.findByUserAndDocumentType(dependentVerifiedRecord.userDependent(), documentType).orElse(null);
    }

}
