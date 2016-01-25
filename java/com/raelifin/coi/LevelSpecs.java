package com.raelifin.coi;

import java.util.ArrayList;

/**
 * Created by HP on 8/27/2015.
 */
public class LevelSpecs {

    public int[][] board;
    public int[][] colors;
    public int playerX, playerZ;
    public double playerTheta;
    public ArrayList<HeadSpec> headSpecs;

    public LevelSpecs(int[][] board, int playerX, int playerZ, double playerTheta, ArrayList<HeadSpec> headSpecs) {
        this(board, new int[board.length][board[0].length], playerX, playerZ, playerTheta, headSpecs);
    }

    public LevelSpecs(int[][] board, int[][] colors, int playerX, int playerZ, double playerTheta, ArrayList<HeadSpec> headSpecs) {
        this.board = board;
        this.colors = colors;
        this.playerX = playerX;
        this.playerZ = playerZ;
        this.playerTheta = playerTheta;
        this.headSpecs = headSpecs;
        if (board[playerZ][playerX] == 0) {
            throw new IllegalArgumentException("Invalid starting location!");
        }
    }

    public static LevelSpecs buildLevelZipideeDooDah() {
        ArrayList<HeadSpec> npcs = new ArrayList<HeadSpec>();
        npcs.add(new HeadSpec(2,1,0, new String[]{":coi","coi:coi"}, "face0"));
        return new LevelSpecs(new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,1,0,1,2,1,0,0},
                {0,1,1,1,0,1,1,0},
                {0,1,1,1,1,1,0,0},
                {0,0,2,0,1,1,1,0},
                {0,1,1,0,1,1,1,0},
                {0,0,0,0,0,0,0,0},
        }, 2, 2, -Math.PI/2, npcs);
    }

    public static LevelSpecs buildLevel1() {
        ArrayList<HeadSpec> npcs = new ArrayList<HeadSpec>();
        npcs.add(new HeadSpec(0, 1, Math.PI/2, new String[]{":*speaking coi","coi:*happy coi"}, "face0"));
        npcs.add(new HeadSpec(4, 1, Math.PI*3/2, new String[]{":*unhappy silence","coi:*happy levelupDing silence"}, "goalface"));
        return new LevelSpecs(new int[][]{
                {0,0,0,0,0},
                {0,1,2,1,0},
                {0,0,0,0,0},
        }, 1, 1, Math.PI, npcs);
    }

    public static LevelSpecs buildLevel2() {
        ArrayList<HeadSpec> npcs = new ArrayList<HeadSpec>();
        npcs.add(new HeadSpec(5, 1, Math.PI*3/2, new String[]{":*speaking coi","coi:*speaking ko klama fu le blanu"}, "face0"));
        npcs.add(new HeadSpec(1, 6, Math.PI, new String[]{":*unhappy silence", "coi:*happy levelupDing silence"}, "goalface"));
        return new LevelSpecs(new int[][]{
                {0,0,0,0,0,0,0,0,0},
                {0,0,1,2,1,0,0,1,0},
                {0,0,0,0,2,0,0,2,0},
                {0,1,2,2,1,2,2,1,0},
                {0,2,0,0,2,0,0,0,0},
                {0,1,0,0,2,0,1,0,0},
                {0,0,0,0,1,2,1,0,0},
                {0,0,0,0,0,0,0,0,0},
        }, new int[][]{
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,2,0},
                {0,0,0,0,0,0,0,2,0},
                {0,3,3,3,0,2,2,2,0},
                {0,3,0,0,1,0,0,0,0},
                {0,3,0,0,1,0,1,0,0},
                {0,0,0,0,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0},
        }, 2, 1, 0/*Math.PI*/, npcs);
    }
}
