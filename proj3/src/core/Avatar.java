package core;

import tileengine.TETile;
import tileengine.Tileset; // Import Tileset to use FLOOR, AVATAR etc.

public class Avatar {
    private static int avatarX;
    private static int avatarY;

    public static void placeAvatar(TETile[][] world) {
        for (int x = 0; x < Main.WIDTH; x++) {
            for (int y = 0; y < Main.HEIGHT; y++) {
                if (world[x][y].description().equals("floor")) {
                    avatarX = x;
                    avatarY = y;
                    world[x][y] = Tileset.AVATAR;
                    return;
                }
            }
        }
    }

    public static int[] getAvatarPosition() {
        return new int[]{avatarX, avatarY};
    }

    public static void moveAvatar(char direction, TETile[][] world) {
        int targetX = avatarX;
        int targetY = avatarY;

        // Move avatar using WASD
        switch (Character.toLowerCase(direction)) {
            case 'w':
                targetY += 1;
                break;
            case 'a':
                targetX -= 1;
                break;
            case 's':
                targetY -= 1;
                break;
            case 'd':
                targetX += 1;
                break;
            default:
                return;
        }

        if (targetX < 0 || targetX >= Main.WIDTH || targetY < 0 || targetY >= Main.HEIGHT) {
            return;
        }

        // Always check if movement is valid in the actual world (not sight world)
        TETile targetTile = Main.worlds.get(Main.worldIndex)[targetX][targetY];

        // Check if the target tile is not a wall
        if (!targetTile.description().equals("wall")) {
            // Update the actual world
            Main.worlds.get(Main.worldIndex)[avatarX][avatarY] = Tileset.FLOOR;
            Main.worlds.get(Main.worldIndex)[targetX][targetY] = Tileset.AVATAR;

            // Also update the sight world if it's being used
            if (World.getSightToggle()) {
                // Check if these positions are within the sight range before updating
                if (Math.abs(avatarX - targetX) <= 2 && Math.abs(avatarY - targetY) <= 2) {
                    Main.sight[avatarX][avatarY] = Tileset.FLOOR;
                }
                Main.sight[targetX][targetY] = Tileset.AVATAR;
            }

            avatarX = targetX;
            avatarY = targetY;

            // Update sight range if toggled
            if (World.getSightToggle()) {
                updateSightRange();
            }
        }
    }

    private static void updateSightRange() {
        // First reset all tiles outside visibility range back to NOTHING
        for (int x = 0; x < Main.WIDTH; x++) {
            for (int y = 0; y < Main.HEIGHT; y++) {
                if (Math.abs(x - avatarX) <= 2 && Math.abs(y - avatarY) <= 2) {
                    // Update tiles in sight range to match the actual world
                    Main.sight[x][y] = Main.worlds.get(Main.worldIndex)[x][y];
                } else {
                    Main.sight[x][y] = Tileset.NOTHING;
                }
            }
        }
    }
}
