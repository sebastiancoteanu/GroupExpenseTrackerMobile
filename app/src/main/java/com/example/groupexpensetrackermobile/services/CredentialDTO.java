package com.example.groupexpensetrackermobile.services;

import com.example.groupexpensetrackermobile.entities.User;

import java.io.Serializable;

public class CredentialDTO implements Serializable {

    private String token;
    private User user;

    public CredentialDTO() {
    }

    public CredentialDTO(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
