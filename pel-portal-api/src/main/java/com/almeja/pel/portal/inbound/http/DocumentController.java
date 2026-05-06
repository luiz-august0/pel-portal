package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.usecase.document.DeleteDocumentUC;
import com.almeja.pel.portal.core.domain.usecase.document.DownloadDocumentUC;
import com.almeja.pel.portal.core.domain.usecase.document.GetDocumentUC;
import com.almeja.pel.portal.core.domain.usecase.document.UploadDocumentUC;
import com.almeja.pel.portal.core.dto.DocumentDTO;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import com.almeja.pel.portal.infra.context.AuthContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@ApplicationScoped
@RequiredArgsConstructor
@Path(PREFIX_PATH + "/document")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentController {

    private final UploadDocumentUC uploadDocumentUC;
    private final DeleteDocumentUC deleteDocumentUC;
    private final GetDocumentUC getDocumentUC;
    private final DownloadDocumentUC downloadDocumentUC;
    private final AuthContext authContext;

    @POST
    @Path("/upload")
    public void upload(@QueryParam("documentType") EnumDocumentType documentType, MultipartDTO multipartDTO) {
        uploadDocumentUC.execute(authContext.getUser(), documentType, multipartDTO, true);
    }

    @DELETE
    @Path("/delete")
    public void delete(@QueryParam("documentType") EnumDocumentType documentType) {
        deleteDocumentUC.execute(authContext.getUser(), documentType, true);
    }

    @GET
    public DocumentDTO getDocument(@QueryParam("documentType") EnumDocumentType documentType) {
        return ConverterEntityToDTOUtil.convert(getDocumentUC.execute(authContext.getUser(), documentType, true), DocumentDTO.class);
    }

    @GET
    @Path("/download")
    public byte[] downloadDocument(@QueryParam("documentType") EnumDocumentType documentType) {
        return downloadDocumentUC.execute(authContext.getUser(), documentType, true);
    }

}
