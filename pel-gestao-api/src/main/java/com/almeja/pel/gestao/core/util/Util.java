package com.almeja.pel.gestao.core.util;

public class Util {

    public static Object nvl(Object value, Object defaultValue) {
        return value != null ? value : defaultValue;
    }

}
