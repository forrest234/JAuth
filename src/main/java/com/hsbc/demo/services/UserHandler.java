package com.hsbc.demo.services;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.demo.dao.BusinessException;
import com.hsbc.demo.dao.RoleDaoIntf;
import com.hsbc.demo.dao.UserDaoIntf;
import com.hsbc.demo.entity.Reply;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserHandler extends BaseHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    private UserDaoIntf userDao;
    private RoleDaoIntf roleDao;

    public UserHandler(UserDaoIntf userDao, RoleDaoIntf roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
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

        if(!obj.containsKey("userName")){
            throw new BusinessException("Please provide a user name", null);
        }
        String name = obj.getString("userName");
        String method = httpExchange.getRequestMethod().toUpperCase();
        boolean retv = false;
        log.debug("request uri is {}", httpExchange.getRequestURI().getPath());
        log.debug("request uri is {}", httpExchange.getRequestURI().toString());

        if ("/api/user/bind".equals(httpExchange.getRequestURI().getPath())){
            if(!obj.containsKey("roleName")){
                throw new BusinessException("parameter roleName is required", null);
            } else {
                String rname = obj.getString("roleName");
                if (!roleDao.roleExist(rname)){
                    throw new BusinessException(String.format("roleName %s not exist", rname), null);
                } else {
                    retv = userDao.bindRoletoUser(rname, name);
                }
            }
        } else if ("/api/user".equals(httpExchange.getRequestURI().getPath())){
            String pwd = obj.getString("password");
            if ("POST".equals(method)) {
                retv = userDao.createUser(name, pwd);
                r.setCode(0);
                r.setRetValue(retv);
            } else if ("DELETE".equals(method)) {
                retv = userDao.deleteUser(name);
                r.setCode(0);
                r.setRetValue(retv);
            } else {
                r.setCode(1);
                r.setMessage("Only POST/DELETE allowed");
                return r;
            }
        } else {

        }

        r.setRetValue(retv);
        return r;
    }

}