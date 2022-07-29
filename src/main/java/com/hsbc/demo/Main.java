package com.hsbc.demo;

import com.hsbc.demo.dao.*;
import com.hsbc.demo.services.AuthHandler;
import com.hsbc.demo.services.RoleHandler;
import com.hsbc.demo.services.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        InputStream in = Main.class.getResourceAsStream("/cfg.properties");
        Properties props = new Properties();
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        props.load(inputStreamReader);
        String expireTimeCfg = props.getProperty("TokenExpireInSec", "7200");
        int expireTime = Integer.parseInt(expireTimeCfg);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(9090), 0);

        UserDaoIntf userDao = new LocalUserDao();
        RoleDaoIntf roleDao = new LocalRoleDao();
        TokenDaoIntf tokenDao = new LocalTokenDao(userDao, expireTime);

        //创建一个HttpContext，将路径为/myserver请求映射到MyHttpHandler处理器
        httpServer.createContext("/api/user", new UserHandler(userDao, roleDao));
        httpServer.createContext("/api/role", new RoleHandler(roleDao, userDao));
        httpServer.createContext("/api/auth", new AuthHandler(tokenDao));

        //设置服务器的线程池对象
        httpServer.setExecutor(Executors.newFixedThreadPool(10));

        //启动服务器
        httpServer.start();

        log.info("service started at :9090");

    }
}