package com.bartu.onlyhocams.exception;

public class OhException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public OhException(ExceptionCode exceptionCode) {
        super(exceptionCode.getDescription());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
