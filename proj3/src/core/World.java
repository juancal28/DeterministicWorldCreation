package core;

import tileengine.*;

import java.util.Random;


public class World {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    private static int chunks;

    private static final Random rand = new Random(6578897764558030256L);

    //helper methods for room generation
    public static int getRoomNums() {
        int rooms;
        if (rand.nextInt() % 2 == 0) {
            rooms = 6;
        } else {
            rooms = 8;
        }
        return rooms;
    }
    public static int getRoomWidth() {
        int width;
        if (rand.nextInt() % 3 == 0) {
            width = 5;
        } else if (rand.nextInt() % 3 == 1) {
            width = 6;
        } else {
            width = 7;
        }
        return width;
    }
    public static int getRoomHeight() {
        int height;
        if (rand.nextInt() % 3 == 0) {
            height = 6;
        } else if (rand.nextInt() % 3 == 1) {
            height = 7;
        } else {
            height = 9;
        }
        return height;
    }
    public static int[][] chunkAreas(int roomNums) {
        int[][] chunks = new int[roomNums][2]; // Array to store x,y coordinates of chunks
        int minDistance = 2; // Minimum distance from world edge
        int chunkSize = 6;   // Approximate size of each chunk for overlap calculation

        // Calculate max bounds for chunk placement
        int maxX = WIDTH - minDistance;
        int maxY = HEIGHT - minDistance;

        // Generate roomNums unique chunk locations
        for (int i = 0; i < roomNums; i++) {
            boolean validPosition = false;
            int x = 0;
            int y = 0;

            int attempts = 0;
            while (!validPosition && attempts < 100) {
                // Generate a position that's at least minDistance away from the edges
                x = minDistance + rand.nextInt(maxX - minDistance + 1);
                y = minDistance + rand.nextInt(maxY - minDistance + 1);

                // Check for overlap with existing chunks
                validPosition = true;
                for (int j = 0; j < i; j++) {
                    // Calculate distance between current position and existing chunk
                    int existingX = chunks[j][0];
                    int existingY = chunks[j][1];

                    // If chunks are too close to each other (less than chunkSize apart)
                    if (Math.abs(x - existingX) < chunkSize && Math.abs(y - existingY) < chunkSize) {
                        validPosition = false;
                        break;
                    }
                }
                attempts++;
            }

            // Store the coordinates of the chunk
            chunks[i][0] = x;
            chunks[i][1] = y;
        }

        return chunks;
    }
    public static int[][] getRoomLocations(int[][] chunks) {
        int[][] roomPositions = new int[chunks.length][2]; // Array to store bottom-left positions of rooms

        for (int i = 0; i < chunks.length; i++) {
            // Get chunk center coordinates
            int chunkCenterX = chunks[i][0];
            int chunkCenterY = chunks[i][1];

            // Generate random offset within the chunk
            int offsetX = rand.nextInt(3) - 1; // Random offset between -1 and 1
            int offsetY = rand.nextInt(3) - 1; // Random offset between -1 and 1

            // Calculate the bottom-left corner position with some randomness
            int roomX = chunkCenterX + offsetX;
            int roomY = chunkCenterY + offsetY;

            // Ensure room is within world bounds
            roomX = Math.max(1, Math.min(roomX, WIDTH - 2));
            roomY = Math.max(1, Math.min(roomY, HEIGHT - 2));

            // Store the bottom-left position
            roomPositions[i][0] = roomX;
            roomPositions[i][1] = roomY;
        }

        return roomPositions;
    }

    public static void makeRooms(TETile[][] world) {
        chunks = getRoomNums();
        int[][] chunkLocations = chunkAreas(chunks);
        int[][] roomLocations = getRoomLocations(chunkLocations);

        for (int i = 0; i < chunks; i++) {
            int roomWidth = getRoomWidth();
            int roomHeight = getRoomHeight();

            // Get the bottom-left corner of the room
            int roomX = roomLocations[i][0];
            int roomY = roomLocations[i][1];

            // Draw the room with walls around the edges
            for (int x = roomX - 1; x <= roomX + roomWidth; x++) {
                for (int y = roomY - 1; y <= roomY + roomHeight; y++) {
                    // Check if we're at the perimeter
                    if (x == roomX - 1 || x == roomX + roomWidth ||
                        y == roomY - 1 || y == roomY + roomHeight) {
                        world[x][y] = Tileset.WALL;
                    } else {
                        // Interior of room
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }
}
