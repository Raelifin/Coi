package com.raelifin.coi;

/**
 * Created by HP on 7/14/2015.
 */
public class Util {

    public static double angleDistance(double a, double b) {
        double result = Math.abs(a - b) % (Math.PI*2);
        if (result > Math.PI) { result = Math.PI*2 - result; }
        return result;
    }

    public static boolean isCloserOnTheRight(double myTheta, double targetTheta) {
        double t = targetTheta-myTheta;
        while (t < 0) { t += Math.PI*2; }
        while (t > Math.PI*2) { t -= Math.PI*2; }
        return t < Math.PI;
    }
}
