package com.almeja.pel.gestao.core.util;

import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty() || str.equalsIgnoreCase("null");
    }

    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean isValidEmail(String email) {
        if (isNotNullOrEmpty(email)) {
            String regex = "[a-zA-Z0-9-_\\.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+";

            return Pattern.matches(regex, email);
        }

        return false;
    }

    public static String keepOnlyNumbers(String str) {
        return str.replaceAll("[^0-9]", "");
    }

}
