package com.example.midistouchmalfunction54.net.tasks;

import com.example.midistouchmalfunction54.net.beans.UserBean;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class FirebaseSignUpTask {


    /**Takes in a database-reference and performs the adding of a userbean to that reference
     * -> Changes cursor to indicate loading
     * @param databaseReference
     * @param insertionBean
     * @param currentScene
     */
    public void runSignUpTask(DatabaseReference databaseReference, UserBean insertionBean, Scene currentScene){
        Task<Void> signUpTask = new Task<Void>() {

            @Override
            protected Void call() throws Exception {


                ApiFuture<Void> future = databaseReference.child(insertionBean.getUsername()).setValueAsync(insertionBean);
                future.get();
                return null;
            }

            @Override
            protected void cancelled() {
                    super.cancelled();

            }

            @Override
            protected void failed() {

                currentScene.setCursor(Cursor.DEFAULT);


                super.failed();

            }

            @Override
            protected void succeeded() {

                    currentScene.setCursor(Cursor.DEFAULT);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "The sign-up task was completed successfully!");
                    successAlert.show();
                    System.out.print("Success!");


                super.succeeded();

            }

            @Override
            protected void scheduled() {
               currentScene.setCursor(Cursor.WAIT);
               this.run();
               super.scheduled();



            }
        };
        /*Runs the process in a background thread*/
        new Thread(signUpTask).start();


    }


}

