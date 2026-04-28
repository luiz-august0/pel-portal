package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.*;
import com.almeja.pel.portal.infra.dto.DependentDTO;
import com.almeja.pel.portal.infra.dto.DependentsLinkedListDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@Path(IDependentController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IDependentController {

    String PATH = PREFIX_PATH + "/dependent";

    @POST
    @Path("/create")
    Response create(DependentCreateDTO dependentCreateDTO);

    @GET
    @Path("/list")
    DependentsLinkedListDTO getList();

    @GET
    @Path("/{id}/info")
    DependentDTO getInfo(@PathParam("id") UUID id);

    @POST
    @Path("/{id}/recognize")
    void recognize(@PathParam("id") UUID id,
                   @QueryParam("recognize") @DefaultValue("false") boolean recognize);

    @PUT
    @Path("/{id}/update")
    void updateDependent(@PathParam("id") UUID id,
                         UserUpdateDTO userUpdateDTO);

    @PUT
    @Path("/{id}/update-relationship-special-needs")
    void addRelationshipAndUpdateSpecialNeeds(@PathParam("id") UUID id,
                                              DependentRelationshipAndSpecialNeedsDTO dto);

    @POST
    @Path("/{id}/address")
    void createUpdateAddress(@PathParam("id") UUID id,
                             CreateUpdateDependentAddressDTO dto);

    @POST
    @Path("/{id}/document/upload")
    Response uploadDocument(@PathParam("id") UUID id,
                            @QueryParam("documentType") EnumDocumentType documentType,
                            MultipartDTO multipartDTO);

    @GET
    @Path("/{id}/document")
    DocumentDTO getDocument(@PathParam("id") UUID id,
                            @QueryParam("documentType") EnumDocumentType documentType);

    @GET
    @Path("/{id}/document/download")
    byte[] downloadDocument(@PathParam("id") UUID id,
                            @QueryParam("documentType") EnumDocumentType documentType);

    @DELETE
    @Path("/{id}/document/delete")
    void deleteDocument(@PathParam("id") UUID id,
                        @QueryParam("documentType") EnumDocumentType documentType);

    @GET
    @Path("/responsible")
    DependentDTO getResponsible();

}
