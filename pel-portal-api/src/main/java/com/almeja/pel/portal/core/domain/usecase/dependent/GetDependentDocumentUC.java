package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.repository.DocumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class GetDependentDocumentUC {

    private final VerifyDependentService verifyDependentService;
    private final DocumentRepository documentRepository;

    public DocumentEntity execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        return documentRepository.findByUserAndDocumentType(dependentVerifiedRecord.userDependent(), documentType).orElse(null);
    }

}
