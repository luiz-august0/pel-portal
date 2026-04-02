package com.almeja.pel.gestao.infra.context;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;

public class AuthContext {

    private static final ThreadLocal<PersonEntity> userThreadLocal = new ThreadLocal<>();

    public static void setUser(PersonEntity user) {
        userThreadLocal.set(user);
    }

    public static void clear() {
        userThreadLocal.remove();
    }

    public static PersonEntity getUser() {
        return userThreadLocal.get();
    }

}
