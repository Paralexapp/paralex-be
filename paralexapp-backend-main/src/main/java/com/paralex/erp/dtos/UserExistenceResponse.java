package com.paralex.erp.dtos;

public class UserExistenceResponse {
    private boolean exists;
    private String message;
    private String userType;

    // Constructors
    public UserExistenceResponse() {}

    public UserExistenceResponse(boolean exists, String message, String userType) {
        this.exists = exists;
        this.message = message;
        this.userType = userType;
    }

    // Getters and Setters
    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
