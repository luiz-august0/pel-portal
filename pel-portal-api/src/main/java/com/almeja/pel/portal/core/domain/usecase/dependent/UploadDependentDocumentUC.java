package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
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

import java.util.UUID;


@ApplicationScoped
public class UploadDependentDocumentUC {

    @Inject
    VerifyDependentService verifyDependentService;
    @Inject
    UploadDocumentService uploadDocumentService;
    @Inject
    Mediator mediator;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, EnumDocumentType documentType, MultipartDTO multipartDTO) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        dependentVerifiedRecord.userDependent().validateReviewed();
        uploadDocumentService.upload(dependentVerifiedRecord.userDependent(), documentType, multipartDTO);
        mediator.send(new UpdateUserCommand(dependentVerifiedRecord.userDependent()));
    }

}
