package com.hsbc.demo.dao;

public class UserNotExistException extends BusinessException {

    public UserNotExistException(Throwable cause) {
        super(cause);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
