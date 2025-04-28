package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.*;

import java.util.ArrayList;

public class Main {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 45;
    public static final int HUD_HEIGHT = 2;
    public static ArrayList<TETile[][]> worlds = new ArrayList<>();
    public static int worldIndex = 0;
    public static TETile[][] sight = new TETile[WIDTH][HEIGHT];
    public static TERenderer ter;
    private static Metadata gameData;



    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        Main.ter = ter;
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setYscale(0, HEIGHT + HUD_HEIGHT);
        TETile[][] world1 = new TETile[WIDTH][HEIGHT];
        worlds.add(world1);
        World.makeNewWorld(world1);
        long seed = World.getSeed();


        MainMenu menu = new MainMenu();
        menu.generateMenu();
        Metadata gameData = new Metadata(seed, "Game" + worldIndex);


        while (true) {

            while (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (World.getSightToggle()){
                    Avatar.moveAvatar(key, sight);

                } else {
                    Avatar.moveAvatar(key, worlds.get(worldIndex));
                    gameData.addInput(key);

                }
                if (key == 'm' || key == 'M') {
                    menu.generateMenu();
                }
                if (key == 'v' || key == 'V') {
                    World.sightToggle();

                }
                if (key == ':') {
                    
                }

            }

            if (World.getSightToggle()) {
                ter.renderFrame(sight);
            } else {
                ter.renderFrame(worlds.get(worldIndex));
            }
            HUD.currentBlock(world1);
            StdDraw.show();
            StdDraw.pause(16);
        }



        //ter.renderFrame(world1);
    }
}
