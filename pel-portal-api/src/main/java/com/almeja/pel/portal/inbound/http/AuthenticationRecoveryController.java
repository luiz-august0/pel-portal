package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.usecase.user.ChangePasswordByRecoveryUC;
import com.almeja.pel.portal.core.domain.usecase.user.GenerateRecoveryUC;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryRecord;
import com.almeja.pel.portal.inbound.http.interfaces.IAuthenticationRecoveryController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationRecoveryController implements IAuthenticationRecoveryController {

    private final GenerateRecoveryUC generateRecoveryUC;
    private final ChangePasswordByRecoveryUC changePasswordByRecoveryUC;

    @Override
    public void generateRecovery(AuthenticationRecoveryRecord authenticationRecoveryRecord) {
        generateRecoveryUC.execute(authenticationRecoveryRecord);
    }

    @Override
    public void changePassword(AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord) {
        changePasswordByRecoveryUC.execute(authenticationRecoveryPasswordRecord);
    }

}
