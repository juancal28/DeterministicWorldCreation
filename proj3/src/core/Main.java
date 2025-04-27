package core;

import tileengine.*;

import java.util.ArrayList;

public class Main {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 45;
    public static ArrayList<TETile[][] > worlds = new ArrayList<>();
    public static TERenderer ter;


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        Main.ter = ter;
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world1 = new TETile[WIDTH][HEIGHT];
        worlds.add(world1);


        MainMenu menu = new MainMenu();
        menu.generateMenu();
        //World.makeNewWorld(world1);

        //ter.renderFrame(world1);
    }
}
