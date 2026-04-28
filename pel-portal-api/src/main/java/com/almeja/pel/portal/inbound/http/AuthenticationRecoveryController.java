package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.usecase.user.ChangePasswordByRecoveryUC;
import com.almeja.pel.portal.core.domain.usecase.user.GenerateRecoveryUC;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryRecord;
import com.almeja.pel.portal.inbound.http.interfaces.IAuthenticationRecoveryController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthenticationRecoveryController implements IAuthenticationRecoveryController {

    @Inject
    GenerateRecoveryUC generateRecoveryUC;

    @Inject
    ChangePasswordByRecoveryUC changePasswordByRecoveryUC;

    @Override
    public void generateRecovery(AuthenticationRecoveryRecord authenticationRecoveryRecord) {
        generateRecoveryUC.execute(authenticationRecoveryRecord);
    }

    @Override
    public void changePassword(AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord) {
        changePasswordByRecoveryUC.execute(authenticationRecoveryPasswordRecord);
    }

}
