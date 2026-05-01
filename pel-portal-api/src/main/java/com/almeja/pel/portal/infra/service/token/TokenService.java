package com.almeja.pel.portal.infra.service.token;

import com.almeja.pel.portal.core.domain.enums.EnumAuthorizedLinkType;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.token.RecoveryTokenGTW;
import com.almeja.pel.portal.core.util.StringUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ApplicationScoped
public class TokenService implements RecoveryTokenGTW {

    @ConfigProperty(name = "api.security.token.secret")
    String secret;

    @ConfigProperty(name = "api.security.session-issuer")
    String sessionIssuer;

    @ConfigProperty(name = "api.security.recovery-session-issuer")
    String recoverySessionIssuer;

    @ConfigProperty(name = "api.security.authorized-link-issuer")
    String authorizedLinkIssuer;

    public static final String AUTHORIZED_LINK_TYPE_CLAIM = "authorizedLinkType";

    public String generateToken(String cpf) {
        return generateTokenWithIssuer(cpf, sessionIssuer, genExpirationDateAccessToken(), EnumAppException.GENERATE_TOKEN);
    }

    public String generateAuthorizedLinkToken(String cpf,
                                              Instant expirationDate,
                                              EnumAuthorizedLinkType authorizedLinkType,
                                              EnumAppException errorEnum) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(authorizedLinkIssuer)
                    .withSubject(cpf)
                    .withClaim("authorizedLinkType", authorizedLinkType.getKey())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new AppException(errorEnum);
        }
    }

    public String generateRecoveryToken(String cpf) {
        return generateTokenWithIssuer(cpf, recoverySessionIssuer, genExpirationDateRecoveryToken(), EnumAppException.GENERATE_RECOVERY_TOKEN);
    }

    public String validateRecoveryToken(String token) {
        return validateTokenWithIssuer(token, recoverySessionIssuer, e -> new AppException(EnumAppException.EXPIRED_RECOVERY));
    }

    public DecodedJWT validateAuthorizedLinkToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(authorizedLinkIssuer)
                    .build()
                    .verify(token);
        } catch (Exception e) {
            EnumAuthorizedLinkType authorizedLinkType = EnumAuthorizedLinkType.valueOf(decodedJWT.getClaim(AUTHORIZED_LINK_TYPE_CLAIM).asString());
            throw new AppException(authorizedLinkType.equals(EnumAuthorizedLinkType.RESPONSIBLE) ?
                    EnumAppException.EXPIRED_RESPONSIBLE_LINK :
                    EnumAppException.EXPIRED_DEPENDENT_LINK);
        }
    }

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    private String generateTokenWithIssuer(String cpf, String issuer, Instant expirationDate, EnumAppException errorEnum) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(cpf)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new AppException(errorEnum);
        }
    }

    private String validateTokenWithIssuer(String token, String issuer, ExceptionHandler exceptionHandler) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            throw exceptionHandler.handle(e);
        }
    }

    public Instant genExpirationDateAccessToken() {
        return LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.of("-03:00"));
    }

    public Instant genExpirationDateAuthorizedLinkToken() {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant genExpirationDateRecoveryToken() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

    @FunctionalInterface
    private interface ExceptionHandler {
        AppException handle(Exception e);
    }

}
