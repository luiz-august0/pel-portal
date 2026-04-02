package com.almeja.pel.portal.infra.interceptor;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.util.StringUtil;
import com.almeja.pel.portal.infra.context.AuthContext;
import com.almeja.pel.portal.infra.service.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * Interceptor responsável por setar o usuário autenticado no AuthContext
 * para cada requisição que contenha um token JWT válido.
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final UserRepositoryGTW userRepositoryGTW;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Extrair token da requisição
        String token = tokenService.getTokenFromRequest(request);
        if (StringUtil.isNotNullOrEmpty(token)) {
            // Extrair CPF
            String cpf = tokenService.getSubject(token);
            if (StringUtil.isNotNullOrEmpty(cpf)) {
                // Buscar usuário pelo CPF
                Optional<UserEntity> userOptional = userRepositoryGTW.findByCpf(cpf);
                if (userOptional.isPresent()) {
                    // Setar usuário no AuthContext
                    AuthContext.setUser(userOptional.get());
                    return true;
                }
            }
        }
        return false;
    }

    // Limpar o AuthContext após o processamento da requisição
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AuthContext.clear();
    }

}
