package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.gateway.token.RecoveryTokenGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordByRecoveryUC {

    private final UserRepositoryGTW userRepositoryGTW;
    private final UserValidatorService userValidatorService;
    private final UserCryptPasswordGTW userCryptPasswordGTW;
    private final RecoveryTokenGTW recoveryTokenGTW;

    @Transactional
    public void execute(AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord) {
        userValidatorService.validatePassword(authenticationRecoveryPasswordRecord.password());
        String cpf = recoveryTokenGTW.validateRecoveryToken(authenticationRecoveryPasswordRecord.token());
        UserEntity user = userRepositoryGTW.findByCpf(cpf).orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
        user.setPassword(userCryptPasswordGTW.cryptPassword(authenticationRecoveryPasswordRecord.password()));
        userRepositoryGTW.save(user);
    }

}
