package com.simplechat.myapp.simplechat.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private String FILE_NAME = "applicationdata";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private String IDENTIFIER_KEY = "currentUserIdentifier";
    private String NAME_KEY = "currentUserName";

    public Preferences ( Context contextParameter ){

        context = contextParameter;
        preferences = context.getSharedPreferences(FILE_NAME, MODE);
        editor = preferences.edit();

    }

    public void saveData( String userIdentifier, String userName ){
        editor.putString( IDENTIFIER_KEY, userIdentifier );
        editor.putString( NAME_KEY, userName );
        editor.commit();

    }

    public String getCurrentUserIdentifier (){
        return preferences.getString( IDENTIFIER_KEY, null );
    }

    public String getCurrentUserName (){
        return preferences.getString( NAME_KEY, null );
    }

}
