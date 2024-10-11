package com.example.midistouchmalfunction54.music;

import java.util.Arrays;
import java.util.Random;

public class MelodyMusicSequencer extends MusicSequencer {

    public MelodyMusicSequencer(byte numberOfBarsIn, int noteLengthIn) {
        super(numberOfBarsIn, noteLengthIn);
        super.setMidiSequence(makeMelodyTrack(numberOfBarsIn, noteLengthIn));

    }

    // generates a melody track
    private static byte[][][] makeMelodyTrack(byte numberOfBars, int noteLengthIn) {
        // initialize random object for easy melody creation
        Random rand = new Random();
        // using a simple pentatonic scale (agnostic to key)
        byte[] pentatonicScale = {12, 14, 16, 19, 21};
        byte[][][] midiSequenceArray = new byte[numberOfBars][16][1];


        // generate melody notes for each bar
        for (int bar = 0; bar < numberOfBars; bar++) {
            for (int beat = 0; beat < 16; beat++) {
                byte note = pentatonicScale[rand.nextInt(pentatonicScale.length)];
                midiSequenceArray[bar][beat][0] = note;
            }
        }

        System.out.println(Arrays.deepToString(midiSequenceArray));
        return midiSequenceArray;
    }
}