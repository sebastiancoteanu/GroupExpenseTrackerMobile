package com.example.groupexpensetrackermobile.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;

    private User() {};

    public User(long id, String login, String firstName, String lastName, String email) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static User fromJsonObject(JSONObject json) {
        try {
            User user = new User();
            user.setId(json.getLong("id"));
            user.setLogin(json.getString("login"));
            user.setEmail(json.getString("email"));
            user.setFirstName(json.getString("firstName"));
            user.setLastName(json.getString("lastName"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
