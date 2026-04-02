package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.infra.context.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetCurrentUserUC {

    private final UserRepositoryGTW userRepositoryGTW;

    public UserEntity execute() {
        Optional<UserEntity> userOptional = userRepositoryGTW.findById(AuthContext.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new AppException("Usuário nao encontrado");
        }
        UserEntity user = userOptional.get();
        if (!user.getActive()) {
            throw new AppException(EnumAppException.USER_INACTIVE);
        }
        return user;
    }

}
