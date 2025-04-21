package core;

import tileengine.*;

public class Main {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 45;


    public static void makeNothing(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        makeNothing(world);
        World.makeRooms(world);

        ter.renderFrame(world);
    }
}
