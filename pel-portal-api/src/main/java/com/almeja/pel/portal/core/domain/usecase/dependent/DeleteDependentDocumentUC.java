package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.DeleteDocumentService;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class DeleteDependentDocumentUC {

    @Inject
    DeleteDocumentService deleteDocumentService;
    @Inject
    VerifyDependentService verifyDependentService;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        dependentVerifiedRecord.userDependent().validateReviewed();
        deleteDocumentService.delete(dependentVerifiedRecord.userDependent(), documentType);
    }

}
