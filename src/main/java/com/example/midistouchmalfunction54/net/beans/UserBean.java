package com.example.midistouchmalfunction54.net.beans;

import java.io.Serializable;
import java.util.List;


public class UserBean implements Serializable {

    private String username;
    private String password;
    /**A musicsequencer contains the chord and melody-sequencer,
     * First index %2 is type of sequencer, the second index is the cardinality of
     * the song in the list
     */
    private List<SongBean> songList;


    public UserBean() {

    }

    /**Returns a userbean with the set attributes
     *
     * @param usernameIn
     * @param passwordIn
     * @param songsIn
     * @return userBeanFromInput
     */
    public static UserBean buildUserBean(String usernameIn, String passwordIn, List<SongBean> songsIn){
        UserBean returnBean = new UserBean();
        returnBean.setUsername(usernameIn);
        returnBean.setPassword(passwordIn);
        returnBean.setSongList(songsIn);
        return returnBean;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SongBean> getSongList() {
        return songList;
    }

    public void setSongList(List<SongBean> songList) {
        this.songList = songList;
    }
    //    public List<List<MusicSequencer>> getSongs() {
//        return songs;
//    }
//
//    public void setSongs(MusicSequencer[][] songs) {
////        /*List of hord and melody track for one song - Needed to be list for firebase admin*/
////        List<List<MusicSequencer>> songList = null;
////        if (songs.length>0){
////        List<MusicSequencer> currentChordAndMelodySequencers;
////        /*This is all done due to firebase not being able to accept arrays, just lists*/
////        for(int i = 0 ; i < songs.length ; i ++ ){
////            /*Create a list of two music-sequences, chord and melody, for one song*/
////            currentChordAndMelodySequencers = List.of(songs[i][0],songs[i][1]);
////            /*Initiates the songList if first run-through, else just adds the sequences to the list of sequences*/
////            if(i == 0 ) songList = List.of(currentChordAndMelodySequencers);
////            else songList.add(currentChordAndMelodySequencers);
//
////
////        }
////        this.songs = songList;}
////        /*If signing up we want to add a empty songslist even though there is none currently */
////        else this.songs = List.of();
//    }
}
