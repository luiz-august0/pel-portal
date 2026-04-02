package com.almeja.pel.portal.core.domain.service;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.dto.record.FileUploadedRecord;
import com.almeja.pel.portal.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UploadDocumentService {

    private final FileHandlerGTW fileHandlerGTW;
    private final DocumentRepositoryGTW documentRepositoryGTW;
    private final DeleteDocumentService deleteDocumentService;

    @Transactional
    public void upload(UserEntity user, EnumDocumentType documentType, MultipartDTO multipartDTO) {
        FileUploadedRecord fileUploadedRecord = fileHandlerGTW.uploadFile(multipartDTO);
        DocumentEntity document = new DocumentEntity(user, documentType, multipartDTO.getFilename(), fileUploadedRecord.filename(),
                fileUploadedRecord.size(), fileUploadedRecord.s3File());
        deleteDocumentService.delete(user, documentType);
        documentRepositoryGTW.save(document);
    }

}
