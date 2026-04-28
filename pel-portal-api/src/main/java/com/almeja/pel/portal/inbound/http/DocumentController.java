package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.usecase.document.DeleteDocumentUC;
import com.almeja.pel.portal.core.domain.usecase.document.DownloadDocumentUC;
import com.almeja.pel.portal.core.domain.usecase.document.GetDocumentUC;
import com.almeja.pel.portal.core.domain.usecase.document.UploadDocumentUC;
import com.almeja.pel.portal.core.dto.DocumentDTO;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import com.almeja.pel.portal.inbound.http.interfaces.IDocumentController;
import com.almeja.pel.portal.infra.context.AuthContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DocumentController implements IDocumentController {

    @Inject
    UploadDocumentUC uploadDocumentUC;

    @Inject
    DeleteDocumentUC deleteDocumentUC;

    @Inject
    GetDocumentUC getDocumentUC;

    @Inject
    DownloadDocumentUC downloadDocumentUC;

    @Inject
    AuthContext authContext;

    @Override
    public void upload(EnumDocumentType documentType, MultipartDTO multipartDTO) {
        uploadDocumentUC.execute(authContext.getUser(), documentType, multipartDTO, true);
    }

    @Override
    public void delete(EnumDocumentType documentType) {
        deleteDocumentUC.execute(authContext.getUser(), documentType, true);
    }

    @Override
    public DocumentDTO getDocument(EnumDocumentType documentType) {
        return ConverterEntityToDTOUtil.convert(getDocumentUC.execute(authContext.getUser(), documentType, true), DocumentDTO.class);
    }

    @Override
    public byte[] downloadDocument(EnumDocumentType documentType) {
        return downloadDocumentUC.execute(authContext.getUser(), documentType, true);
    }

}
