package core;

import tileengine.*;

public class Main {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 45;



    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        //makeNothing() ALWAYS needs to go first!
        World.makeNothing(world);
        MainMenu menu = new MainMenu(ter);
        menu.generateMenu(world);

        ter.renderFrame(world);
    }
}
