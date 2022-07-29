package com.hsbc.demo.dao;

import com.hsbc.demo.entity.Role;
import com.hsbc.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalUserDao implements UserDaoIntf {

    private static final Logger log = LoggerFactory.getLogger(LocalUserDao.class);

    public Map<String, User> userMap = new ConcurrentHashMap<String, User>();

    /**
     * @param name
     * @param password
     * @return
     * @throws BusinessException
     */
    @Override
    public boolean createUser(String name, String password) throws BusinessException {
        if (userMap.containsKey(name)){
            throw new UserExistException(String.format("UserName %s Exists", name), null);
        } else {
            userMap.put(name, new User(name, password));
            log.debug("create user {}", name);
        }
        return true;
    }

    /**
     * @param name
     * @return
     * @throws BusinessException
     */
    @Override
    public boolean deleteUser(String name/*, String password*/) throws BusinessException {
        if (!userMap.containsKey(name)){
            throw new UserNotExistException(String.format("UserName %s not Exists", name), null);
        } else {
//            String encryptPwd = User.entryptPassword(password);
//            User u = userMap.get(name);
//            if (u.getPassword().equals(encryptPwd)){
                userMap.remove(name);
                log.debug("delete user {}", name);
//            } else {
//                throw new BusinessException(String.format("Wrong UserName/Password", name), null);
//            }
        }
        return true;
    }

    /**
     * @param r
     * @param userName
     * @return
     */
    @Override
    public boolean bindRoletoUser(String r, String userName) throws BusinessException {
        if(!userMap.containsKey(userName)){
            throw new UserExistException(String.format("UserName %s Exists", userName), null);
        } else {
            User u = userMap.get(userName);
            u.bindRole(new Role(r));
            log.debug("bind user {} with role {}", userName, r);
        }
        return true;
    }

    public void deleteRole(String role){
        for(User u : userMap.values()){
            Set<Role> sr = u.getRoleSet();
            for(Role rr : sr){
                if (rr.getName().equals(role)){
                   sr.remove(rr);
                }
            }
            u.setRoleSet(sr);
        }
    }

    public User getUserByName(String name) {
        return userMap.get(name);
    }

}
