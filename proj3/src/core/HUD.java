package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;

import static edu.princeton.cs.algs4.StdDraw.*;

public class HUD {
    public static void currentBlock(TETile[][] world) {
        int worldWidth = world.length;
        int worldHeight = world[0].length;
        double hudAreaHeight = 2.0;
        double hudBottomY = worldHeight;
        double hudCenterY = hudBottomY + hudAreaHeight / 2.0;

        // Draw the HUD background
        setPenColor(StdDraw.BLACK);
        filledRectangle(worldWidth / 2.0, hudCenterY, worldWidth / 2.0, hudAreaHeight / 2.0);

        double mouseXCoord = mouseX();
        double mouseYCoord = mouseY();

        int x = (int) Math.floor(mouseXCoord);
        int y = (int) Math.floor(mouseYCoord);

        String tileType = "Out of bounds";
        if (x >= 0 && x < worldWidth && y >= 0 && y < worldHeight) {
            if (world[x][y] != null) {
                tileType = world[x][y].description();
            } else {
                tileType = "Empty";
            }
        }

        // Display the tile type
        setPenColor(StdDraw.WHITE);
        textLeft(1, hudCenterY, "Tile: " + tileType);

    }
}
