package com.raelifin.coi;

import com.min3d.core.Scene;
import com.min3d.vos.Number3d;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by HP on 8/15/2015.
 */
public class Map {

    public final Explorer player;

    public ArrayList<MapNode> mapNodes = new ArrayList<>();
    public ArrayList<MapEntity> decorations = new ArrayList<>();

    private final String[] colors = {"", "Red", "Green", "Blue"};

    public int nextLevelTimer = -1;

    public Map(LevelSpecs specs) {
        int[][] board = specs.board;
        MapNode[][] nodes = new MapNode[board.length][board[0].length];
        for (int z=0; z < board.length; z++) {
            for (int x = 0; x < board[z].length; x++) {
                String color = colors[specs.colors[z][x]];

                if (board[z][x] == 1) {

                    double theta = 0;
                    int neighbors = 0;
                    if (board[z-1][x]>0 && board[z+1][x]>0 && board[z][x-1]>0 && board[z][x+1]>0) {
                        theta = 0; neighbors = 4;
                    } else if (   (board[z-1][x]>0) &&   (board[z+1][x]>0) && ! (board[z][x-1]>0) &&   (board[z][x+1]>0)) {
                        theta = Math.PI; neighbors = 3;
                    } else if (   (board[z-1][x]>0) && ! (board[z+1][x]>0) &&   (board[z][x-1]>0) &&   (board[z][x+1]>0)) {
                        theta = Math.PI*3/2; neighbors = 3;
                    } else if (   (board[z-1][x]>0) &&   (board[z+1][x]>0) &&   (board[z][x-1]>0) && ! (board[z][x+1]>0)) {
                        theta = 0; neighbors = 3;
                    } else if ( ! (board[z-1][x]>0) &&   (board[z+1][x]>0) &&   (board[z][x-1]>0) &&   (board[z][x+1]>0)) {
                        theta = Math.PI/2; neighbors = 3;
                    } else if ( ! (board[z-1][x]>0) &&   (board[z+1][x]>0) &&   (board[z][x-1]>0) && ! (board[z][x+1]>0)) {
                        theta = 0; neighbors = 2;
                    } else if ( ! (board[z-1][x]>0) &&   (board[z+1][x]>0) && ! (board[z][x-1]>0) &&   (board[z][x+1]>0)) {
                        theta = Math.PI/2; neighbors = 2;
                    } else if (   (board[z-1][x]>0) && ! (board[z+1][x]>0) && ! (board[z][x-1]>0) &&   (board[z][x+1]>0)) {
                        theta = Math.PI; neighbors = 2;
                    } else if (   (board[z-1][x]>0) && ! (board[z+1][x]>0) &&   (board[z][x-1]>0) && ! (board[z][x+1]>0)) {
                        theta = Math.PI*3/2; neighbors = 2;
                    } else if ( ! (board[z-1][x]>0) && ! (board[z+1][x]>0) &&   (board[z][x-1]>0) && ! (board[z][x+1]>0)) {
                        theta = Math.PI*3/2; neighbors = 1;
                    } else if ( ! (board[z-1][x]>0) &&   (board[z+1][x]>0) && ! (board[z][x-1]>0) && ! (board[z][x+1]>0)) {
                        theta  = 0; neighbors = 1;
                    } else if ( ! (board[z-1][x]>0) && ! (board[z+1][x]>0) && ! (board[z][x-1]>0) &&   (board[z][x+1]>0)) {
                        theta  = Math.PI/2; neighbors = 1;
                    } else if (   (board[z-1][x]>0) && ! (board[z+1][x]>0) && ! (board[z][x-1]>0) && ! (board[z][x+1]>0)) {
                        theta = Math.PI; neighbors = 1;
                    }

                    nodes[z][x] = new MapNode(x*CoiEngine.CELL_SIZE, 0, z*CoiEngine.CELL_SIZE, theta, neighbors+color+"Platform", true);
                    mapNodes.add(nodes[z][x]);
                }
            }
        }
        Number3d[] deltas = {new Number3d(-1,0,0),new Number3d(1,0,0),new Number3d(0,0,-1),new Number3d(0,0,1)};
        for (int z=0; z < board.length; z++) {
            for (int x = 0; x < board[z].length; x++) {
                if (board[z][x] == 1) {
                    MapNode n = nodes[z][x];
                    for (Number3d delta : deltas) {
                        int dx = (int)(x+delta.x);
                        int dz = (int)(z+delta.z);
                        while (board[dz][dx] == 2) {
                            dx += delta.x; dz += delta.z;
                        }
                        if (board[dz][dx] == 1) {
                            n.addAdjacentNode(nodes[dz][dx]);
                        }
                    }
                }
            }
        }

        for (HeadSpec h : specs.headSpecs) {
            String color = colors[specs.colors[(int)h.z][(int)h.x]];
            MapNode head = new Head(h.x * CoiEngine.CELL_SIZE, 0, h.z * CoiEngine.CELL_SIZE, h.theta, "head"+color, h);
            mapNodes.add(head);
            for (int dz = -1; dz < 2; dz++) {
                for (int dx = -1; dx < 2; dx++) {
                    try {
                        MapNode n = nodes[(int)(h.z + dz)][(int)(h.x + dx)];
                        if (n != null) {
                            n.addAdjacentNode(head);
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                }
            }
        }

        for (int z=0; z < board.length; z++) {
            for (int x=0; x < board[z].length; x++) {
                if (board[z][x] == 2) {
                    String color = colors[specs.colors[z][x]];
                    decorations.add(new MapEntity(x * CoiEngine.CELL_SIZE, -1, z * CoiEngine.CELL_SIZE, board[z - 1][x] > 0 ? 0 : Math.PI/2, "straight2"+color+"Platform"));
                }
            }
        }
        for (float z=0.5f; z < board.length-1; z++) {
            for (float x=0.5f; x < board[(int)z].length-1; x++) {
                if (board[(int)z][(int)x] > 0 || board[(int)(z+1)][(int)x] > 0 || board[(int)(z+1)][(int)(x+1)] > 0 || board[(int)z][(int)(x+1)] > 0) {
                    decorations.add(new MapEntity(x*CoiEngine.CELL_SIZE, 0, z*CoiEngine.CELL_SIZE, 0, "pillar"));
                }
            }
        }

        player = new Explorer(specs.playerTheta, nodes[specs.playerZ][specs.playerX]);
    }

    public void update(int dt, DialogueLog textLog) {
        player.update(dt, textLog);
        for (MapNode n : mapNodes) {
            n.update(dt);
            if (n instanceof Head && ((Head)n).getTexture().equals("goalface_happy") && nextLevelTimer < 0) {
                Jukebox.play("levelupDing");
                nextLevelTimer = 800;
            }
        }
    }
}
