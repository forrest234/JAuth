package com.hsbc.demo.dao;

public class RoleDao implements RoleDaoIntf{

    /**
     * @param name
     * @return
     * @throws BusinessException
     */
    @Override
    public boolean createRole(String name) throws BusinessException {
        // insert into DB or other persistent storage
        return false;
    }

    /**
     * @param name
     * @return
     * @throws BusinessException
     */
    @Override
    public boolean deleteRole(String name) throws BusinessException {
        return false;
    }

    /**
     * @param role
     * @return
     */
    @Override
    public boolean roleExist(String role) {
        return false;
    }
}
