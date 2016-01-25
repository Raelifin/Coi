package com.raelifin.coi;

import android.app.Dialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HP on 9/1/2015.
 */
public class Head extends MapNode {

    private static int WORD_SPACE_IN_MS = 0;

    private HashMap<String,String> responseMap;

    private String[] wordQueue = null;
    private int speakingIndex = -1;
    private int speakingCountdown = 0;

    private String rootTexture;
    private String texture;

    public Head(double x, double y, double z, double rotation, String modelName, HeadSpec spec) {
        super(x,y,z,rotation,modelName, false);
        rootTexture = spec.baseTexture+"_";
        texture = rootTexture+"neutral";
        this.responseMap = spec.responseMap;
    }

    public String getTexture() { return texture; }

    public void update(int dt) {
        if (listener != null && wordQueue != null) {
            speakingCountdown -= dt;
            if (speakingCountdown <= 0) {
                speakingIndex++;
                if (speakingIndex >= wordQueue.length) {
                    wordQueue = null;
                    texture = rootTexture+"neutral";
                    listener.changeTexture(texture);
                } else {
                    speakingCountdown = Jukebox.play(wordQueue[speakingIndex]) + WORD_SPACE_IN_MS;
                }
            }
        }
    }

    @Override
    public void getTouched(DialogueLog textLog) {
        if (wordQueue == null) {
            hear("", textLog);
        }
    }

    @Override
    public void hear(String s, DialogueLog textLog) {
        if (listener != null && responseMap.containsKey(s)) {

            wordQueue = responseMap.get(s).split(" ");
            speakingIndex = 0;

            String display = "";
            for (String w : wordQueue) {
                if (w.startsWith("*")) { continue; }
                display += w+" ";
            }
            if (display.length() > 0) {
                display = display.substring(0, display.length()-1);
                textLog.add(rootTexture+"neutral:"+display);
            }

            if (wordQueue[speakingIndex].startsWith("*")) {
                speakingCountdown = -1;
                texture = rootTexture+wordQueue[speakingIndex].substring(1);
                listener.changeTexture(texture);
            } else {
                speakingCountdown = Jukebox.play(wordQueue[speakingIndex]) + WORD_SPACE_IN_MS;
            }
        }
    }
}
