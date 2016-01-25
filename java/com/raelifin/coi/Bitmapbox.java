package com.raelifin.coi;

import android.graphics.Bitmap;

import com.min3d.Shared;
import com.min3d.Utils;

import java.util.HashMap;

/**
 * Created by HP on 10/9/2015.
 */
public abstract class Bitmapbox {

    private static HashMap<String,Bitmap> map = new HashMap<>();
    private static boolean initialized = false;

    public static void init(CoiEngine e) {
        registerBitmap(e, R.drawable.goalface_neutral, "goalface_neutral");
        registerBitmap(e, R.drawable.goalface_happy, "goalface_happy");
        registerBitmap(e, R.drawable.goalface_unhappy, "goalface_unhappy");
        registerBitmap(e, R.drawable.goalface_speaking, "goalface_speaking");
        registerBitmap(e, R.drawable.face0_neutral, "face0_neutral");
        registerBitmap(e, R.drawable.face0_happy, "face0_happy");
        registerBitmap(e, R.drawable.face0_unhappy, "face0_unhappy");
        registerBitmap(e, R.drawable.face0_speaking, "face0_speaking");
        initialized = true;
    }

    public static final void registerBitmap(CoiEngine e, int id, String name) {
        Bitmap b = Utils.makeBitmapFromResourceId(e, id);
        Shared.textureManager().addTextureId(b, name, false);
        map.put(name, b);
    }

    public static Bitmap get(String key) {
        return map.get(key);
    }

    public static boolean isReady() {
        return initialized;
    }
}
