package com.almeja.pel.portal.core.domain.usecase.user;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.dto.record.ChangePasswordRecord;
import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ChangePasswordUC {

    @Inject
    UserRepositoryGTW userRepositoryGTW;
    @Inject
    UserValidatorService userValidatorService;
    @Inject
    UserCryptPasswordGTW userCryptPasswordGTW;

    @Transactional
    public void execute(UserEntity user, ChangePasswordRecord changePasswordRecord) {
        userValidatorService.validatePassword(changePasswordRecord.password());
        user.setPassword(userCryptPasswordGTW.cryptPassword(changePasswordRecord.password()));
        userRepositoryGTW.save(user);
    }

}
