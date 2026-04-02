package com.almeja.pel.gestao.infra.util;

import com.almeja.pel.gestao.core.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String getTokenFromRequest(HttpServletRequest request) {
        String sessionHeader = request.getHeader("Authorization");
        if (StringUtil.isNullOrEmpty(sessionHeader)) return null;

        return sessionHeader.replace("Bearer", "").trim();
    }

}
