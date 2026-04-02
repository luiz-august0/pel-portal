package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDependentDocumentUC {

    private final VerifyDependentService verifyDependentService;
    private final DocumentRepositoryGTW documentRepositoryGTW;

    public DocumentEntity execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        return documentRepositoryGTW.findByUserAndDocumentType(dependentVerifiedRecord.userDependent(), documentType).orElse(null);
    }

}
