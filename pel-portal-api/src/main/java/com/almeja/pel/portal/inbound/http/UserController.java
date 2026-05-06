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
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@ApplicationScoped
@RequiredArgsConstructor
@Path(PREFIX_PATH + "/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final GenerateResponsibleLinkUC generateResponsibleLinkUC;
    private final UpdateUserUC updateUserUC;
    private final CreateUpdateAddressUC createUpdateAddressUC;
    private final UpdateInternalRelationshipTypeUC updateInternalRelationshipTypeUC;
    private final GetUserStatusUC getUserStatusUC;
    private final ChangePasswordUC changePasswordUC;
    private final GetCurrentUserUC getCurrentUserUC;
    private final AuthContext authContext;

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
