package com.example.midistouchmalfunction54.music;

public class TrackGenerator {

    // generates an array of Tracks (ChordTrack and MelodyTrack) based on bpm, key, and numberOfBars
    public static MusicSequencer[] generateTracks(byte numberOfBars, int chordNoteLength, int melodyNoteLength) {
        ChordMusicSequencer chordTrack = new ChordMusicSequencer(numberOfBars, chordNoteLength);
        MelodyMusicSequencer melodyTrack = new MelodyMusicSequencer(numberOfBars, melodyNoteLength);

        return new MusicSequencer[]{chordTrack, melodyTrack};
    }

}
