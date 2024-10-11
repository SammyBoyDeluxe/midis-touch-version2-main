package com.example.midistouchmalfunction54.net.callbacks;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface FirebaseLoginCallback {

    public void dataSnapshotExists(DataSnapshot snapshot);
    public void onCancelled(DatabaseError error);

}
