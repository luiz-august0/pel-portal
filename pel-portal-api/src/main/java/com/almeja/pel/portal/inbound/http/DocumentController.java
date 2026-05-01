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
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@ApplicationScoped
@Path(PREFIX_PATH + "/document")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentController {

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
