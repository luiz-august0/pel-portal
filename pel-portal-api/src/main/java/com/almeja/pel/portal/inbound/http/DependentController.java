package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.usecase.dependent.*;
import com.almeja.pel.portal.core.dto.*;
import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import com.almeja.pel.portal.infra.context.AuthContext;
import com.almeja.pel.portal.infra.dto.DependentDTO;
import com.almeja.pel.portal.infra.dto.DependentsLinkedListDTO;
import com.almeja.pel.portal.infra.dto.mapper.DependentsLinkedListMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@ApplicationScoped
@RequiredArgsConstructor
@Path(PREFIX_PATH + "/dependent")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DependentController {

    private final ListDependentsLinkedUC listDependentsLinkedUC;
    private final RecognizeDependentUC recognizeDependentUC;
    private final UpdateDependentUC updateDependentUC;
    private final AddRelationshipAndUpdateSpecialNeedsUC addRelationshipAndUpdateSpecialNeedsUC;
    private final CreateUpdateDependentAddressUC createUpdateDependentAddressUC;
    private final UploadDependentDocumentUC uploadDependentDocumentUC;
    private final GetDependentDocumentUC getDependentDocumentUC;
    private final DownloadDependentDocumentUC downloadDependentDocumentUC;
    private final DeleteDependentDocumentUC deleteDependentDocumentUC;
    private final GetDependentInfoUC getDependentInfoUC;
    private final CreateDependentUC createDependentUC;
    private final GetMinorResponsibleUC getMinorResponsibleUC;
    private final AuthContext authContext;

    @POST
    @Path("/create")
    public Response create(DependentCreateDTO dependentCreateDTO) {
        UUID id = createDependentUC.execute(authContext.getUser(), dependentCreateDTO);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @GET
    @Path("/list")
    public DependentsLinkedListDTO getList() {
        return DependentsLinkedListMapper.map(listDependentsLinkedUC.execute(authContext.getUser()));
    }

    @GET
    @Path("/{id}/info")
    public DependentDTO getInfo(@PathParam("id") UUID id) {
        return ConverterEntityToDTOUtil.convert(getDependentInfoUC.execute(authContext.getUser(), id), DependentDTO.class);
    }

    @POST
    @Path("/{id}/recognize")
    public void recognize(@PathParam("id") UUID id,
                          @QueryParam("recognize") @DefaultValue("false") boolean recognize) {
        recognizeDependentUC.execute(id, recognize, authContext.getUser());
    }

    @PUT
    @Path("/{id}/update")
    public void updateDependent(@PathParam("id") UUID id,
                                UserUpdateDTO userUpdateDTO) {
        updateDependentUC.execute(authContext.getUser(), id, userUpdateDTO);
    }

    @PUT
    @Path("/{id}/update-relationship-special-needs")
    public void addRelationshipAndUpdateSpecialNeeds(@PathParam("id") UUID id,
                                                     DependentRelationshipAndSpecialNeedsDTO dto) {
        addRelationshipAndUpdateSpecialNeedsUC.execute(authContext.getUser(), id, dto);
    }

    @POST
    @Path("/{id}/address")
    public void createUpdateAddress(@PathParam("id") UUID id,
                                    CreateUpdateDependentAddressDTO dto) {
        createUpdateDependentAddressUC.execute(authContext.getUser(), id, dto);
    }

    @POST
    @Path("/{id}/document/upload")
    public Response uploadDocument(@PathParam("id") UUID id,
                                   @QueryParam("documentType") EnumDocumentType documentType,
                                   MultipartDTO multipartDTO) {
        uploadDependentDocumentUC.execute(authContext.getUser(), id, documentType, multipartDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}/document")
    public DocumentDTO getDocument(@PathParam("id") UUID id,
                                   @QueryParam("documentType") EnumDocumentType documentType) {
        return ConverterEntityToDTOUtil.convert(getDependentDocumentUC.execute(authContext.getUser(), id, documentType), DocumentDTO.class);
    }

    @GET
    @Path("/{id}/document/download")
    public byte[] downloadDocument(@PathParam("id") UUID id,
                                   @QueryParam("documentType") EnumDocumentType documentType) {
        return downloadDependentDocumentUC.execute(authContext.getUser(), id, documentType);
    }

    @DELETE
    @Path("/{id}/document/delete")
    public void deleteDocument(@PathParam("id") UUID id,
                               @QueryParam("documentType") EnumDocumentType documentType) {
        deleteDependentDocumentUC.execute(authContext.getUser(), id, documentType);
    }

    @GET
    @Path("/responsible")
    public DependentDTO getResponsible() {
        return ConverterEntityToDTOUtil.convert(getMinorResponsibleUC.execute(authContext.getUser()), DependentDTO.class);
    }

}
