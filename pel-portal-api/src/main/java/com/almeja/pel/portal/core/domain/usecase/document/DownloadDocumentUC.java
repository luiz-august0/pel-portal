package com.almeja.pel.portal.core.domain.usecase.document;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DownloadDocumentUC {

    private final DocumentRepositoryGTW documentRepositoryGTW;
    private final FileHandlerGTW fileHandlerGTW;

    public byte[] execute(UserEntity user, EnumDocumentType documentType, boolean validateMinor) {
        if (user.getUserDetails().isMinor() && validateMinor)
            throw new ValidatorException("Menor não pode baixar documentos");
        Optional<DocumentEntity> documentOptional = documentRepositoryGTW.findByUserAndDocumentType(user, documentType);
        if (documentOptional.isPresent()) {
            DocumentEntity document = documentOptional.get();
            return fileHandlerGTW.getFile(document.getFilename(), document.getS3File());
        }
        return null;
    }

}
