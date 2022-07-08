package com.example.groupplanstudy.Server.DTO;

public class APIMessage
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
}
