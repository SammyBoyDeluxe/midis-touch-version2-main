package com.example.midistouchmalfunction54.net.callbacks;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public interface FirebaseSignUpCallback {

    public void onComplete(DatabaseError error, DatabaseReference reference);
}
