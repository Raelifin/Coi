package com.raelifin.coi;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import java.util.HashMap;

/**
 * Created by HP on 8/13/2015.
 */
public class Jukebox {

    private static HashMap<String,MediaPlayer> map = new HashMap<String,MediaPlayer>();

    public static void init(Context context) {
        map.put("silence", MediaPlayer.create(context, R.raw.silence));
        map.put("levelupDing", MediaPlayer.create(context, R.raw.ding));
        map.put("click", MediaPlayer.create(context, R.raw.click));
        map.put("coi", MediaPlayer.create(context, R.raw.voice_coi));
        map.put("ko", MediaPlayer.create(context, R.raw.voice_ko));
        map.put("klama", MediaPlayer.create(context, R.raw.voice_klama));
        map.put("fu", MediaPlayer.create(context, R.raw.voice_fu));
        map.put("le", MediaPlayer.create(context, R.raw.voice_le));
        map.put("blanu", MediaPlayer.create(context, R.raw.voice_blanu));
    }

    public static int play(String name) {
        if (map.containsKey(name)) {
            map.get(name).start();
            return map.get(name).getDuration();
        } else {
            System.out.println("Jukebox says no audio by name \""+name+"\"!");
            return -1;
        }
    }
}
