package com.example.midistouchmalfunction54.music;

import java.util.Arrays;

public class ChordMusicSequencer extends MusicSequencer {
    private byte numberOfBars;
    byte[][][] midiSequenceArray;

    public ChordMusicSequencer(byte numberOfBarsIn, int noteLengthIn){
        super(numberOfBarsIn, noteLengthIn);
        this.setMidiSequence(makeChordTrack(numberOfBarsIn, noteLengthIn));

    }

    // generates a chord progression track
    private static byte[][][] makeChordTrack(byte numberOfBars, int noteLength) {
        // defining the intervals of a major chord progression
        byte[] majorScaleIntervals = {2, 2, 1, 2, 2, 2, 1};

        // array to select root note for chords from index in scale array
        byte[] majorChordProgression = {0, 3, 4};

        // scale array covering 1 octave
        byte[] scaleArray = new byte[7];

        // define a root note at 0 which is transposed upon play
        scaleArray[0] = 0;

        // filling the scale array according to the pattern defined in majorScaleIntervals
        for (int i = 1; i < scaleArray.length; i++) {
            scaleArray[i] = (byte) (scaleArray[i - 1] + majorScaleIntervals[i - 1]);
        }

        // number of bars, 4 chords per bar, 3 notes in a chord
        byte[][][] midiSequenceArray = new byte[numberOfBars][4][3];

        // generating chords for each bar
        for (int bar = 0; bar < numberOfBars; bar++) {
            for (int beat = 0; beat < 4; beat++) {
                int chordIndex = majorChordProgression[beat % majorChordProgression.length];
                byte root = scaleArray[chordIndex % scaleArray.length];
                byte third = scaleArray[(chordIndex + 2) % scaleArray.length];
                byte fifth = scaleArray[(chordIndex + 4) % scaleArray.length];

                midiSequenceArray[bar][beat] = new byte[]{root, third, fifth};

            }
        }

        System.out.println(Arrays.deepToString(midiSequenceArray));
        return midiSequenceArray;
    }
}