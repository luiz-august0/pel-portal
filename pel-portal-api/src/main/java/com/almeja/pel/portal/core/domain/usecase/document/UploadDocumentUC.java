package com.almeja.pel.portal.core.domain.usecase.document;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.UploadDocumentService;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@ApplicationScoped
@RequiredArgsConstructor
public class UploadDocumentUC {

    private final UploadDocumentService uploadDocumentService;
    private final UserRepository userRepository;
    private final Mediator mediator;

    @Transactional
    public void execute(UserEntity user, EnumDocumentType documentType, MultipartDTO multipartDTO, boolean validateMinor) {
        if (user.getUserDetails().isMinor() && validateMinor)
            throw new ValidatorException("Menor não pode fazer upload de documentos");
        user.validateReviewed();
        uploadDocumentService.upload(user, documentType, multipartDTO);
        user.setReviewed(false);
        userRepository.save(user);
        mediator.send(new UpdateUserCommand(user));
    }

}
