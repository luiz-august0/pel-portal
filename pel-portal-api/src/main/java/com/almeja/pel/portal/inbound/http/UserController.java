package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.enums.EnumInternalRelationshipType;
import com.almeja.pel.portal.core.domain.usecase.user.*;
import com.almeja.pel.portal.core.dto.CreateUpdateAddressDTO;
import com.almeja.pel.portal.core.dto.UserDTO;
import com.almeja.pel.portal.core.dto.UserStatusDTO;
import com.almeja.pel.portal.core.dto.UserUpdateDTO;
import com.almeja.pel.portal.core.dto.record.AuthorizedLinkGeneratedRecord;
import com.almeja.pel.portal.core.dto.record.ChangePasswordRecord;
import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import com.almeja.pel.portal.inbound.http.interfaces.IUserController;
import com.almeja.pel.portal.infra.context.AuthContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserController implements IUserController {

    @Inject
    GenerateResponsibleLinkUC generateResponsibleLinkUC;

    @Inject
    UpdateUserUC updateUserUC;

    @Inject
    CreateUpdateAddressUC createUpdateAddressUC;

    @Inject
    UpdateInternalRelationshipTypeUC updateInternalRelationshipTypeUC;

    @Inject
    GetUserStatusUC getUserStatusUC;

    @Inject
    ChangePasswordUC changePasswordUC;

    @Inject
    GetCurrentUserUC getCurrentUserUC;

    @Inject
    AuthContext authContext;

    @Override
    public UserDTO getCurrentUser() {
        return ConverterEntityToDTOUtil.convert(getCurrentUserUC.execute(), UserDTO.class);
    }

    @Override
    public AuthorizedLinkGeneratedRecord regenerateResponsibleLink() {
        return generateResponsibleLinkUC.execute(authContext.getUser());
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        updateUserUC.execute(authContext.getUser(), userUpdateDTO);
    }

    @Override
    public void updateInternalRelationshipType(EnumInternalRelationshipType relationshipType) {
        updateInternalRelationshipTypeUC.execute(authContext.getUser(), relationshipType);
    }

    @Override
    public UUID createUpdateAddress(CreateUpdateAddressDTO createUpdateAddressDTO) {
        return createUpdateAddressUC.execute(authContext.getUser(), createUpdateAddressDTO);
    }

    @Override
    public List<UserStatusDTO> getUserStatus() {
        return getUserStatusUC.execute(authContext.getUser());
    }

    @Override
    public void changePassword(ChangePasswordRecord changePasswordRecord) {
        changePasswordUC.execute(authContext.getUser(), changePasswordRecord);
    }

}
