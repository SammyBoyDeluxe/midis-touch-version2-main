package com.example.midistouchmalfunction54.net;

import com.example.midistouchmalfunction54.HelloController;
import com.example.midistouchmalfunction54.net.beans.SongBean;
import com.example.midistouchmalfunction54.net.beans.UserBean;
import com.example.midistouchmalfunction54.net.tasks.FirebaseSignUpTask;
import com.example.midistouchmalfunction54.net.tasks.LoginTask;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.ThreadManager;
import com.google.firebase.database.*;
import com.google.firebase.internal.FirebaseThreadManagers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class FirebaseAPIClient {
    /*Upon completed login, counted from finding the username/EnteredPassword-subdirectory to exist -> Directs us to the root of the userbeanobject*/
    private static DatabaseReference userRootReference = null;
    private static FirebaseApp firebaseApp = null;
    public UserBean loggedInUser = null;
    private Scene applicationScene;
    private SongBean selectedSong;

    public FirebaseAPIClient(Scene applicationSceneIn) throws FileNotFoundException {
        applicationScene = applicationSceneIn;
        try {

            FileInputStream serviceAccount =

                    new FileInputStream("src/main/resources/midistouch090924-firebase-adminsdk-qsjkw-05d56bbabb.json");


            FirebaseOptions options = FirebaseOptions.builder()

                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://midistouch090924-default-rtdb.europe-west1.firebasedatabase.app/")
                    .setThreadManager(FirebaseThreadManagers.DEFAULT_THREAD_MANAGER)
                    .build();

            /*Sets the static class-global variable*/
            firebaseApp = FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    /**
     * Returns true on successful login, false otherwise
     *
     * @param username
     * @param password
     * @return
     */
    public void logIn(String username, String password, HelloController controller) throws ExecutionException, InterruptedException {
        String loginPath = "/Users/" + username;
        DatabaseReference loginReference = FirebaseDatabase.getInstance(firebaseApp).getReference("/Users").child(username);
        applicationScene.setCursor(Cursor.WAIT);
        LoginTask loginTask = new LoginTask(loginReference, password);
        runLogin(controller, loginTask, loginReference);
    }

    /** Binds the UI to task-properties, updates UI with progressbar and and progresslabel (-> Visible : true)
     * and runs the task in a background thread. Updates UI and (UserBean) loggedInUser on success
     * @param controller
     * @param loginTask
     * @param loginReference
     */
    private void runLogin(HelloController controller, LoginTask loginTask, DatabaseReference loginReference) {
        // Set the progress bar and label to visible
        controller.loginProgressBar.setVisible(true);
        controller.progressLabel.setVisible(true);
        controller.loginProgressBar.progressProperty().bind(loginTask.progressProperty());

        loginTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            loggedInUser = newValue;
            if (loggedInUser != null) {
                userRootReference = loginReference;
                applicationScene.setCursor(Cursor.DEFAULT);
                controller.changeLoggedInSignUpPanel(newValue.getUsername());
                if (loggedInUser.getSongList() != null && !loggedInUser.getSongList().isEmpty()) {
                    setSongListView(controller);
                }
            }


        });

        loginTask.messageProperty().addListener((observable, oldValue, newValue) -> {
            controller.progressLabel.setText(newValue);
        });

        loginTask.stateProperty().addListener((observable, oldValue, newValue) -> {
            if(loginTask.isDone()){
                applicationScene.setCursor(Cursor.DEFAULT);

            }
        });

        Thread th = new Thread(loginTask);
        th.setDaemon(true);
        try {
            th.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /** Sets the songTabPane to visible (only done if the user has songs) and loads the songBeans into the list view.
     * A change listener is also added, updating our selectedSongBean from the loggedInUser-object
     * on selection change.
     * @param controller
     */
    private void setSongListView(HelloController controller) {
        // Check if the user has songs and create an observable list of song names
        if (loggedInUser.getSongList() != null && !loggedInUser.getSongList().isEmpty()) {
            List<String> list = new ArrayList<>();
            for (SongBean bean : loggedInUser.getSongList()) {
                if(bean != null){
                String name = bean.getName();
                list.add(name);
                }
            }
            ObservableList<String> songNames = FXCollections.observableArrayList(
                    list
            );

            // Set items to the ListView and ensure it uses the existing ListView from the FXML
            controller.songListView.setItems(songNames);

            // Make sure the ListView is visible (if needed, though it should already be set in the FXML)
            controller.songListView.setVisible(true);

            // Add a listener to handle song selection
            controller.songListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                Optional<SongBean> selected = loggedInUser.getSongList().stream()
                        .filter(songBean -> songBean.getName().equals(newValue))
                        .findFirst();

                // If a match is found, set the selectedSong
                selected.ifPresent(songBean -> selectedSong = songBean);
            });

            // Make the entire TabPane containing the ListView visible
            controller.songTabPane.setVisible(true);
        } else {
            // If no songs are available, hide the ListView or display a message accordingly
            controller.songListView.setItems(FXCollections.observableArrayList("No songs available"));
        }
    }


    /**
     * Saves the generated song to the users log in path
     *
     * @param saveSong - Generated from UI-data in application
     */
    public void saveSong(SongBean saveSong, HelloController controller) throws ExecutionException, InterruptedException {
        /*We need to ascertain that we have a user logged in since that specifies the saving directory*/
        if (loggedInUser != null) {


            /*Used to work around multi threading fuckery*/

            /*Package the array in firebase-acceptable format*/
            Optional<List<SongBean>> songBeanList = Optional.ofNullable(loggedInUser.getSongList());
            /* If songList is empty, we create a new list with the saveSong and set it in the user*/
            if (songBeanList.isEmpty()) {
                List<SongBean> songList = List.of(saveSong);
                loggedInUser.setSongList(songList);
            } else {
                /*To avoid java.lang.unsupportedaction we need to make a modifiable list*/
                ListIterator<SongBean> iterator =  loggedInUser.getSongList().listIterator();
                SongBean[] songArray = loggedInUser.getSongList().toArray(new SongBean[0]);

                /*Assign the first free index to the saveSong*/
                SongBean[] newArray = new SongBean[songArray.length+1];
                for(int i = 0 ; i < songArray.length ; i++) newArray[i] = songArray[i];
                newArray[songArray.length-1] = saveSong;

                loggedInUser.setSongList(Arrays.stream(newArray).toList());
                /*Updates the songlist when we save a new song*/
                setSongListView(controller);

            }
            SaveService saveService = new SaveService(loggedInUser,userRootReference,controller);


            controller.loginProgressBar.progressProperty().bind(saveService.progressProperty());
            controller.progressLabel.textProperty().bind(saveService.messageProperty());
            controller.changeLoggedInPanelForProgressTrackerPanel();

            saveService.start();







                }}










    /**
     * Tries to sign up -> Shows alerts on failed, success, cancelled and changes
     * the cursor on the current scene to a loading cursor on running asynchronous query
     *
     * @param insertionBean
     * @return
     */
    public void signUp(UserBean insertionBean) throws Exception {
        String userPath = "/Users";
        DatabaseReference userPathReference = FirebaseDatabase.getInstance(firebaseApp).getReference(userPath);
        addNode(userPathReference, insertionBean);


    }



    /**
     * Returns success of added node-operation via callback asynchronous calling
     *
     * @param reference
     * @return -> Runs signUpTask
     */
    public void addNode(DatabaseReference reference, UserBean insertionBean) {

        FirebaseSignUpTask signUpTask = new FirebaseSignUpTask();


        try {
            signUpTask.runSignUpTask(reference, insertionBean, applicationScene);
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION, "Error: \n" + e.getMessage(), ButtonType.OK);
            errorAlert.show();
        }


    }

    /**
     * Sets logged in path and logged in UserBean to null
     */
    public void signOut(HelloController controller) {
        controller.songTabPane.setVisible(false);
        controller.songListView.getSelectionModel().clearSelection();
        controller.loginProgressBar.setVisible(false);
        controller.progressLabel.setVisible(false);
        userRootReference = null;
        loggedInUser = null;
        this.selectedSong = null;
    }

    public UserBean getLoggedInUser() {
        return loggedInUser;
    }

    public SongBean getSelectedSong() {
        return selectedSong;
    }
}

