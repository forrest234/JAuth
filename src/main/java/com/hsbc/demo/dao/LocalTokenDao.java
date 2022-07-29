package com.hsbc.demo.dao;

import com.hsbc.demo.entity.Role;
import com.hsbc.demo.entity.User;
import com.hsbc.demo.services.RoleHandler;
import com.hsbc.demo.util.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

public class LocalTokenDao implements TokenDaoIntf {

    private static final Logger log = LoggerFactory.getLogger(LocalTokenDao.class);

    private UserDaoIntf userDao;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private ExpiringMap<String, String> userTokenRecordMap = new ExpiringMap<>();
    private ExpiringMap<String, String> token2UserMap = new ExpiringMap<>();

    private long expireTimeInSecond = 3600 * 2;   // default 2 hours

    public LocalTokenDao(UserDaoIntf dao, int expireTimeInSecond) {
        this.userDao = dao;
        this.expireTimeInSecond = expireTimeInSecond;
        userTokenRecordMap.setTimeToLive(expireTimeInSecond);
        userTokenRecordMap.setExpirationInterval(30);
    }

    /**
     * @param userName
     * @param password
     * @return
     */
    @Override
    public String getToken(String userName, String password) throws BusinessException{

        String encryptPwd = User.entryptPassword(password);
        User u = userDao.getUserByName(userName);
        if (u == null || !u.getPassword().equals(encryptPwd)){      // no such user, or user password is wrong
            throw new BusinessException("Invalid user/password", null);
        }

        if (!userTokenRecordMap.containsKey(userName)){
            // generate a now token
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            String newToken = base64Encoder.encodeToString(randomBytes);

            userTokenRecordMap.put(userName, newToken);
            token2UserMap.put(newToken, userName);
            log.debug("user {} new token is {}", userName, newToken);
            return newToken;
        } else {
            log.debug("user {} old token is {}", userName, userTokenRecordMap.get(userName));
            return userTokenRecordMap.get(userName);
        }
    }

    /**
     * @param token
     * @return
     */
    @Override
    public void invalidate(String token) {
        if (token2UserMap.containsKey(token)){
            String userName = token2UserMap.get(token);
            userTokenRecordMap.remove(userName, token);
            token2UserMap.remove(token);
            log.debug("delete token {}", token);
        }
    }

    /**
     * @param token
     * @param role
     * @return
     */
    @Override
    public boolean checkRole(String token, String role) {
        // find user according to token, then check the user's role
        if(token2UserMap.containsKey(token)){       // valid token, not expired
            String userName = token2UserMap.get(token);
            User u = userDao.getUserByName(userName);
            log.debug("user {} has roles {}. target:{}", userName, Arrays.toString(u.getRoleSet().toArray()), role);

            for(Role sr : u.getRoleSet()){
                if (sr.getName().equals(role)){
//                    log.debug("compare string, contains: true"); break;
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Role> getAllRoles(String token) throws BusinessException{
        if(token2UserMap.containsKey(token)){       // valid token, not expired
            String userName = token2UserMap.get(token);
            User u = userDao.getUserByName(userName);
            log.debug("user {} has roles {}", userName, Arrays.toString(u.getRoleSet().toArray()));
            return u.getRoleSet();
        } else {
            throw new BusinessException("invalid token", null);
        }
    }
}
