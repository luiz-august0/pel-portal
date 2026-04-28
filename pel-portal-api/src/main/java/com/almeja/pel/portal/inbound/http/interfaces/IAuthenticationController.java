package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticatedRecord;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@Path(IAuthenticationController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IAuthenticationController {

    String PATH = PREFIX_PATH + "/auth";

    @POST
    @Path("/register")
    Response register(UserRegisterDTO userRegisterDTO);

    @POST
    @Path("/login")
    AuthenticatedRecord authenticate(AuthenticateRecord authenticationRecord);

}
