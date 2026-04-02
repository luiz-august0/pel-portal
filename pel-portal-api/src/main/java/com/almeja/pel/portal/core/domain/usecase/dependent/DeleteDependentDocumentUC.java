package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.DeleteDocumentService;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDependentDocumentUC {

    private final DeleteDocumentService deleteDocumentService;
    private final VerifyDependentService verifyDependentService;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        dependentVerifiedRecord.userDependent().validateReviewed();
        deleteDocumentService.delete(dependentVerifiedRecord.userDependent(), documentType);
    }

}
