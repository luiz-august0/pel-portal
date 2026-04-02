package com.almeja.pel.gestao.infra.service.token;

import com.almeja.pel.gestao.core.util.StringUtil;
import com.auth0.jwt.JWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String sessionHeader = request.getHeader("Authorization");
        if (StringUtil.isNullOrEmpty(sessionHeader)) return null;
        return sessionHeader.replace("Bearer", "").trim();
    }

}