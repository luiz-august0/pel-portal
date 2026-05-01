package com.almeja.pel.portal.core.domain.service;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.dto.record.FileUploadedRecord;
import com.almeja.pel.portal.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.portal.core.repository.DocumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UploadDocumentService {

    @Inject
    FileHandlerGTW fileHandlerGTW;
    @Inject
    DocumentRepository documentRepository;
    @Inject
    DeleteDocumentService deleteDocumentService;

    @Transactional
    public void upload(UserEntity user, EnumDocumentType documentType, MultipartDTO multipartDTO) {
        FileUploadedRecord fileUploadedRecord = fileHandlerGTW.uploadFile(multipartDTO);
        DocumentEntity document = new DocumentEntity(user, documentType, multipartDTO.getFilename(), fileUploadedRecord.filename(),
                fileUploadedRecord.size(), fileUploadedRecord.s3File());
        deleteDocumentService.delete(user, documentType);
        documentRepository.save(document);
    }

}
