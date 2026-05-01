package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.domain.usecase.document.DownloadDocumentUC;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class DownloadDependentDocumentUC {

    @Inject
    VerifyDependentService verifyDependentService;
    @Inject
    DownloadDocumentUC downloadDocumentUC;

    public byte[] execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        return downloadDocumentUC.execute(dependentVerifiedRecord.userDependent(), documentType, false);
    }

}
