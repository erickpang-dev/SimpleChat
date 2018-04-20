package com.simplechat.myapp.simplechat.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private String FILE_NAME = "applicationdata";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private String NAME_KEY = "username";
    private String PHONE_NUMBER_KEY = "phonenumber";
    private String TOKEN_KEY = "token";

    public Preferences ( Context contextParameter ){

        context = contextParameter;
        preferences = context.getSharedPreferences(FILE_NAME, MODE);
        editor = preferences.edit();

    }

    public void saveUserPreferences( String userName, String phoneNumber, String token ){
        editor.putString( NAME_KEY, userName );
        editor.putString( PHONE_NUMBER_KEY, phoneNumber );
        editor.putString( TOKEN_KEY, token );
        editor.commit();

    }

    public HashMap<String, String> getUserData(){

        HashMap<String, String> userData = new HashMap<>();

        userData.put(NAME_KEY, preferences.getString(NAME_KEY, null));
        userData.put(PHONE_NUMBER_KEY, preferences.getString(PHONE_NUMBER_KEY, null));
        userData.put(TOKEN_KEY, preferences.getString(TOKEN_KEY, null));
        return userData;
    }
}
