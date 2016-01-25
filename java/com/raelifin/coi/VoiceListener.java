package com.raelifin.coi;

/**
 * Created by HP on 8/27/2015.
 */
public interface VoiceListener {
    public void voiceError(String message);
    public void hearSpeech(String message);
    public void voiceReady();
}
