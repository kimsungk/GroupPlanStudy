package com.example.groupplanstudy.Server.DTO;

import java.io.Serializable;

public class APIMessage implements Serializable
{
    private String message;
    private Object data;

    public APIMessage() {
    }

    public APIMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
