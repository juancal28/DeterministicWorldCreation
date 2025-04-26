package core;

import tileengine.*;

import java.util.ArrayList;

public class Main {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 45;
    public static ArrayList<TETile[][] > worlds = new ArrayList<>();


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world1 = new TETile[WIDTH][HEIGHT];
        worlds.add(world1);


        MainMenu menu = new MainMenu();
        menu.generateMenu();

        //ter.renderFrame(world);
    }
}
