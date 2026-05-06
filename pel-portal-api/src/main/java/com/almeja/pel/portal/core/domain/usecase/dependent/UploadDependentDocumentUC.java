package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.UploadDocumentService;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@ApplicationScoped
@RequiredArgsConstructor
public class UploadDependentDocumentUC {

    private final VerifyDependentService verifyDependentService;
    private final UploadDocumentService uploadDocumentService;
    private final Mediator mediator;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType, MultipartDTO multipartDTO) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        dependentVerifiedRecord.userDependent().validateReviewed();
        uploadDocumentService.upload(dependentVerifiedRecord.userDependent(), documentType, multipartDTO);
        mediator.send(new UpdateUserCommand(dependentVerifiedRecord.userDependent()));
    }

}
