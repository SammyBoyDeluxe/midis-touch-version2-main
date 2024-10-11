package com.example.midistouchmalfunction54.music;

public abstract class MusicSequencer {
    protected byte numberOfBars;
    protected int noteLength;

    public byte[][][] midiSequenceArray;

    protected MusicSequencer(byte numberOfBarsIn, int noteLengthIn){
        this.numberOfBars = numberOfBarsIn;
        this.noteLength = noteLengthIn;

    }

    protected void setMidiSequence(byte[][][] midiSequenceArrayIn){

        this.midiSequenceArray = midiSequenceArrayIn;

    }

    public byte getNumberOfBars() {
        return numberOfBars;
    }

    public void setNumberOfBars(byte numberOfBars) {
        this.numberOfBars = numberOfBars;
    }

    public int getNoteLength() {
        return noteLength;
    }

    public void setNoteLength(int noteLength) {
        this.noteLength = noteLength;
    }

    public byte[][][] getMidiSequenceArray() {
        return midiSequenceArray;
    }

    public void setMidiSequenceArray(byte[][][] midiSequenceArray) {
        this.midiSequenceArray = midiSequenceArray;
    }
}
