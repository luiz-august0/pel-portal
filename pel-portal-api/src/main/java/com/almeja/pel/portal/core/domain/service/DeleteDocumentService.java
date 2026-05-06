package com.almeja.pel.portal.core.domain.service;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.portal.core.repository.DocumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class DeleteDocumentService {

    private final FileHandlerGTW fileHandlerGTW;
    private final DocumentRepository documentRepository;

    @Transactional
    public void delete(UserEntity user, EnumDocumentType documentType) {
        Optional<DocumentEntity> oldDocumentOptional = documentRepository.findByUserAndDocumentType(user, documentType);
        if (oldDocumentOptional.isPresent()) {
            DocumentEntity oldDocument = oldDocumentOptional.get();
            fileHandlerGTW.deleteFile(oldDocument.getFilename(), oldDocument.getS3File());
            documentRepository.delete(oldDocument);
        }
    }

}
