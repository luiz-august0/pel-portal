package com.almeja.pel.portal.infra.service.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthorizedUserRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.authorization.AuthorizationGTW;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.infra.service.token.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthorizationService implements AuthorizationGTW {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public AuthorizedUserRecord authorize(AuthenticateRecord authenticateRecord) {
        Optional<UserEntity> userOptional = userRepository.findByCpf(authenticateRecord.cpf());
        UserEntity user = userOptional.orElseThrow(() -> new AppException(EnumAppException.WRONG_CREDENTIALS));
        BCrypt.Result result = BCrypt.verifyer().verify(authenticateRecord.password().toCharArray(), user.getPassword());
        if (!result.verified) {
            throw new AppException(EnumAppException.WRONG_CREDENTIALS);
        }
        String accessToken = tokenService.generateToken(user.getCpf());
        return new AuthorizedUserRecord(user, accessToken);
    }

}
