package com.almeja.pel.portal.core.domain.usecase.document;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.UploadDocumentService;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class UploadDocumentUC {

    @Inject
    UploadDocumentService uploadDocumentService;
    @Inject
    UserRepositoryGTW userRepositoryGTW;
    @Inject
    Mediator mediator;

    @Transactional
    public void execute(UserEntity user, EnumDocumentType documentType, MultipartDTO multipartDTO, boolean validateMinor) {
        if (user.getUserDetails().isMinor() && validateMinor)
            throw new ValidatorException("Menor não pode fazer upload de documentos");
        user.validateReviewed();
        uploadDocumentService.upload(user, documentType, multipartDTO);
        user.setReviewed(false);
        userRepositoryGTW.save(user);
        mediator.send(new UpdateUserCommand(user));
    }

}
