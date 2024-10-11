package com.example.midistouchmalfunction54.net.callbacks;

import com.google.firebase.database.DataSnapshot;

public interface FirebaseLoadCallback {


    public void onChildAdded(DataSnapshot snapshot, String previousChildName);


    public void onChildChanged(DataSnapshot snapshot, String previousChildName);


    public void onChildRemoved(DataSnapshot snapshot);


    public void onChildMoved(DataSnapshot snapshot, String previousChildName);






}
