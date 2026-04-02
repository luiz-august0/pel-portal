package com.almeja.pel.portal.infra.exception.handlers;

import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.util.DateUtil;
import com.almeja.pel.portal.core.util.StringUtil;
import com.almeja.pel.portal.infra.exception.classes.ErrorResponse;
import com.almeja.pel.portal.infra.service.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Date;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandler {

    private final TokenService tokenService;

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

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handler(AuthenticationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String message = e.getMessage();
        String token = tokenService.getTokenFromRequest(request);
        if (StringUtil.isNullOrEmpty(token)) {
            message = EnumAppException.EXPIRED_SESSION.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        String error = e.getClass().getName();
        ErrorResponse err = new ErrorResponse(new Date(), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}