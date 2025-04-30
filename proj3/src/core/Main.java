package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.*;

import java.util.ArrayList;

public class Main {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 45;
    public static final int HUD_HEIGHT = 2;
    static ArrayList<TETile[][]> worlds = new ArrayList<>(Saving.loadGameIndex() + 1);
    static int worldIndex = Saving.loadGameIndex();
    static TETile[][] sight = new TETile[WIDTH][HEIGHT];
    static TERenderer ter;
    static Metadata gameData = null;

    public static void updateWorldIndex(int index) {
        worldIndex = index;
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        Main.ter = ter;
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setYscale(0, HEIGHT + HUD_HEIGHT);
        // Ensure worlds has enough elements
        for (int i = worlds.size(); i < worldIndex; i++) {
            worlds.add(new TETile[WIDTH][HEIGHT]);
        }

        //TETile[][] world1 = new TETile[WIDTH][HEIGHT];
        //worlds.add(world1);
        //World.makeNewWorld(worlds.get(worldIndex));


        // make sure to update the worldIndex utilizing the saving method
        MainMenu menu = new MainMenu();
        menu.generateMenu();
        gameData.changeSeed(World.getSeed());

        boolean clickedColon = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                gameData.addInput(key);
                System.out.println(gameData.getInputs().toString());
                if (World.getSightToggle()) {
                    Avatar.moveAvatar(key, sight);

                } else {
                    Avatar.moveAvatar(key, Main.worlds.get(worldIndex));

                }
                if (key == 'm' || key == 'M') {
                    menu.pauseGame();
                    menu.printPauseStatus();
                    System.out.println("Game paused");
                    menu.generateMenu();
                }
                if (key == 'v' || key == 'V') {
                    World.sightToggle();
                }
                if (key == ':') {
                    clickedColon = true;
                }
                if (clickedColon && key == 'q' || key == 'Q') {
                    Saving.saveGame(gameData);
                    clickedColon = false;
                    System.exit(0);
                }

            }

            if (World.getSightToggle()) {
                ter.renderFrame(sight);
            } else {
                ter.renderFrame(worlds.get(worldIndex));
            }
            HUD.currentBlock(worlds.get(worldIndex));
            StdDraw.show();
            StdDraw.pause(10);
        }



        //ter.renderFrame(world1);
    }
}
