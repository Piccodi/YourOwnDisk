package com.piccodi.yodisk.exception;

import org.springframework.http.HttpStatus;

public class CustomResponseException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String message;

    public CustomResponseException(HttpStatus hs, String mes){
        this.httpStatus = hs;
        this.message = mes;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
