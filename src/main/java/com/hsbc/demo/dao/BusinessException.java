package com.hsbc.demo.dao;

public class BusinessException extends Exception{
    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
