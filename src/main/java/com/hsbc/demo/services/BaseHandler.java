package com.hsbc.demo.services;

import com.alibaba.fastjson2.JSONObject;
import com.hsbc.demo.dao.BusinessException;
import com.hsbc.demo.entity.Reply;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseHandler implements HttpHandler {

    private static final Logger log = LoggerFactory.getLogger(BaseHandler.class);

    abstract Reply invoke(HttpExchange httpExchange, JSONObject obj) throws BusinessException;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Reply reply = new Reply();
        try {
            JSONObject obj = parseRequestBody(httpExchange);
            reply = this.invoke(httpExchange, obj);
        } catch (Exception ex) {
            reply.setCode(1);
            reply.setMessage(ex.getMessage());
            ex.printStackTrace();
        }

        try {
            handleResponse(httpExchange, reply);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    /**
     * 处理响应
     * @param httpExchange
     * @param reply
     * @throws Exception
     */
    private void handleResponse(HttpExchange httpExchange, Reply reply) throws Exception {
//        log.debug("reply code:{} message:{} id:{} value:{}", reply.getCode(), reply.getMessage(), reply.getRequestId(), reply.getRetValue());
        String responseContentStr = reply.toJsonStr();
//        log.debug("reply: {}", responseContentStr);

        byte[] responseContentByte = responseContentStr.getBytes("utf-8");
        //设置响应头，必须在sendResponseHeaders方法之前设置！
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");

        //设置响应码和响应体长度，必须在getResponseBody方法之前调用！
        httpExchange.sendResponseHeaders(200, responseContentByte.length);

        OutputStream out = httpExchange.getResponseBody();
        out.write(responseContentByte);
        out.flush();
        out.close();
    }

    private JSONObject parseRequestBody(HttpExchange httpExchange) throws BusinessException {
        try{
            byte[] req = httpExchange.getRequestBody().readAllBytes();

//            log.debug("req is {}", new String(req));

            JSONObject obj = JSONObject.parseObject(new String(req));
            return obj;
        } catch (IOException e) {
            throw new BusinessException("Invalid Request Body", null);
        }
    }
}
