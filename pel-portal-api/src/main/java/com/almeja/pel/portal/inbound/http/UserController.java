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
import com.almeja.pel.portal.infra.context.AuthContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@ApplicationScoped
@Path(PREFIX_PATH + "/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

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

    @GET
    @Path("/current")
    public UserDTO getCurrentUser() {
        return ConverterEntityToDTOUtil.convert(getCurrentUserUC.execute(), UserDTO.class);
    }

    @POST
    @Path("/regenerate-responsible-link")
    public AuthorizedLinkGeneratedRecord regenerateResponsibleLink() {
        return generateResponsibleLinkUC.execute(authContext.getUser());
    }

    @PUT
    @Path("/update")
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        updateUserUC.execute(authContext.getUser(), userUpdateDTO);
    }

    @PATCH
    @Path("/update-internal-relationship-type")
    public void updateInternalRelationshipType(@QueryParam("relationshipType") EnumInternalRelationshipType relationshipType) {
        updateInternalRelationshipTypeUC.execute(authContext.getUser(), relationshipType);
    }

    @POST
    @Path("/address")
    public UUID createUpdateAddress(CreateUpdateAddressDTO createUpdateAddressDTO) {
        return createUpdateAddressUC.execute(authContext.getUser(), createUpdateAddressDTO);
    }

    @GET
    @Path("/status")
    public List<UserStatusDTO> getUserStatus() {
        return getUserStatusUC.execute(authContext.getUser());
    }

    @POST
    @Path("/change-password")
    public void changePassword(ChangePasswordRecord changePasswordRecord) {
        changePasswordUC.execute(authContext.getUser(), changePasswordRecord);
    }

}
