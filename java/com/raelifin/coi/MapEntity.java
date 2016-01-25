package com.raelifin.coi;

import com.min3d.vos.Number3d;

/**
 * Created by HP on 8/18/2015.
 */
public class MapEntity {

    public final double x, y, z;
    public final double rotation;
    public final String modelName;

    public MapEntity(double x, double y, double z, double rotation, String modelName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.modelName = modelName;
    }

    public Number3d makePositionObj() {
        return new Number3d((float)x,(float)y,(float)z);
    }

    public String toString() {
        return "{"+((int)x)+","+((int)z)+"}";
    }

}
