package com.simplechat.myapp.simplechat.configuration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseConfiguration {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;

    public static DatabaseReference getFirebase(){

        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;

    }

    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null) {
            firebaseAuth = firebaseAuth.getInstance();
        }
        return firebaseAuth;
    }


}
