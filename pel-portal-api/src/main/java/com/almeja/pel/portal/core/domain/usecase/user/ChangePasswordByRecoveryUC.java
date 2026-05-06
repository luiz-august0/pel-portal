package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import com.almeja.pel.portal.core.gateway.token.RecoveryTokenGTW;
import com.almeja.pel.portal.core.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ChangePasswordByRecoveryUC {

    private final UserRepository userRepository;
    private final UserValidatorService userValidatorService;
    private final UserCryptPasswordGTW userCryptPasswordGTW;
    private final RecoveryTokenGTW recoveryTokenGTW;

    @Transactional
    public void execute(AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord) {
        userValidatorService.validatePassword(authenticationRecoveryPasswordRecord.password());
        String cpf = recoveryTokenGTW.validateRecoveryToken(authenticationRecoveryPasswordRecord.token());
        UserEntity user = userRepository.findByCpf(cpf).orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
        user.setPassword(userCryptPasswordGTW.cryptPassword(authenticationRecoveryPasswordRecord.password()));
        userRepository.save(user);
    }

}
