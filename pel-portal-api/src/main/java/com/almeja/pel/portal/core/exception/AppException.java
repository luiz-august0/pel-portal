package com.almeja.pel.portal.core.exception;

import com.almeja.pel.portal.core.exception.enums.EnumAppException;

public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }

    public AppException(EnumAppException exception) {
        super(exception.getMessage());
    }

}
