package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.dto.record.ChangePasswordRecord;
import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordUC {

    private final UserRepositoryGTW userRepositoryGTW;
    private final UserValidatorService userValidatorService;
    private final UserCryptPasswordGTW userCryptPasswordGTW;

    @Transactional
    public void execute(UserEntity user, ChangePasswordRecord changePasswordRecord) {
        userValidatorService.validatePassword(changePasswordRecord.password());
        user.setPassword(userCryptPasswordGTW.cryptPassword(changePasswordRecord.password()));
        userRepositoryGTW.save(user);
    }

}
