package com.simplechat.myapp.simplechat.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.simplechat.myapp.simplechat.config.FirebaseConfiguration;

public class User {

    private String userId;
    private String userName;
    private String userEmail;
    private String userPassword;

    public User(){

    }

    public void save(){
        DatabaseReference firebaseReference = FirebaseConfiguration.getFirebase();
        firebaseReference.child("users").child( getUserId() ).setValue( this );

    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Exclude
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }



}
