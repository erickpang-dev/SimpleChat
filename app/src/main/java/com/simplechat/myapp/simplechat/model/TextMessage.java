package com.simplechat.myapp.simplechat.model;

public class TextMessage {

    private String userId;
    private String message;

    public TextMessage() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
