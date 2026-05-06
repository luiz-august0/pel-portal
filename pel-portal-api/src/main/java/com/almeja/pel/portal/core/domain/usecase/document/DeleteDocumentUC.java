package com.almeja.pel.portal.core.domain.usecase.document;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.service.DeleteDocumentService;
import com.almeja.pel.portal.core.exception.ValidatorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DeleteDocumentUC {

    private final DeleteDocumentService deleteDocumentService;

    @Transactional
    public void execute(UserEntity user, EnumDocumentType documentType, boolean validateMinor) {
        if (user.getUserDetails().isMinor() && validateMinor)
            throw new ValidatorException("Menor não pode deletar documentos");
        user.validateReviewed();
        deleteDocumentService.delete(user, documentType);
    }

}
