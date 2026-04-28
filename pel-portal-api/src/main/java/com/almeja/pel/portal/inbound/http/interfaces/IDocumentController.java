package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.DocumentDTO;
import com.almeja.pel.portal.core.dto.MultipartDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@Path(IDocumentController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IDocumentController {

    String PATH = PREFIX_PATH + "/document";

    @POST
    @Path("/upload")
    void upload(@QueryParam("documentType") EnumDocumentType documentType, MultipartDTO multipartDTO);

    @DELETE
    @Path("/delete")
    void delete(@QueryParam("documentType") EnumDocumentType documentType);

    @GET
    DocumentDTO getDocument(@QueryParam("documentType") EnumDocumentType documentType);

    @GET
    @Path("/download")
    byte[] downloadDocument(@QueryParam("documentType") EnumDocumentType documentType);

}
