package com.example.midistouchmalfunction54.net.beans;

import com.example.midistouchmalfunction54.music.MusicSequencer;

import java.io.Serializable;
import java.util.List;

public class SongBean implements Serializable {

    private String name;
    private List<MusicSequencerBean> chordAndMelodySequencers;
    private int bpm;
    /*Designates key of song*/
    private int baseMidiNote;

    /**The song bean contains relevant data for saving songs to
     *  the database ; name = name of song, chordAndMelodysequencers = chord and melody
     *  -sequences, bpm = beats per minute, baseMidiNote = key of song
     */
    public SongBean(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MusicSequencerBean> getChordAndMelodySequencers() {
        return chordAndMelodySequencers;
    }

    public void setChordAndMelodySequencers(List<MusicSequencerBean> chordAndMelodySequencers) {
        this.chordAndMelodySequencers = chordAndMelodySequencers;
    }



    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getBaseMidiNote() {
        return baseMidiNote;
    }

    public void setBaseMidiNote(int baseMidiNote) {
        this.baseMidiNote = baseMidiNote;
    }
}
