package com.hsbc.demo.dao;

public class RoleNotExistException extends BusinessException {

    public RoleNotExistException(Throwable cause) {
        super(cause);
    }

    public RoleNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
