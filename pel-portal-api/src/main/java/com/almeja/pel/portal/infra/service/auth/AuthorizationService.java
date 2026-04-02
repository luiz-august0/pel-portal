package com.almeja.pel.portal.infra.service.auth;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthorizedUserRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.authorization.AuthorizationGTW;
import com.almeja.pel.portal.infra.repository.UserRepository;
import com.almeja.pel.portal.infra.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements AuthorizationGTW {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthorizedUserRecord authorize(AuthenticateRecord authenticateRecord) {
        Optional<UserEntity> userOptional = userRepository.findByCpf(authenticateRecord.cpf());
        UserEntity user = userOptional.orElseThrow(() -> new AppException(EnumAppException.WRONG_CREDENTIALS));
        if (!passwordEncoder.matches(authenticateRecord.password(), user.getPassword())) {
            throw new AppException(EnumAppException.WRONG_CREDENTIALS);
        }
        String accessToken = tokenService.generateToken(user.getCpf());
        return new AuthorizedUserRecord(user, accessToken);
    }

}
