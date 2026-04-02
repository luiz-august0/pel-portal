package com.almeja.pel.gestao.core.exception;

import com.almeja.pel.gestao.core.exception.enums.EnumAppException;

public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }

    public AppException(EnumAppException exception) {
        super(exception.getMessage());
    }

}
