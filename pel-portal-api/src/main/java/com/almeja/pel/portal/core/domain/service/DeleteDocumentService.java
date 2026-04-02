package com.almeja.pel.portal.core.domain.service;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteDocumentService {

    private final FileHandlerGTW fileHandlerGTW;
    private final DocumentRepositoryGTW documentRepositoryGTW;

    @Transactional
    public void delete(UserEntity user, EnumDocumentType documentType) {
        Optional<DocumentEntity> oldDocumentOptional = documentRepositoryGTW.findByUserAndDocumentType(user, documentType);
        if (oldDocumentOptional.isPresent()) {
            DocumentEntity oldDocument = oldDocumentOptional.get();
            fileHandlerGTW.deleteFile(oldDocument.getFilename(), oldDocument.getS3File());
            documentRepositoryGTW.delete(oldDocument);
        }
    }

}
