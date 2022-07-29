package com.hsbc.demo.dao;

import com.hsbc.demo.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRoleDao implements RoleDaoIntf{

    private static final Logger log = LoggerFactory.getLogger(LocalRoleDao.class);

    public Map<String, Role> roleMap = new ConcurrentHashMap<>();

    /**
     * @param name
     * @return
     * @throws BusinessException
     */
    @Override
    public boolean createRole(String name) throws BusinessException {
        if (roleMap.containsKey(name)){
            throw new UserExistException(String.format("Role %s Exists", name), null);
        } else {
            roleMap.put(name, new Role(name));
            log.info("add new Role {} success!", name);
        }
        return true;
    }

    /**
     * @param name
     * @return
     * @throws BusinessException
     */
    @Override
    public boolean deleteRole(String name) throws BusinessException {
        if (!roleMap.containsKey(name)){
            throw new UserNotExistException(String.format("Role %s not Exists", name), null);
        } else {
            roleMap.remove(name);
            log.info("delete Role {} success!", name);
        }
        return true;
    }

    /**
     * @param role
     * @return
     */
    @Override
    public boolean roleExist(String role) {
        return roleMap.containsKey(role);
    }
}
