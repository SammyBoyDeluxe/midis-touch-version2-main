package com.example.midistouchmalfunction54.net.beans;

import com.example.midistouchmalfunction54.music.ChordMusicSequencer;
import com.example.midistouchmalfunction54.music.MelodyMusicSequencer;
import com.example.midistouchmalfunction54.music.MusicSequencer;

import java.io.Serializable;
import java.util.List;

public class MusicSequencerBean implements Serializable {

    protected byte numberOfBars;
    protected int noteLength;

    public byte[][][] midiSequenceArray;

    public MusicSequencerBean() {
    }

    /**A static method for building a music-sequencer
     *
     * @param numberOfBarsIn
     * @param noteLengthIn
     * @param midiSequenceArrayIn
     * @return
     */
    public static  MusicSequencerBean buildMusicSequencerBeanFromAttributes(byte numberOfBarsIn, int noteLengthIn, byte[][][] midiSequenceArrayIn) {
        MusicSequencerBean returnBean = new MusicSequencerBean();
        returnBean.setNumberOfBars(numberOfBarsIn);
        returnBean.setNoteLength(noteLengthIn);
        returnBean.setMidiSequenceArray(midiSequenceArrayIn);
        return returnBean;
    }

    public static MusicSequencerBean buildMusicSequencerBeanFromMusicSequencer(MusicSequencer sequencer){
        MusicSequencerBean returnBean = new MusicSequencerBean();
        returnBean.setNumberOfBars(sequencer.getNumberOfBars());
        returnBean.setNoteLength(sequencer.getNoteLength());
        returnBean.setMidiSequenceArray(sequencer.getMidiSequenceArray());
        return returnBean;

    }

    /**Used to convert the list of MusicSequencerBeans stored in the SongBean-entities to MusicSequencerArray
     *
     * @param sequencerBeans
     * @return
     */
    public static MusicSequencer[] buildMusicSequencerArrayFromMusicSequencerBeanList(List<MusicSequencerBean> sequencerBeans){
        MusicSequencerBean chordSequenceBean =sequencerBeans.get(0);
        MusicSequencerBean melodySequenceBean = sequencerBeans.get(1);

        ChordMusicSequencer chordMusicSequencer = new ChordMusicSequencer(chordSequenceBean.getNumberOfBars(), chordSequenceBean.getNoteLength());
        chordMusicSequencer.setMidiSequenceArray(chordSequenceBean.getMidiSequenceArray());
        MelodyMusicSequencer melodyMusicSequencer = new MelodyMusicSequencer(melodySequenceBean.getNumberOfBars(), melodySequenceBean.getNoteLength());
        melodyMusicSequencer.setMidiSequenceArray(chordSequenceBean.getMidiSequenceArray());

        return new MusicSequencer[]{chordMusicSequencer, melodyMusicSequencer};
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
