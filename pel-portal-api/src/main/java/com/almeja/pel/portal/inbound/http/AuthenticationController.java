package com.almeja.pel.portal.inbound.http;

import com.almeja.pel.portal.core.domain.usecase.user.AuthenticateUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticatedRecord;
import com.almeja.pel.portal.inbound.http.interfaces.IAuthenticationController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController implements IAuthenticationController {

    private final AuthenticateUC authenticateUC;
    private final RegisterUC registerUC;

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        registerUC.execute(userRegisterDTO);
    }

    @Override
    public AuthenticatedRecord authenticate(AuthenticateRecord authenticationRecord) {
        return authenticateUC.execute(authenticationRecord);
    }

}
