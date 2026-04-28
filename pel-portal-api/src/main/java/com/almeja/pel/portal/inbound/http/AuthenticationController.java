package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.usecase.user.AuthenticateUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticatedRecord;
import com.almeja.pel.portal.inbound.http.interfaces.IAuthenticationController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthenticationController implements IAuthenticationController {

    @Inject
    AuthenticateUC authenticateUC;

    @Inject
    RegisterUC registerUC;

    @Override
    public Response register(UserRegisterDTO userRegisterDTO) {
        registerUC.execute(userRegisterDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public AuthenticatedRecord authenticate(AuthenticateRecord authenticationRecord) {
        return authenticateUC.execute(authenticationRecord);
    }

}
