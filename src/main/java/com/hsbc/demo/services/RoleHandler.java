package com.hsbc.demo.services;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.demo.dao.BusinessException;
import com.hsbc.demo.dao.RoleDaoIntf;
import com.hsbc.demo.dao.UserDaoIntf;
import com.hsbc.demo.entity.Reply;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RoleHandler extends BaseHandler {

    private static final Logger log = LoggerFactory.getLogger(RoleHandler.class);

    private RoleDaoIntf roleDao;
    private UserDaoIntf userDao;

    public RoleHandler(RoleDaoIntf roleDao, UserDaoIntf userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
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

        String name = obj.getString("roleName");
        String method = httpExchange.getRequestMethod().toUpperCase();
        boolean retv = false;
        if ("POST".equals(method)) {
            retv = roleDao.createRole(name);
        } else if ("DELETE".equals(method)) {
            retv = roleDao.deleteRole(name);
            userDao.deleteRole(name);
        } else {
            r.setCode(1);
            r.setMessage("Only POST/DELETE allowed");
            return r;
        }
        r.setRetValue(retv);
        return r;
    }

}
