package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.usecase.user.ChangePasswordByRecoveryUC;
import com.almeja.pel.portal.core.domain.usecase.user.GenerateRecoveryUC;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@ApplicationScoped
@RequiredArgsConstructor
@Path(PREFIX_PATH + "/auth/recovery")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationRecoveryController {

    private final GenerateRecoveryUC generateRecoveryUC;
    private final ChangePasswordByRecoveryUC changePasswordByRecoveryUC;

    @POST
    public void generateRecovery(AuthenticationRecoveryRecord authenticationRecoveryRecord) {
        generateRecoveryUC.execute(authenticationRecoveryRecord);
    }

    @POST
    @Path("/password")
    public void changePassword(AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord) {
        changePasswordByRecoveryUC.execute(authenticationRecoveryPasswordRecord);
    }

}
