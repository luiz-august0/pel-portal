package com.almeja.pel.gestao.infra.exception.handlers;

import com.almeja.pel.gestao.core.exception.AppException;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.util.DateUtil;
import com.almeja.pel.gestao.infra.exception.classes.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handler(AppException e, HttpServletRequest request) {
        String error = e.getClass().getName();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse err = new ErrorResponse(DateUtil.getDate(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidatorException.class)
    public ResponseEntity<ErrorResponse> handler(ValidatorException e, HttpServletRequest request) {
        String error = e.getClass().getName();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse err = new ErrorResponse(DateUtil.getDate(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handler(RuntimeException e, HttpServletRequest request) {
        String error = e.getClass().getName();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse err = new ErrorResponse(DateUtil.getDate(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}