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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DependentController implements IDependentController {

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

    @Override
    public UUID create(DependentCreateDTO dependentCreateDTO) {
        return createDependentUC.execute(AuthContext.getUser(), dependentCreateDTO);
    }

    @Override
    public DependentsLinkedListDTO getList() {
        return DependentsLinkedListMapper.map(listDependentsLinkedUC.execute(AuthContext.getUser()));
    }

    @Override
    public DependentDTO getInfo(UUID id) {
        return ConverterEntityToDTOUtil.convert(getDependentInfoUC.execute(AuthContext.getUser(), id), DependentDTO.class);
    }

    @Override
    public void recognize(UUID dependentId, boolean recognize) {
        recognizeDependentUC.execute(dependentId, recognize, AuthContext.getUser());
    }

    @Override
    public void updateDependent(UUID id, UserUpdateDTO userUpdateDTO) {
        updateDependentUC.execute(AuthContext.getUser(), id, userUpdateDTO);
    }

    @Override
    public void addRelationshipAndUpdateSpecialNeeds(UUID id, DependentRelationshipAndSpecialNeedsDTO dto) {
        addRelationshipAndUpdateSpecialNeedsUC.execute(AuthContext.getUser(), id, dto);
    }

    @Override
    public void createUpdateAddress(UUID id, CreateUpdateDependentAddressDTO dto) {
        createUpdateDependentAddressUC.execute(AuthContext.getUser(), id, dto);
    }

    @Override
    public void uploadDocument(UUID id, EnumDocumentType documentType, MultipartDTO multipartDTO) {
        uploadDependentDocumentUC.execute(AuthContext.getUser(), id, documentType, multipartDTO);
    }

    @Override
    public DocumentDTO getDocument(UUID id, EnumDocumentType documentType) {
        return ConverterEntityToDTOUtil.convert(getDependentDocumentUC.execute(AuthContext.getUser(), id, documentType), DocumentDTO.class);
    }

    @Override
    public byte[] downloadDocument(UUID id, EnumDocumentType documentType) {
        return downloadDependentDocumentUC.execute(AuthContext.getUser(), id, documentType);
    }

    @Override
    public void deleteDocument(UUID id, EnumDocumentType documentType) {
        deleteDependentDocumentUC.execute(AuthContext.getUser(), id, documentType);
    }

    @Override
    public DependentDTO getResponsible() {
        return ConverterEntityToDTOUtil.convert(getMinorResponsibleUC.execute(AuthContext.getUser()), DependentDTO.class);
    }

}
