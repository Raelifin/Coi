package com.raelifin.coi;

import android.media.MediaPlayer;
import android.widget.Button;

import com.min3d.vos.Number3d;
import com.min3d.vos.TextureVo;

import java.util.Map;

/**
 * Created by HP on 7/14/2015.
 */
public class Explorer {

    private double angularVelocity = 0.0015;
    private double velocity = 0.003;

    private MapNode destination;
    private MapNode lastNode;
    private Number3d position;
    private double theta;

    private boolean walking = false;
    private boolean moving = false;
    private boolean turningRight = false;
    private boolean facingLock = false;

    public Explorer(double theta, MapNode destination) {
        if (destination == null) {
            throw new IllegalArgumentException("Initial destination node must not be null.");
        }
        this.destination = destination;
        this.theta = theta;
        endMovement();
    }

    public Number3d getPos() {
        Number3d result = new Number3d();
        result.setAllFrom(position);
        return result;
    }

    public double getTheta() {
        return theta;
    }

    public void update(int dt, DialogueLog textLog) {
        double destinationTheta = Math.atan2(destination.z-position.z, destination.x-position.x);
        Number3d delta = destination.makePositionObj();
        delta.subtract(position);

        if (delta.length() < 0.1) {
            endMovement();
        } else {
            double angleDist = Util.angleDistance(theta, destinationTheta);
            if (angleDist < dt*angularVelocity) {
                theta = destinationTheta;
                if (walking) {
                    if ( ! destination.traversable) {
                        walking = false;
                        moving = false;
                        destination.getTouched(textLog);
                        System.out.println("Explorer.update says: I touched something! It was " + destination);
                    } else {
                        delta.normalize();
                        delta.multiply((float) (velocity * dt));
                        position.add(delta);
                    }
                } else {
                    moving = false;
                }
            } else {
                if (! facingLock) {
                    if (turningRight) {
                        theta = ((theta + dt * angularVelocity) % (2 * Math.PI));
                    } else {
                        theta = ((theta - dt * angularVelocity) % (2 * Math.PI));
                    }
                }
            }
        }
    }

    public void say(String s, DialogueLog textLog) {
        destination.hear(s, textLog);
    }

    public void walk() {
        walking = true;
        moving = true;
    }

    public void turn(boolean left) {
        if (walking) { return; }
        moving = true;
        facingLock = false;
        turningRight = !left;
        double delta = left? -Math.PI/1.8 : Math.PI/1.8; //bias towards the far end so that it doesn't adjust back to origin
        destination = lastNode.getNeighborThatBestMatchesTheta((theta + delta) % (2 * Math.PI));
        System.out.println("Now pointing at: " + destination.fullString());
    }

    public void endMovement() {
        walking = false;
        moving = false;
        lastNode = destination;
        position = lastNode.makePositionObj();
        destination = lastNode.getNeighborThatBestMatchesTheta(theta);
        facingLock = Util.angleDistance(theta, Math.atan2(destination.z-position.z, destination.x-position.x)) > Math.PI/4;
        turningRight = Util.isCloserOnTheRight(theta, lastNode.getTheta(destination));
        System.out.println("Now arriving at: " + lastNode.fullString());
    }

    public boolean canTurn(boolean left) {
        if (walking) { return false; }
        if (facingLock) { return true; }
        double delta = left? -Math.PI/1.8 : Math.PI/1.8; //bias towards the far end so that it doesn't adjust back to origin
        return ! destination.equals(lastNode.getNeighborThatBestMatchesTheta((theta+delta)%(2*Math.PI)));
    }

    public boolean canAdvance() {
        return ! moving && ! facingLock;
    }
}
