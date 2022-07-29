package com.hsbc.demo.dao;

import com.hsbc.demo.entity.Role;

import java.util.Set;

public interface TokenDaoIntf {
    String getToken(String userName, String password) throws BusinessException;
    void invalidate(String token);
    boolean checkRole(String token, String role);

    Set<Role> getAllRoles(String token) throws BusinessException;
}
