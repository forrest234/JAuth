package com.hsbc.demo.dao;

import com.hsbc.demo.entity.Role;
import com.hsbc.demo.entity.User;

import java.util.Set;

public interface UserDaoIntf {

    boolean createUser(String name, String password) throws BusinessException;

    boolean deleteUser(String name) throws BusinessException;

    boolean bindRoletoUser(String r, String user) throws BusinessException;

    User getUserByName(String name);

    void deleteRole(String role);   // when delete rolex, remove rolex from user's RoleSet

}
