package com.almeja.pel.portal.core.domain.usecase.document;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDocumentUC {

    private final DocumentRepositoryGTW documentRepositoryGTW;

    public DocumentEntity execute(UserEntity user, EnumDocumentType documentType, boolean validateMinor) {
        if (user.getUserDetails().isMinor() && validateMinor)
            throw new ValidatorException("Menor não pode buscar documentos");
        return documentRepositoryGTW.findByUserAndDocumentType(user, documentType).orElse(null);
    }

}
