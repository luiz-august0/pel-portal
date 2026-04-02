package com.almeja.pel.gestao.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class ClassUtil {

    public static Class<?> getClassFromCollectionField(Field field) {
        return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

}
