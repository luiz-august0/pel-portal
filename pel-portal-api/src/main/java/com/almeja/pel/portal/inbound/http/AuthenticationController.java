package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticatedRecord;
import com.almeja.pel.portal.core.domain.usecase.user.AuthenticateUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@ApplicationScoped
@Path(PREFIX_PATH + "/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationController {

    @Inject
    AuthenticateUC authenticateUC;

    @Inject
    RegisterUC registerUC;

    @POST
    @Path("/register")
    public Response register(UserRegisterDTO userRegisterDTO) {
        registerUC.execute(userRegisterDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    public AuthenticatedRecord authenticate(AuthenticateRecord authenticationRecord) {
        return authenticateUC.execute(authenticationRecord);
    }

}
