package core;

import tileengine.*;

import java.util.*;


public class World {

    private static final int WIDTH = Main.WIDTH;
    private static final int HEIGHT = Main.HEIGHT;
    private static int chunks;
    private static HashMap<Integer, HashMap<Integer, Integer>> roomMap = new HashMap<>();
    private static Random rand = new Random(2476468437338197851L);
    private static int avatarX;
    private static int avatarY;

    private static void placeAvatar(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (world[x][y].description().equals("floor")) {
                    avatarX = x;
                    avatarY = y;
                    world[x][y] = Tileset.AVATAR;
                    return;
                }
            }
        }
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

        if (targetX < 0 || targetX >= WIDTH || targetY < 0 || targetY >= HEIGHT) {
            return;
        }

        TETile targetTile = world[targetX][targetY];

        // Check if the target tile is not a wall
        if (!targetTile.description().equals("wall")) {
            world[avatarX][avatarY] = Tileset.FLOOR;

            avatarX = targetX;
            avatarY = targetY;

            // Place avatar at target location
            world[avatarX][avatarY] = Tileset.AVATAR;
        }
    }

    //constructors



    //make blank world
    public static void makeNothing(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

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

        // Keep track of where rooms have been placed
        boolean[][] occupied = new boolean[WIDTH][HEIGHT];
        int roomCounter = 1;
        for (int i = 0; i < chunks; i++) {
            int roomWidth = getRoomWidth();
            int roomHeight = getRoomHeight();

            // Get the bottom-left corner of the room
            int roomX = roomLocations[i][0];
            int roomY = roomLocations[i][1];

            // Check and adjust room dimensions to avoid overlapping
            boolean fits = false;
            // dimensions need to be > 2 in order to not be a hallway
            while (!fits && roomWidth > 2 && roomHeight > 2) {
                fits = true;
                // Check if this room would overlap with existing rooms
                for (int x = roomX - 1; x <= roomX + roomWidth && fits; x++) {
                    for (int y = roomY - 1; y <= roomY + roomHeight && fits; y++) {
                        // Make sure coordinates are in bounds
                        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
                            fits = false;
                            break;
                        }
                        // Check if this position is already occupied by another room
                        if (occupied[x][y]) {
                            fits = false;
                            break;
                        }
                    }
                }

                // If room doesn't fit, reduce dimensions
                if (!fits) {
                    if (roomWidth > roomHeight) {
                        roomWidth--;
                    } else {
                        roomHeight--;
                    }
                }
            }

            // Skip this room if it's too small
            if (roomWidth <= 2 || roomHeight <= 2) {
                continue;
            }

            // add info to the roomMap
            HashMap<Integer, Integer> coordinates = new HashMap<>();
            coordinates.put(roomX, roomY);
            roomMap.put(roomCounter, coordinates);
            roomCounter++;

            // Draw the room with walls around the edges
            for (int x = roomX - 1; x <= roomX + roomWidth; x++) {
                for (int y = roomY - 1; y <= roomY + roomHeight; y++) {
                    // Mark position as occupied
                    occupied[x][y] = true;

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
    public static void makeHallways(TETile[][] world) {
        if (roomMap.size() < 2) {
            return; // Not enough rooms to create hallways
        }

        // Create a graph to represent room connectivity
        RoomGraph roomGraph = new RoomGraph();

        // Add all rooms to the graph
        for (int id : roomMap.keySet()) {
            HashMap<Integer, Integer> room = roomMap.get(id);
            int x = room.keySet().iterator().next();
            int y = room.get(x);
            // Using 5x5 as placeholder dimensions since we only need position for hallways
            roomGraph.addRoom(x, y, 5, 5, id);
        }

        // Build a minimum spanning tree to connect rooms efficiently
        Set<Integer> connectedRooms = new HashSet<>();
        List<Integer> roomIds = new ArrayList<>(roomMap.keySet());

        // Start with random room
        int firstRoom = roomIds.get(0);
        connectedRooms.add(firstRoom);

        // Connect all rooms using Prim's algorithm
        while (connectedRooms.size() < roomMap.size()) {
            double minDistance = Double.MAX_VALUE;
            int roomToConnect = -1;
            int connectToRoom = -1;

            // Find the closest unconnected room to any connected room
            for (int connectedId : connectedRooms) {
                for (int unconnectedId : roomIds) {
                    if (!connectedRooms.contains(unconnectedId)) {
                        // Use the RoomGraph's getEdgeWeight method instead of calculating distance separately
                        double distance = roomGraph.getEdgeWeight(connectedId, unconnectedId);
                        if (distance < minDistance) {
                            minDistance = distance;
                            roomToConnect = unconnectedId;
                            connectToRoom = connectedId;
                        }
                    }
                }
            }

            // Add the edge to our graph
            if (roomToConnect != -1) {
                roomGraph.addEdge(connectToRoom, roomToConnect);
                connectedRooms.add(roomToConnect);

                // Create the physical hallway between these two rooms
                createHallway(world, roomMap.get(connectToRoom), roomMap.get(roomToConnect));
            }
        }
    }
    private static void createHallway(TETile[][] world, HashMap<Integer, Integer> room1, HashMap<Integer, Integer> room2) {
        int x1 = room1.keySet().iterator().next();
        int y1 = room1.get(x1);
        int x2 = room2.keySet().iterator().next();
        int y2 = room2.get(x2);

        // Create horizontal hallway first
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        for (int x = minX; x <= maxX; x++) {
            // Place floor tile
            world[x][y1] = Tileset.FLOOR;

            // Place wall tiles above and below if they're not already floor tiles
            if (y1 + 1 < HEIGHT && world[x][y1 + 1] != Tileset.FLOOR) {
                world[x][y1 + 1] = Tileset.WALL;
            }
            if (y1 - 1 >= 0 && world[x][y1 - 1] != Tileset.FLOOR) {
                world[x][y1 - 1] = Tileset.WALL;
            }
        }

        // Create vertical hallway second
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        for (int y = minY; y <= maxY; y++) {
            // Place floor tile
            world[x2][y] = Tileset.FLOOR;

            // Place wall tiles to the left and right if they're not already floor tiles
            if (x2 + 1 < WIDTH && world[x2 + 1][y] != Tileset.FLOOR) {
                world[x2 + 1][y] = Tileset.WALL;
            }
            if (x2 - 1 >= 0 && world[x2 - 1][y] != Tileset.FLOOR) {
                world[x2 - 1][y] = Tileset.WALL;
            }
        }
    }

    public static void makeNewWorld(TETile[][] world) {
        makeNothing(world);
        makeRooms(world);
        makeHallways(world);
        Main.ter.renderFrame(world);
        placeAvatar(world);
    }

    public static void changeSeed(long seed) {
        rand = new Random(seed);
    }

    public static void saveWorld(TETile[][] world) {
        // use information to save world
    }

    public static void restoreWorld(TETile[][] world) {
        // use information to restore world
    }
}
