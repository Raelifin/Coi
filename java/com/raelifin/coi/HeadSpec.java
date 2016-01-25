package com.raelifin.coi;

import java.util.HashMap;

/**
 * Created by HP on 9/7/2015.
 */
public class HeadSpec {
    public final double x, z, theta;
    public final String baseTexture;
    public final HashMap<String,String> responseMap = new HashMap<>();

    public HeadSpec(double x, double z, double theta, String[] responseMap, String baseTexture) {
        this.x=x;
        this.z=z;
        this.theta=theta;
        this.baseTexture = baseTexture;
        for (String s : responseMap) {
            String key = s.substring(0,s.indexOf(':'));
            String value = s.substring(s.indexOf(':')+1);
            this.responseMap.put(key, value);
        }
    }
}
