package com.hsbc.demo.dao;

public interface RoleDaoIntf {

    boolean createRole(String name) throws BusinessException;

    boolean deleteRole(String name) throws BusinessException;

    boolean roleExist(String role);
}
