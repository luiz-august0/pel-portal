package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.domain.enums.EnumInternalRelationshipType;
import com.almeja.pel.portal.core.dto.CreateUpdateAddressDTO;
import com.almeja.pel.portal.core.dto.UserDTO;
import com.almeja.pel.portal.core.dto.UserStatusDTO;
import com.almeja.pel.portal.core.dto.UserUpdateDTO;
import com.almeja.pel.portal.core.dto.record.AuthorizedLinkGeneratedRecord;
import com.almeja.pel.portal.core.dto.record.ChangePasswordRecord;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@Path(IUserController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IUserController {

    String PATH = PREFIX_PATH + "/user";

    @GET
    @Path("/current")
    UserDTO getCurrentUser();

    @POST
    @Path("/regenerate-responsible-link")
    AuthorizedLinkGeneratedRecord regenerateResponsibleLink();

    @PUT
    @Path("/update")
    void updateUser(UserUpdateDTO userUpdateDTO);

    @PATCH
    @Path("/update-internal-relationship-type")
    void updateInternalRelationshipType(@QueryParam("relationshipType") EnumInternalRelationshipType relationshipType);

    @POST
    @Path("/address")
    UUID createUpdateAddress(CreateUpdateAddressDTO createUpdateAddressDTO);

    @GET
    @Path("/status")
    List<UserStatusDTO> getUserStatus();

    @POST
    @Path("/change-password")
    void changePassword(ChangePasswordRecord changePasswordRecord);

}
