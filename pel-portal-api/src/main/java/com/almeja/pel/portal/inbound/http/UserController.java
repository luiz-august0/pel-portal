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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserController implements IUserController {

    private final GenerateResponsibleLinkUC generateResponsibleLinkUC;
    private final UpdateUserUC updateUserUC;
    private final CreateUpdateAddressUC createUpdateAddressUC;
    private final UpdateInternalRelationshipTypeUC updateInternalRelationshipTypeUC;
    private final GetUserStatusUC getUserStatusUC;
    private final ChangePasswordUC changePasswordUC;
    private final GetCurrentUserUC getCurrentUserUC;

    @Override
    public UserDTO getCurrentUser() {
        return ConverterEntityToDTOUtil.convert(getCurrentUserUC.execute(), UserDTO.class);
    }

    @Override
    public AuthorizedLinkGeneratedRecord regenerateResponsibleLink() {
        return generateResponsibleLinkUC.execute(AuthContext.getUser());
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        updateUserUC.execute(AuthContext.getUser(), userUpdateDTO);
    }

    @Override
    public void updateInternalRelationshipType(EnumInternalRelationshipType relationshipType) {
        updateInternalRelationshipTypeUC.execute(AuthContext.getUser(), relationshipType);
    }

    @Override
    public UUID createUpdateAddress(CreateUpdateAddressDTO createUpdateAddressDTO) {
        return createUpdateAddressUC.execute(AuthContext.getUser(), createUpdateAddressDTO);
    }

    @Override
    public List<UserStatusDTO> getUserStatus() {
        return getUserStatusUC.execute(AuthContext.getUser());
    }

    @Override
    public void changePassword(ChangePasswordRecord changePasswordRecord) {
        changePasswordUC.execute(AuthContext.getUser(), changePasswordRecord);
    }

}
