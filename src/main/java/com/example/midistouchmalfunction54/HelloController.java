package com.example.midistouchmalfunction54;

import com.example.midistouchmalfunction54.net.FirebaseAPIClient;
import com.example.midistouchmalfunction54.net.beans.MusicSequencerBean;
import com.example.midistouchmalfunction54.net.beans.SongBean;
import com.example.midistouchmalfunction54.net.beans.UserBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.midistouchmalfunction54.music.PlaySong;
import com.example.midistouchmalfunction54.music.MusicSequencer;
import com.example.midistouchmalfunction54.music.TrackGenerator;
import com.example.midistouchmalfunction54.utilities.MidiUtils;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class HelloController {

    // define constans for note length:

    private static final int MELODY_NOTE_LENGTH = 2;
    private static final int CHORD_NOTE_LENGTH = 2;
    public CheckBox sigunUpCheckBox;
    public Button loginButton;
    public FirebaseAPIClient firebaseAPIClient = null;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public Button saveButton;
    public Button loadButton;
    public HBox loginHbox;
    public StackPane loginBoxesStackPane;
    public HBox loginSignUpBox;
    public VBox loggedInBox;
    public Label loggedInLabel;
    public Button signOutButton;
    public ProgressBar loginProgressBar;
    public Label progressLabel;
    public VBox progressAndButtonVBox;
    public Label songNameLabel;
    public Tab songTab;
    public TabPane songTabPane;
    public TextArea sequencesTextArea;
    public Tab sequencesTab;
    public Label bpmLabel;
    public Label keyLabel;
    public Button loadButton1;
    public ListView songListView;
    public Tab songListTab;
    public HBox loggedInAndSongTabPaneHBox;
    public HBox loginSignUpHBox;

    private boolean signUp = false;

    @FXML
    private Spinner<Integer> bpmSpinner;
    @FXML
    private Spinner<String> keySpinner;

    // field to store the generated tracks
    private MusicSequencer[] generatedMusicSequencers;

    // instance of PlaySong class to play the song
    private PlaySong player = new PlaySong();

    @FXML
    public void initialize() throws IOException {
        bpmSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 200, 120));
        keySpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(
                javafx.collections.FXCollections.observableArrayList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")));
        // default value in GUI spinner
        keySpinner.getValueFactory().setValue("C");
        /*Initializes a firebaseApiClient */
    }

    @FXML
    protected void onGenerateButtonClick() {
        int bpm = bpmSpinner.getValue();
        // converts key string to MIDI note byte value
        byte key = (byte) MidiUtils.noteToMidiValue(keySpinner.getValue());

        // generate track with bpm and key (NOT YET set, TO DO!), 4 bars assumed right now
        generatedMusicSequencers = TrackGenerator.generateTracks((byte) 4, CHORD_NOTE_LENGTH, MELODY_NOTE_LENGTH);
    }

    @FXML
    protected void onPlayButtonClick() {
        if (generatedMusicSequencers != null && generatedMusicSequencers.length > 1) {
            byte[][][] chordTrack = generatedMusicSequencers[0].midiSequenceArray;
            byte[][][] melodyTrack = generatedMusicSequencers[1].midiSequenceArray;
            // use the BPM value from the spinner
            int bpm = bpmSpinner.getValue();
            // get base MIDI note from the spinner
            int baseMidiNote = MidiUtils.noteToMidiValue(keySpinner.getValue());


            // play the com.example.midistouchmalfunction54.music using the stored tracks and BPM

            player.playSong(new byte[][][][]{chordTrack, melodyTrack}, bpm, baseMidiNote, CHORD_NOTE_LENGTH, MELODY_NOTE_LENGTH);
            System.out.println("BPM value from GUI: " + bpm + ", Key: " + baseMidiNote);
        }
    }

    /**Updates the signUp-boolean on check/uncheck
     *
     * @param actionEvent
     */
    public void signUpValueUpdate(ActionEvent actionEvent) {
        signUp = !signUp;
        if(signUp) loginButton.setText("Sign up");
        else loginButton.setText("Log in");
    }

    public void onLoginButtonClick(ActionEvent actionEvent) throws FileNotFoundException, ExecutionException, InterruptedException {
        if (firebaseAPIClient == null)firebaseAPIClient = new FirebaseAPIClient(keySpinner.getScene());

        /*Checks whether to run login or sign-up operation*/
        if(!signUp){
            /*Sees if we have any text in our username/password-fields*/
            if(usernameTextField.getText().isEmpty() ||usernameTextField.getText().isBlank() || passwordTextField.getText().isEmpty() ||passwordTextField.getText().isBlank() ){
                Alert alert = new Alert(Alert.AlertType.ERROR,"Error: \n One or more fields were left empty, please try again",ButtonType.OK);
                alert.show();
            }
            /*If there is text to process we proceed to try to match user credentials to the database */
            else {
                firebaseAPIClient.logIn(usernameTextField.getText(),passwordTextField.getText(),this);


            }



        }
        /*If we want to sign up we run the following code-segment*/
        else{
            if(usernameTextField.getText().isEmpty() ||usernameTextField.getText().isBlank() || passwordTextField.getText().isEmpty() ||passwordTextField.getText().isBlank() ){
                Alert alert = new Alert(Alert.AlertType.ERROR,"Error: \n One or more fields were left empty, please try again",ButtonType.OK);
                alert.show();
            }/*If text exists we sign up*/
            else{
                UserBean insertionBean = UserBean.buildUserBean(usernameTextField.getText(), passwordTextField.getText(),List.of());
                try {
                    firebaseAPIClient.signUp(insertionBean);
                    firebaseAPIClient.logIn(insertionBean.getUsername(), insertionBean.getPassword(),this);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }

    }

    public void onSaveButtonClick(ActionEvent actionEvent) {

        if(generatedMusicSequencers!= null){
        TextInputDialog songNameInputDialog = new TextInputDialog();
        songNameInputDialog.setContentText("Please enter a name to save your song under");
        songNameInputDialog.setTitle("Name input");
        songNameInputDialog.setHeaderText("Song name: ");

        boolean validInput = false;
        Optional<String> songnameInput = null;
        /*Runs a loop of input-dialogue until valid input has been found*/
        String songName = null;
        while (!validInput) {
            /*Asks for string input in dialogue window from user, waits until entry */
            songnameInput = songNameInputDialog.showAndWait();
            songName = String.valueOf(songnameInput.get());

            /*Check so that it isn´t empty or blank*/
            if (songName.trim().isEmpty() || songName == null) {

                songNameInputDialog.setContentText("The name can´t be blank, please try again");

            } else {
                validInput = true;
            }


        }
        /*We have reached valid input, we should now pair this with the generatedMusicSequencers midis touch has created*/
        SongBean songBean = MidiUtils.createSongBean(songName,bpmSpinner.getValue(),MidiUtils.noteToMidiValue(keySpinner.getValue()),generatedMusicSequencers);


            try {
                firebaseAPIClient.saveSong(songBean,this);
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
        else{
            new Alert(Alert.AlertType.ERROR,"A song must be generated in order to save, please try again after generating a new song");
        }
    }

    public void onLoadButtonClick(ActionEvent actionEvent) {
        /*First we see if there is a valid load-target*/
        if(firebaseAPIClient.getSelectedSong() != null){
            SongBean loadSong = firebaseAPIClient.getSelectedSong();


            // generate track with bpm and key (NOT YET set, TO DO!), 4 bars assumed right now
            generatedMusicSequencers = MusicSequencerBean.buildMusicSequencerArrayFromMusicSequencerBeanList(loadSong.getChordAndMelodySequencers());
            /*We update the bpmSpinner and keySpinner to reflect the loaded songs attributes*/
            bpmSpinner.getValueFactory().setValue(loadSong.getBpm());
            keySpinner.getValueFactory().setValue(MidiUtils.midiValueToNoteName(loadSong.getBaseMidiNote()));
            
            /*We then update the songTabPane with the data from the selected song*/
            
            songNameLabel.setText(loadSong.getName());
            String key = MidiUtils.midiValueToNoteName(loadSong.getBaseMidiNote());
            keyLabel.setText("Key: "+ String.valueOf(key));
            bpmLabel.setText("BPM: "+ String.valueOf(loadSong.getBpm()));
            String chordMusicSequenceAsNotes = new String("Chordsequence (Notes):" );
           byte[][][] chordmidiSequenceArray =  generatedMusicSequencers[0].getMidiSequenceArray();
            for (int i = 0; i < chordmidiSequenceArray.length; i++) {
                /*Index i represents changing bar*/

                chordMusicSequenceAsNotes =  chordMusicSequenceAsNotes.concat("\n Bar: " + (i+1)+" ");
                for (int j = 0; j < chordmidiSequenceArray[0].length; j++) {
                    chordMusicSequenceAsNotes = chordMusicSequenceAsNotes.concat("Beat: "+ (j+1)+" ");

                    /*index j represents the beat we are on in the bar, in case of 4/4 those are 4 (index -> 3)*/
                    for (int k = 0; k < chordmidiSequenceArray[0][0].length; k++) {
                        /*Introduces separating '-' signs to differntiate three-chords*/

                            chordMusicSequenceAsNotes = chordMusicSequenceAsNotes.concat( MidiUtils.midiValueToNoteName((int) chordmidiSequenceArray[i][j][k] + firebaseAPIClient.getSelectedSong().getBaseMidiNote()));





                        /*The three entries under index k represents the root, third and fifth of the chord*/




                    }



                }
            }

            String melodyMusicSequenceAsNotes = new String("\n MelodySequence (Notes): " );
            byte[][][] melodymidiSequenceArray =  generatedMusicSequencers[1].getMidiSequenceArray();
            for (int i = 0; i < melodymidiSequenceArray.length; i++) {
                /*Index i represents changing bar*/
                melodyMusicSequenceAsNotes = melodyMusicSequenceAsNotes.concat("\n Bar " + (i+1)+" : ");
                for (int j = 0; j < melodymidiSequenceArray[0].length; j++) {
                   melodyMusicSequenceAsNotes = melodyMusicSequenceAsNotes.concat(" Beat "+j +" : ");

                    /*index j represents the beat we are on in the bar, in case of 4/4 those are 4 (index -> 3)*/
                    for (int k = 0; k < melodymidiSequenceArray[0][0].length; k++) {
                        melodyMusicSequenceAsNotes = melodyMusicSequenceAsNotes.concat(MidiUtils.midiValueToNoteName((int)melodymidiSequenceArray[i][j][k] + firebaseAPIClient.getSelectedSong().getBaseMidiNote()));
                    }
                }
            }
            /*Now we inser the sequences into the textarea*/

            sequencesTextArea.setText(chordMusicSequenceAsNotes +"\n"+ melodyMusicSequenceAsNotes);

        }
    }


    public void onSignOutButtonClick(ActionEvent actionEvent) {
        this.changeLoggedInSignUpPanel(null);

        loginButton.setText("Log in");
        firebaseAPIClient.signOut(this);
    }

    /**Swaps the current panel for the other
     *
     * @param username
     */
    public void changeLoggedInSignUpPanel(String username) {
        if (loginSignUpHBox.isVisible()) {
            // Hide loginSignUpHBox and update the label for logged-in state
            loginSignUpHBox.setVisible(false);
            loginSignUpHBox.toBack();
            loggedInLabel.setText("Logged in as: " + username);

            // Make the loggedInAndSongTabPaneHBox visible and bring it to the front
            loggedInAndSongTabPaneHBox.setVisible(true);
            loggedInAndSongTabPaneHBox.toFront();

            // Update the loginButton to act as a sign-out button
            loginButton.setText("Sign out");

            // Make sure songTabPane is visible
            songTabPane.setVisible(true);

        } else {
            // Hide loggedInAndSongTabPaneHBox and reset the view back to login/signup state
            loggedInAndSongTabPaneHBox.setVisible(false);
            loggedInAndSongTabPaneHBox.toBack();

            // Reset the login button text
            loginButton.setText("Log in");

            // Make the loginSignUpHBox visible and bring it to the front
            loginSignUpHBox.setVisible(true);
            loginSignUpHBox.toFront();

            // Hide the songTabPane when logging out
            songTabPane.setVisible(false);
        }
    }

    /** When logged in this changes the current setting of loggedInPanel to
         *  progresstracker or vice versa -> Enables UX-tracking of loading progress
         */
        public void changeLoggedInPanelForProgressTrackerPanel() {
            // Toggle visibility of loggedInBox and loginSignUpBox
            if (loggedInAndSongTabPaneHBox.isVisible()) {
                // Hide the loggedIn panel and bring the login panel forward
                loggedInAndSongTabPaneHBox.setVisible(false);
                loggedInAndSongTabPaneHBox.toBack();

                loginSignUpHBox.setVisible(true);
                loginSignUpHBox.toFront();

                // Hide specific elements within the login panel
                usernameTextField.setVisible(false);
                passwordTextField.setVisible(false);
                sigunUpCheckBox.setVisible(false);
                loginButton.setVisible(false);
            } else if (progressAndButtonVBox.isVisible()) {
                // Restore login elements visibility in the loginSignUpBox
                usernameTextField.setVisible(true);
                passwordTextField.setVisible(true);
                sigunUpCheckBox.setVisible(true);
                loginButton.setVisible(true);

                // Hide the login panel and bring the loggedIn panel forward
                loginSignUpHBox.setVisible(false);
                loginSignUpHBox.toBack();

                loggedInAndSongTabPaneHBox.setVisible(true);
                loggedInAndSongTabPaneHBox.toFront();
            }
        }


}

