package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.infra.context.AuthContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class GetCurrentUserUC {

    private final UserRepository userRepository;
    private final AuthContext authContext;

    public UserEntity execute() {
        Optional<UserEntity> userOptional = userRepository.findById(authContext.getUser().getId());
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
