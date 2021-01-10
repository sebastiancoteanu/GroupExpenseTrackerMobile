package com.example.groupexpensetrackermobile.config;

import com.example.groupexpensetrackermobile.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class CredentialManager {

    private static final CredentialManager INSTANCE = new CredentialManager();

    private User currentUser;
    private String currentToken;

    private CredentialManager() {};

    public static CredentialManager getInstance() {
        return INSTANCE;
    }

    public boolean storeUser(JSONObject userJson) {
        if(currentToken == null) {
            System.err.println("Token should have been fetched until this point.");
        }

        currentUser = User.fromJsonObject(userJson);
        if(currentUser == null) {
            return false;
        }
        return true;
    }

    public boolean storeToken(JSONObject jsonObject) {
        try {
            this.currentToken = jsonObject.getString("id_token");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void removeUser() {
        currentUser = null;
        currentToken = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentToken() {
        return currentToken;
    }
}
