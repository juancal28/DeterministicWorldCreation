package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Avatar {


    public static void addCharacter(TETile[][] world) {
        int[] pos = World.getStartRoom();
        world[pos[0]][pos[1]] = Tileset.AVATAR;
    }

}
