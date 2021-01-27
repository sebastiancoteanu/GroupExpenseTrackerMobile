package com.example.groupexpensetrackermobile.services;

import android.content.Context;

import com.example.groupexpensetrackermobile.MainApp;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.entities.User;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LocalStorageService {

    private static final LocalStorageService INSTANCE = new LocalStorageService();
    private static final String CREDENTIAL_STORE = "credentials";

    private LocalStorageService() {
    }

    public static LocalStorageService getInstance() {
        return LocalStorageService.INSTANCE;
    }

    public void clearStorage() {
        FileOutputStream fos = null;
        try {
            fos = MainApp.getAppContext().openFileOutput(CREDENTIAL_STORE, Context.MODE_PRIVATE);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeLoggedUserCredentials() {

        User currentUser = CredentialManager.getInstance().getCurrentUser();
        String token = CredentialManager.getInstance().getCurrentToken();
        if(token == null || currentUser == null) {
            System.err.println("Cannot store user credentials");
            return;
        }

        CredentialDTO credentialDTO = new CredentialDTO(token, currentUser);

        try {
            // Empty the file content
            FileOutputStream fos = MainApp.getAppContext().openFileOutput(CREDENTIAL_STORE, Context.MODE_PRIVATE);
            fos.write(SerializationUtils.serialize(credentialDTO));
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadCredentials() {

        User currentUser = null;
        String token = null;
        CredentialDTO credentialDTO = null;

        try {

            // Read file content
            // Write new file content
            FileInputStream fis =  MainApp.getAppContext().openFileInput(CREDENTIAL_STORE);
            credentialDTO = SerializationUtils.deserialize(fis);
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(credentialDTO == null) {
            return false;
        }

        currentUser = credentialDTO.getUser();
        token = credentialDTO.getToken();

        if(currentUser == null || token == null) {
            return false;
        }

        CredentialManager.getInstance().setCurrentToken(token);
        CredentialManager.getInstance().setCurrentUser(currentUser);

        return true;
    }

}
