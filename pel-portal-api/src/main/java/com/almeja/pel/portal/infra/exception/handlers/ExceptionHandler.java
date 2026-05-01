package com.almeja.pel.portal.infra.exception.handlers;

import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.util.DateUtil;
import com.almeja.pel.portal.infra.exception.classes.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

public class ExceptionHandler {

    @Provider
    public static class AppExceptionMapper implements ExceptionMapper<AppException> {

        @Context
        UriInfo uriInfo;

        @Override
        public Response toResponse(AppException e) {
            String error = e.getClass().getName();
            ErrorResponse err = new ErrorResponse(DateUtil.getDate(), Response.Status.BAD_REQUEST.getStatusCode(), error, e.getMessage(), uriInfo.getPath());
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }
    }

    @Provider
    public static class ValidatorExceptionMapper implements ExceptionMapper<ValidatorException> {

        @Context
        UriInfo uriInfo;

        @Override
        public Response toResponse(ValidatorException e) {
            String error = e.getClass().getName();
            ErrorResponse err = new ErrorResponse(DateUtil.getDate(), Response.Status.UNAUTHORIZED.getStatusCode(), error, e.getMessage(), uriInfo.getPath());
            return Response.status(Response.Status.UNAUTHORIZED).entity(err).build();
        }
    }

    @Provider
    public static class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

        @Context
        UriInfo uriInfo;

        @Override
        public Response toResponse(RuntimeException e) {
            String error = e.getClass().getName();
            ErrorResponse err = new ErrorResponse(DateUtil.getDate(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), error, e.getMessage(), uriInfo.getPath());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(err).build();
        }
    }

}
