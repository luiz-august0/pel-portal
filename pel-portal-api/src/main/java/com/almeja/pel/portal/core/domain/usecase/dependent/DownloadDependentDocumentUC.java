package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.domain.usecase.document.DownloadDocumentUC;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DownloadDependentDocumentUC {

    private final VerifyDependentService verifyDependentService;
    private final DownloadDocumentUC downloadDocumentUC;

    public byte[] execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        return downloadDocumentUC.execute(dependentVerifiedRecord.userDependent(), documentType, false);
    }

}
