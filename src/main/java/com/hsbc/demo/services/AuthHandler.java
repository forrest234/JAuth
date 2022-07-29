package com.hsbc.demo.services;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.demo.dao.BusinessException;
import com.hsbc.demo.dao.TokenDaoIntf;
import com.hsbc.demo.entity.Reply;
import com.hsbc.demo.entity.Role;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class AuthHandler extends BaseHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthHandler.class);

    private TokenDaoIntf tokenDao;

    public AuthHandler(TokenDaoIntf tokenDao) {
        this.tokenDao = tokenDao;
    }

    /**
     * @param httpExchange
     * @param obj
     * @return
     * @throws BusinessException
     */
    @Override
    Reply invoke(HttpExchange httpExchange, JSONObject obj) throws BusinessException {
        Reply r = new Reply();
        if(obj.containsKey("requestId")){
            r.setRequestId(obj.getString("requestId"));
        }

        String method = httpExchange.getRequestMethod().toUpperCase();
        boolean retv = false;

        if ("/api/auth".equals(httpExchange.getRequestURI().getPath())){
            if ("POST".equals(method)) {
                if(!obj.containsKey("userName") || !obj.containsKey("password")){
                    throw new BusinessException("userName/password is required", null);
                } else {
                    String name = obj.getString("userName");
                    String pwd = obj.getString("password");
                    String token = tokenDao.getToken(name, pwd);
                    r.setRetValue(token);
                }
            } else if ("DELETE".equals(method)) {
                if(!obj.containsKey("token")){
                    throw new BusinessException("token is required", null);
                } else {
                    String token = obj.getString("token");
                    tokenDao.invalidate(token);
                }
            }
        } else if ("/api/auth/role".equals(httpExchange.getRequestURI().getPath())) {

            if ("POST".equals(method)) {
                if (!obj.containsKey("token") || !obj.containsKey("roleName")) {
                    throw new BusinessException("token/roleName is required", null);
                } else {
                    String token = obj.getString("token");
                    String role = obj.getString("roleName");
                    retv = tokenDao.checkRole(token, role);
                }
            }
            r.setRetValue(retv);
        } else if ("/api/auth/roles".equals(httpExchange.getRequestURI().getPath())){
            if ("POST".equals(method)) {
                if(!obj.containsKey("token")){
                    throw new BusinessException("token is required", null);
                } else {
                    String token = obj.getString("token");
                    Set<Role> rs = tokenDao.getAllRoles(token);
                    r.setRetValue(rs);
                }
            }
        } else {
            r.setCode(1);
            r.setMessage("Invalid method " + method);
            return r;
        }
        return r;
    }

}
