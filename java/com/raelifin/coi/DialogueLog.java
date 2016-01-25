package com.raelifin.coi;

import java.util.ArrayList;

/**
 * Created by HP on 10/10/2015.
 */
public class DialogueLog {

    private ArrayList<String> log = new ArrayList<>();
    private boolean needsRepaint = true;

    public void add(String s) {
        log.add(0, s);
        needsRepaint = true;
    }

    public ArrayList<String> getLogCopy() {
        return new ArrayList<String>(log);
    }

    public boolean needsRepaint() {
        return needsRepaint;
    }
}
