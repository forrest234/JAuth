package com.hsbc.demo.dao;

public class UserExistException extends BusinessException {

    public UserExistException(Throwable cause) {
        super(cause);
    }

    public UserExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
