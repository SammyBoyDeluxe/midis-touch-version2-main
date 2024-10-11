package com.example.midistouchmalfunction54.utilities;

import com.example.midistouchmalfunction54.music.MusicSequencer;
import com.example.midistouchmalfunction54.net.beans.MusicSequencerBean;
import com.example.midistouchmalfunction54.net.beans.SongBean;

import java.util.List;

public class MidiUtils {
    /*Creates a songbean from valid data*/
    public static SongBean createSongBean(String name, int bpm, int key, MusicSequencer[] chordAndMelodySequencers){
        SongBean songBean = new SongBean();
        songBean.setBaseMidiNote(key);
        songBean.setName(name);
        songBean.setChordAndMelodySequencers(List.of(MusicSequencerBean.buildMusicSequencerBeanFromMusicSequencer(chordAndMelodySequencers[0]),MusicSequencerBean.buildMusicSequencerBeanFromMusicSequencer(chordAndMelodySequencers[1])));
        songBean.setBpm(bpm);
        return songBean;
    }



    // Method to convert note names to MIDI values
    public static int noteToMidiValue(String noteName) {
        return switch (noteName) {
            case "C" -> 60;
            case "C#" -> 61;
            case "D" -> 62;
            case "D#" -> 63;
            case "E" -> 64;
            case "F" -> 65;
            case "F#" -> 66;
            case "G" -> 67;
            case "G#" -> 68;
            case "A" -> 69;
            case "A#" -> 70;
            case "B" -> 71;
            default -> 60; // Default to Middle C
        };
    }

    public static String midiValueToNoteName(int midiValue) {
        /*Used to find what note corresponds to the given note value, since there are 12 notes it restarts on the twelfth*/
        int noteValue = midiValue % 12;
        return switch (noteValue) {
            case 0 -> "C";
            case 1 -> "C#";
            case 2 -> "D";
            case 3 -> "D#";
            case 4 -> "E";
            case 5 -> "F";
            case 6 -> "F#";
            case 7 -> "G";
            case 8 -> "G#";
            case 9 -> "A";
            case 10 -> "A#";
            case 11 -> "B";
            default -> "C"; // Default to "C" if the midiValue is out of range
        };
    }
}
