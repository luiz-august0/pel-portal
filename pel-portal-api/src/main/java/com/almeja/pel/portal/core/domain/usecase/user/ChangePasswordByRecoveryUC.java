package com.almeja.pel.portal.core.domain.usecase.user;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.gateway.token.RecoveryTokenGTW;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ChangePasswordByRecoveryUC {

    @Inject
    UserRepositoryGTW userRepositoryGTW;
    @Inject
    UserValidatorService userValidatorService;
    @Inject
    UserCryptPasswordGTW userCryptPasswordGTW;
    @Inject
    RecoveryTokenGTW recoveryTokenGTW;

    @Transactional
    public void execute(AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord) {
        userValidatorService.validatePassword(authenticationRecoveryPasswordRecord.password());
        String cpf = recoveryTokenGTW.validateRecoveryToken(authenticationRecoveryPasswordRecord.token());
        UserEntity user = userRepositoryGTW.findByCpf(cpf).orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
        user.setPassword(userCryptPasswordGTW.cryptPassword(authenticationRecoveryPasswordRecord.password()));
        userRepositoryGTW.save(user);
    }

}
