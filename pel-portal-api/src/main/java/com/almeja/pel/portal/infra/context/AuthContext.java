package com.almeja.pel.portal.infra.context;

import com.almeja.pel.portal.core.domain.entity.UserEntity;

public class AuthContext {

    private static final ThreadLocal<UserEntity> userThreadLocal = new ThreadLocal<>();

    public static void setUser(UserEntity user) {
        userThreadLocal.set(user);
    }

    public static void clear() {
        userThreadLocal.remove();
    }

    public static UserEntity getUser() {
        return userThreadLocal.get();
    }

}
