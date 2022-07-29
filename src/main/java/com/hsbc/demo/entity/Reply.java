package com.hsbc.demo.entity;

import com.alibaba.fastjson.JSONObject;

public class Reply {

    private String requestId;

    // we can use different status code for different situation, and desc explain the status and why
    private int code;     // 状态码
    private String message;
    private Object retValue;   //返回值

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetValue() {
        return retValue;
    }

    public void setRetValue(Object retValue) {
        this.retValue = retValue;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String toJsonStr(){
        return JSONObject.toJSONString(this);
    }
}
