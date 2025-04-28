package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.*;

import java.util.ArrayList;

public class Main {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 45;
    public static final int HUD_HEIGHT = 2;
    public static ArrayList<TETile[][] > worlds = new ArrayList<>();
    public static TERenderer ter;


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        Main.ter = ter;
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setYscale(0, HEIGHT + HUD_HEIGHT);
        TETile[][] world1 = new TETile[WIDTH][HEIGHT];
        worlds.add(world1);
        World.makeNewWorld(world1);


        MainMenu menu = new MainMenu();
        menu.generateMenu();

        // World loop
        while (true) {
            //Avatar movement
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                World.moveAvatar(key, world1);
            }
            //HUD
            ter.renderFrame(world1);
            HUD.currentBlock(world1);
            StdDraw.show();
            StdDraw.pause(16);
        }



        //ter.renderFrame(world1);
    }
}
