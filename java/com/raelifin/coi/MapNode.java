package com.raelifin.coi;

import android.media.MediaPlayer;

import com.min3d.core.Object3d;
import com.min3d.core.Object3dContainer;
import com.min3d.objectPrimitives.Box;
import com.min3d.vos.Color4;
import com.min3d.vos.Number3d;
import com.min3d.vos.TextureVo;

import java.util.ArrayList;

/**
 * Created by HP on 6/17/2015.
 */
public class MapNode extends MapEntity {
    private ArrayList<MapNode> adjacentNodes = new ArrayList<>();
    private ArrayList<Double> adjacentNodeThetas = new ArrayList<Double>();
    public final boolean traversable;
    protected MapNodeListener listener = null;

    public MapNode(double x, double y, double z, double rotation, String modelName, boolean traversable) {
        super(x,y,z,rotation,modelName);
        this.traversable = traversable;
    }

    public void setListener(MapNodeListener listener) {
        this.listener = listener;
    }

    public String fullString() {
        return "{"+((int)x)+","+((int)z)+"}->("+adjacentNodes+")";
    }

    public void addAdjacentNode(MapNode neighbor) {
        double theta = Math.atan2(neighbor.z-z, neighbor.x-x);
        adjacentNodes.add(neighbor);
        adjacentNodeThetas.add(theta);
    }

    public MapNode getNeighborThatBestMatchesTheta(double theta) {
        int bestIndex = 0;
        double bestError = Util.angleDistance(theta, adjacentNodeThetas.get(0));
        for (int i=1; i < adjacentNodeThetas.size(); i++) {
            double e = Util.angleDistance(theta, adjacentNodeThetas.get(i));
            if (e < bestError) {
                bestIndex = i;
                bestError = e;
            }
        }
        return adjacentNodes.get(bestIndex);
    }

    public double getTheta(MapNode n) {
        return adjacentNodeThetas.get(adjacentNodes.indexOf(n));
    }

    public void update(int dt) {}

    public void getTouched(DialogueLog textLog) {}

    public void hear(String s, DialogueLog textLogue) {}
}
