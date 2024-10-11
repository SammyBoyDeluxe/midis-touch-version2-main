package com.example.midistouchmalfunction54.net;

import com.example.midistouchmalfunction54.HelloController;
import com.example.midistouchmalfunction54.net.beans.UserBean;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutionException;

public class SaveService extends Service<Void> {

    private final UserBean loggedInUser;
    private final DatabaseReference reference;
    private final HelloController controller;

    public SaveService(UserBean loggedInUser, DatabaseReference reference, HelloController controller) {
        this.loggedInUser = loggedInUser;
        this.reference = reference;
        this.controller = controller;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                // Start saving data to Firebase
                updateMessage("Saving data...");
                updateProgress(0, 1);
                Gson gson = new Gson();
                String userAsJson = gson.toJson(loggedInUser, UserBean.class);
                // Run the Firebase async save operation
                ApiFuture<Void> future = reference.setValueAsync(userAsJson);
                try {
                    // Block and wait for the Firebase operation to complete
                    future.get();

                    if (future.isCancelled()) {
                        future.cancel(false);

                        Platform.runLater(() -> {
                            updateMessage("Save task was cancelled.");
                            updateProgress(0, 1);
                        });
                        return null; // No further execution after cancellation
                    }

                    // Firebase save operation succeeded
                    Platform.runLater(() -> {

                        updateMessage("Save successful!");
                        updateProgress(1, 1);
                    });

                } catch (InterruptedException e) {
                    // Task interrupted
                    Platform.runLater(() -> updateMessage("Save task interrupted: " + e.getMessage()));
                } catch (ExecutionException e) {
                    // Log the full exception for better error diagnosis
                    Platform.runLater(() -> {
                        updateMessage("Save task execution failed: " + e.getMessage());
                        e.printStackTrace();  // Print full stack trace to see the cause of the error
                    });
                    this.failed();
                }

                return null;
            }


            @Override
            protected void succeeded() {
                super.succeeded();
                // Handle task success
                Platform.runLater(() -> this.updateMessage("Data saved successfully!"));
            }

            @Override
            protected void failed() {
                super.failed();
                System.out.println(this.getException().getMessage());
                // Handle task failure
                Platform.runLater(() -> this.updateMessage(("Data save failed!")));
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                // Handle task cancellation
                Platform.runLater(() -> this.updateMessage("Save operation was cancelled."));
            }
        };
    }

    @Override
    protected void succeeded() {
        controller.changeLoggedInPanelForProgressTrackerPanel();

        super.succeeded();
        // Handle success at service level if needed
    }

    @Override
    protected void failed() {
        controller.changeLoggedInPanelForProgressTrackerPanel();

        super.failed();
        // Handle failure at service level if needed
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        // Handle cancellation at service level if needed
    }
}
