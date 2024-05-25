package com.bartu.onlyhocams.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;


@ControllerAdvice
public class ExceptionHandler   extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(OhException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(OhException e) {
        ExceptionResponse response = new ExceptionResponse(
                e.getExceptionCode().name(),
                e.getExceptionCode().getDescription(),
                 new Timestamp(System.currentTimeMillis())
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
