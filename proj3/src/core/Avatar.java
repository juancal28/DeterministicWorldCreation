package core;

import tileengine.TETile;
import tileengine.Tileset; // Import Tileset to use FLOOR, AVATAR etc.

public class Avatar {
    private int x;
    private int y;
    private final TETile tile; // The avatar's appearance

    /**
     * Constructor for the Avatar.
     * @param startX The initial X coordinate.
     * @param startY The initial Y coordinate.
     * @param avatarTile The TETile representing the avatar.
     */
    public Avatar(int startX, int startY, TETile avatarTile) {
        this.x = startX;
        this.y = startY;
        this.tile = avatarTile;
    }

    /**
     * Attempts to move the avatar in the given direction.
     * Checks for world boundaries and wall collisions.
     * Updates the avatar's internal coordinates only if the move is valid.
     *
     * @param direction The character representing the direction ('w', 'a', 's', 'd').
     * @param world The world grid used for collision detection.
     * @return true if the avatar successfully moved, false otherwise.
     */
    public boolean move(char direction, TETile[][] world) {
        int targetX = this.x;
        int targetY = this.y;
        int worldWidth = world.length;
        int worldHeight = world[0].length;

        // Calculate target coordinates
        switch (Character.toLowerCase(direction)) {
            case 'w': targetY++; break;
            case 'a': targetX--; break;
            case 's': targetY--; break;
            case 'd': targetX++; break;
            default: return false; // Not a movement key
        }

        // --- Collision and Bounds Detection ---
        // Check bounds
        if (targetX < 0 || targetX >= worldWidth || targetY < 0 || targetY >= worldHeight) {
            return false; // Cannot move off map
        }

        // Check target tile for walls
        TETile targetTile = world[targetX][targetY];
        if (targetTile.description().equals("wall")) {
            return false; // Cannot move into a wall
        }

        // --- Move is Valid: Update internal coordinates ---
        this.x = targetX;
        this.y = targetY;
        return true; // Move succeeded
    }

    // --- Getters ---
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TETile getTile() {
        return tile;
    }
}