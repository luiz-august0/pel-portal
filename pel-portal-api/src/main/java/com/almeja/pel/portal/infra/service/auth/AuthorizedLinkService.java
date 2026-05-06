package com.almeja.pel.portal.infra.service.auth;

import com.almeja.pel.portal.core.domain.enums.EnumAuthorizedLinkType;
import com.almeja.pel.portal.core.dto.record.AuthorizedLinkGeneratedRecord;
import com.almeja.pel.portal.core.dto.record.AuthorizedTokenRecord;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.token.AuthorizedLinkGTW;
import com.almeja.pel.portal.core.util.DateUtil;
import com.almeja.pel.portal.infra.service.token.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Date;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthorizedLinkService implements AuthorizedLinkGTW {

    private final TokenService tokenService;

    @ConfigProperty(name = "app.url")
    String url;

    @Override
    public AuthorizedLinkGeneratedRecord generateResponsibleLink(String cpf) {
        return generateAuthorizedLink(
                cpf,
                EnumAuthorizedLinkType.RESPONSIBLE,
                EnumAppException.GENERATE_RESPONSIBLE_LINK
        );
    }

    @Override
    public AuthorizedTokenRecord validateToken(String token) {
        DecodedJWT decodedJWT = tokenService.validateAuthorizedLinkToken(token);

        return new AuthorizedTokenRecord(
                decodedJWT.getSubject(),
                true,
                EnumAuthorizedLinkType.valueOf(decodedJWT.getClaim(TokenService.AUTHORIZED_LINK_TYPE_CLAIM).asString())
        );
    }

    private AuthorizedLinkGeneratedRecord generateAuthorizedLink(String cpf,
                                                                 EnumAuthorizedLinkType authorizedLinkType,
                                                                 EnumAppException errorEnum) {
        Date generatedAt = DateUtil.getDate();
        Instant expirationDate = tokenService.genExpirationDateAuthorizedLinkToken();
        String token = tokenService.generateAuthorizedLinkToken(
                cpf,
                expirationDate,
                authorizedLinkType,
                errorEnum
        );

        return new AuthorizedLinkGeneratedRecord(
                token,
                generatedAt,
                Date.from(expirationDate));
    }

}
