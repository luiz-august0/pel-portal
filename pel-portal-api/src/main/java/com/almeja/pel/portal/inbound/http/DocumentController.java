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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DocumentController implements IDocumentController {

    private final UploadDocumentUC uploadDocumentUC;
    private final DeleteDocumentUC deleteDocumentUC;
    private final GetDocumentUC getDocumentUC;
    private final DownloadDocumentUC downloadDocumentUC;

    @Override
    public void upload(EnumDocumentType documentType, MultipartDTO multipartDTO) {
        uploadDocumentUC.execute(AuthContext.getUser(), documentType, multipartDTO, true);
    }

    @Override
    public void delete(EnumDocumentType documentType) {
        deleteDocumentUC.execute(AuthContext.getUser(), documentType, true);
    }

    @Override
    public DocumentDTO getDocument(EnumDocumentType documentType) {
        return ConverterEntityToDTOUtil.convert(getDocumentUC.execute(AuthContext.getUser(), documentType, true), DocumentDTO.class);
    }

    @Override
    public byte[] downloadDocument(EnumDocumentType documentType) {
        return downloadDocumentUC.execute(AuthContext.getUser(), documentType, true);
    }

}
