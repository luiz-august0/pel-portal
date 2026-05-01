package com.almeja.pel.portal.infra.interceptor;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.util.StringUtil;
import com.almeja.pel.portal.infra.context.AuthContext;
import com.almeja.pel.portal.infra.service.token.TokenService;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Optional;

@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class AuthInterceptor implements ContainerRequestFilter {

    @Inject
    TokenService tokenService;

    @Inject
    UserRepository userRepository;

    @Inject
    AuthContext authContext;

    private static final String[] EXCLUDED_PATHS = {
            "/auth", "/v3/api-docs", "/swagger-ui", "/openapi", "/q/health", "/q/openapi"
    };

    @Override
    public void filter(ContainerRequestContext ctx) {
        String path = ctx.getUriInfo().getPath();

        for (String excluded : EXCLUDED_PATHS) {
            if (path.contains(excluded)) {
                return;
            }
        }

        String authHeader = ctx.getHeaderString("Authorization");
        if (StringUtil.isNullOrEmpty(authHeader)) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authHeader.replace("Bearer", "").trim();
        if (StringUtil.isNullOrEmpty(token)) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String cpf;
        try {
            cpf = tokenService.getSubject(token);
        } catch (Exception e) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        if (StringUtil.isNullOrEmpty(cpf)) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        Optional<UserEntity> userOptional = userRepository.findByCpf(cpf);
        if (userOptional.isEmpty()) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        authContext.setUser(userOptional.get());
    }

}
