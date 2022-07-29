package com.hsbc.demo.dao;

public class RoleExistException extends BusinessException {

    public RoleExistException(Throwable cause) {
        super(cause);
    }

    public RoleExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
