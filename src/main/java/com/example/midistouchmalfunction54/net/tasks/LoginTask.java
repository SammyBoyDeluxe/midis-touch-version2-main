package com.example.midistouchmalfunction54.net.tasks;

import com.example.midistouchmalfunction54.net.beans.SongBean;
import com.example.midistouchmalfunction54.net.beans.UserBean;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LoginTask extends Task<UserBean> {

    private DatabaseReference reference;
    private String password;

    /**Instances a logintask with a database reference pointing to the user
     * root node and a password input to see if it matches
     * -> Task returns a UserBean, null on fail.
     * Triggers the first progress-property update
     * @param reference
     * @param password
     */
    public LoginTask(DatabaseReference reference, String password) {
        this.reference = reference;
        this.password = password;
        /*Will be used to track progress for UX*/

    }

    @Override
    protected void running() {
        this.updateProgress(2,3);
        super.running();
    }

    @Override
    protected void succeeded() {
        this.updateProgress(3,3);
        this.updateMessage("-Login successful-");

        super.succeeded();
    }

    @Override
    protected void failed() {

        super.failed();
    }

    @Override
    protected void scheduled() {
        this.updateProgress(1,3);
        this.updateMessage("-Login scheduled-");
        this.run();
        super.scheduled();
    }

    @Override
    protected UserBean call() throws Exception {

        /*The reference points to the userbean object, so we get the userbean and see if password matches*/
        reference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                /*Check for match against password*/
                UserBean userBean = new UserBean();
                if (snapshot.exists()) {
                    Gson gson = new Gson();
                    userBean = gson.fromJson(snapshot.getValue().toString(), UserBean.class);
//                     userBean = UserBean.buildUserBean(snapshot.child("username").getValue(String.class), snapshot.child("password").getValue(String.class), (List<SongBean>) snapshot.child("songList").getValue(new GenericTypeIndicator<List<SongBean>>() {
                }
                /*If we the password matches then we run the login callback*/
                if (userBean.getPassword().equals(password)) {
                    LoginTask.this.updateValue(userBean);
                    LoginTask.this.succeeded();
                } else {
                    LoginTask.this.updateValue(null);
                    LoginTask.this.updateProgress(0,3);
                    LoginTask.this.updateMessage("The user credentials did not match, please try again.");
                    LoginTask.this.cancel(true);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                LoginTask.this.setException(error.toException());

            }
        });





        return this.getValue();
    }}
