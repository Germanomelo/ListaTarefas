package com.example.tasks.service.model;

public class Feedback {
    private boolean success = true;
    private String message;

    public Feedback() {
    }

    public Feedback(String message) {
        this.success = false;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
