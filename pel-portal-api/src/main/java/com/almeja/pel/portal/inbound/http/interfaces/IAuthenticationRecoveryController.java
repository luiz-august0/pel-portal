package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryRecord;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path(IAuthenticationRecoveryController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IAuthenticationRecoveryController {

    String PATH = IAuthenticationController.PATH + "/recovery";

    @POST
    void generateRecovery(AuthenticationRecoveryRecord authenticationRecoveryRecord);

    @POST
    @Path("/password")
    void changePassword(AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord);

}
