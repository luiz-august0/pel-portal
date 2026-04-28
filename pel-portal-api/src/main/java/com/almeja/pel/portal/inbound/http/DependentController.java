package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.domain.usecase.dependent.*;
import com.almeja.pel.portal.core.dto.*;
import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import com.almeja.pel.portal.inbound.http.interfaces.IDependentController;
import com.almeja.pel.portal.infra.context.AuthContext;
import com.almeja.pel.portal.infra.dto.DependentDTO;
import com.almeja.pel.portal.infra.dto.DependentsLinkedListDTO;
import com.almeja.pel.portal.infra.dto.mapper.DependentsLinkedListMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@ApplicationScoped
public class DependentController implements IDependentController {

    @Inject
    ListDependentsLinkedUC listDependentsLinkedUC;

    @Inject
    RecognizeDependentUC recognizeDependentUC;

    @Inject
    UpdateDependentUC updateDependentUC;

    @Inject
    AddRelationshipAndUpdateSpecialNeedsUC addRelationshipAndUpdateSpecialNeedsUC;

    @Inject
    CreateUpdateDependentAddressUC createUpdateDependentAddressUC;

    @Inject
    UploadDependentDocumentUC uploadDependentDocumentUC;

    @Inject
    GetDependentDocumentUC getDependentDocumentUC;

    @Inject
    DownloadDependentDocumentUC downloadDependentDocumentUC;

    @Inject
    DeleteDependentDocumentUC deleteDependentDocumentUC;

    @Inject
    GetDependentInfoUC getDependentInfoUC;

    @Inject
    CreateDependentUC createDependentUC;

    @Inject
    GetMinorResponsibleUC getMinorResponsibleUC;

    @Inject
    AuthContext authContext;

    @Override
    public Response create(DependentCreateDTO dependentCreateDTO) {
        UUID id = createDependentUC.execute(authContext.getUser(), dependentCreateDTO);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @Override
    public DependentsLinkedListDTO getList() {
        return DependentsLinkedListMapper.map(listDependentsLinkedUC.execute(authContext.getUser()));
    }

    @Override
    public DependentDTO getInfo(UUID id) {
        return ConverterEntityToDTOUtil.convert(getDependentInfoUC.execute(authContext.getUser(), id), DependentDTO.class);
    }

    @Override
    public void recognize(UUID dependentId, boolean recognize) {
        recognizeDependentUC.execute(dependentId, recognize, authContext.getUser());
    }

    @Override
    public void updateDependent(UUID id, UserUpdateDTO userUpdateDTO) {
        updateDependentUC.execute(authContext.getUser(), id, userUpdateDTO);
    }

    @Override
    public void addRelationshipAndUpdateSpecialNeeds(UUID id, DependentRelationshipAndSpecialNeedsDTO dto) {
        addRelationshipAndUpdateSpecialNeedsUC.execute(authContext.getUser(), id, dto);
    }

    @Override
    public void createUpdateAddress(UUID id, CreateUpdateDependentAddressDTO dto) {
        createUpdateDependentAddressUC.execute(authContext.getUser(), id, dto);
    }

    @Override
    public Response uploadDocument(UUID id, EnumDocumentType documentType, MultipartDTO multipartDTO) {
        uploadDependentDocumentUC.execute(authContext.getUser(), id, documentType, multipartDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public DocumentDTO getDocument(UUID id, EnumDocumentType documentType) {
        return ConverterEntityToDTOUtil.convert(getDependentDocumentUC.execute(authContext.getUser(), id, documentType), DocumentDTO.class);
    }

    @Override
    public byte[] downloadDocument(UUID id, EnumDocumentType documentType) {
        return downloadDependentDocumentUC.execute(authContext.getUser(), id, documentType);
    }

    @Override
    public void deleteDocument(UUID id, EnumDocumentType documentType) {
        deleteDependentDocumentUC.execute(authContext.getUser(), id, documentType);
    }

    @Override
    public DependentDTO getResponsible() {
        return ConverterEntityToDTOUtil.convert(getMinorResponsibleUC.execute(authContext.getUser()), DependentDTO.class);
    }

}
