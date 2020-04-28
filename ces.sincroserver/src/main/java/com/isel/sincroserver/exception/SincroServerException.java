package com.isel.sincroserver.exception;

public class SincroServerException extends Exception {
    SincroServerExceptionType type;

    public SincroServerException() {
    }

    public SincroServerException(String message) {
        super(message);
    }

    public SincroServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SincroServerException(Throwable cause) {
        super(cause);
    }

    public SincroServerException(SincroServerExceptionType type) {
        this.type = type;
    }

    public SincroServerException(String message, SincroServerExceptionType type) {
        super(message);
        this.type = type;
    }

    public SincroServerException(String message, Throwable cause, SincroServerExceptionType type) {
        super(message, cause);
        this.type = type;
    }

    public SincroServerException(Throwable cause, SincroServerExceptionType type) {
        super(cause);
        this.type = type;
    }

    public SincroServerExceptionType getType() {
        return type;
    }

    public void setType(SincroServerExceptionType type) {
        this.type = type;
    }
}