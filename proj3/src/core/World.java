package core;

import tileengine.*;
import java.util.*;


public class World {

    // Define Edge class for Prim's MST algorithm
    public static class Edge {
        int from;
        int to;
        double weight;

        public Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private static final int WIDTH = Main.WIDTH;
    private static final int HEIGHT = Main.HEIGHT;
    private static int chunks;
    private static HashMap<Integer, HashMap<Integer, Integer>> roomMap = new HashMap<>(); // Map to store room coordinates
    private static long seed = 5519969932840662953L; // Default seed
    private static Random rand = new Random(seed);
    private static int[] startRoom = new int[2];
    public static boolean sightToggle = false;
    private static boolean[] skippedRooms;

    //useful for other methods
    public static int[] getStartRoom() {
        return startRoom;
    }

    public static long getSeed() {
        return seed;
    }


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
        int randomNum = rand.nextInt(100);
        if (rand.nextInt() % 2 == 0 && randomNum < 50) {
            rooms = 12; // 4 x 3 rooms
        } else if (rand.nextInt() % 2 == 0 && randomNum >= 50) {
            rooms = 11; // 4 rooms, 3 rooms, then 4 rooms again
        } else if (rand.nextInt() % 2 == 1 && randomNum < 50) {
            rooms = 13; // 4 rooms, 5 rooms, then 4 rooms again
        } else {
            rooms = 14; // 5 rooms, 4 rooms, then 5 rooms again
        }
        return rooms;
    }
    public static int getRoomWidth() {
        int width;
        if (rand.nextInt() % 3 == 0) {
            width = 3;
        } else if (rand.nextInt() % 3 == 1) {
            width = 5;
        } else {
            width = 7;
        }
        return width;
    }

    public static int getRoomHeight() {
        int height;
        if (rand.nextInt() % 3 == 0) {
            height = 3;
        } else if (rand.nextInt() % 3 == 1) {
            height = 5;
        } else {
            height = 8;
        }
        return height;
    }

    public static int[][] chunkAreas(int roomNums) {
        // split world into 3 rows. Each row then split into x amount of columns, depending on the number of rooms
        int[][] chunks = new int[roomNums][2]; // Array to store x,y coordinates of chunks

        // when 12, standard 4 x 3
        if (roomNums == 12) {
            // split world into 3 rows
            int rowHeight = HEIGHT / 3;
            for (int i = 0; i < 3; i++) {
                int rowY = i * rowHeight;
                // split each row into columns
                int colWidth = WIDTH / 4;
                for (int j = 0; j < 4; j++) {
                    int colX = j * colWidth;
                    chunks[i * 4 + j][0] = colX;
                    chunks[i * 4 + j][1] = rowY;
                }
            }
        } else if (roomNums == 11) { // row w/ 4 rooms, then row w/ 3 rooms, then row w/ 4 rooms
            // split world into 3 rows
            int rowHeight = HEIGHT / 3;
            for (int i = 0; i < 3; i++) {
                int rowY = i * rowHeight;
                // split each row into columns
                if (i == 0) {
                    for (int j = 0; j < 4; j++) {
                        int colX = j * (WIDTH / 4);
                        chunks[j][0] = colX;
                        chunks[j][1] = rowY;
                    }
                } else if (i == 1) {
                    for (int j = 0; j < 3; j++) {
                        int colX = j * (WIDTH / 3);
                        chunks[4 + j][0] = colX;
                        chunks[4 + j][1] = rowY;
                    }
                } else {
                    for (int j = 0; j < 4; j++) {
                        int colX = j * (WIDTH / 4);
                        chunks[7 + j][0] = colX;
                        chunks[7 + j][1] = rowY;
                    }
                }
            }
        } else if (roomNums == 13) { // row w/ 4 rooms, then row w/ 5 rooms, then row w/ 4 rooms
            // split world into 3 rows
            int rowHeight = HEIGHT / 3;
            for (int i = 0; i < 3; i++) {
                int rowY = i * rowHeight;
                // split each row into columns
                if (i == 0) {
                    for (int j = 0; j < 4; j++) {
                        int colX = j * (WIDTH / 4);
                        chunks[j][0] = colX;
                        chunks[j][1] = rowY;
                    }
                } else if (i == 1) {
                    for (int j = 0; j < 5; j++) {
                        int colX = j * (WIDTH / 5);
                        chunks[4 + j][0] = colX;
                        chunks[4 + j][1] = rowY;
                    }
                } else {
                    for (int j = 0; j < 4; j++) {
                        int colX = j * (WIDTH / 4);
                        chunks[9 + j][0] = colX;
                        chunks[9 + j][1] = rowY;
                    }
                }
            }
        } else if (roomNums == 14) { // row w/ 5 rooms, then row w/ 4 rooms, then row w/ 5 rooms
            // split world into 3 rows
            int rowHeight = HEIGHT / 3;
            for (int i = 0; i < 3; i++) {
                int rowY = i * rowHeight;
                // split each row into columns
                if (i == 0) {
                    for (int j = 0 ; j < 5; j++) {
                        int colX = j * (WIDTH / 5);
                        chunks[j][0] = colX;
                        chunks[j][1] = rowY;
                    }
                } else if (i == 1) {
                    for (int j = 0; j < 4; j++) {
                        int colX = j * (WIDTH / 4);
                        chunks[5 + j][0] = colX;
                        chunks[5 + j][1] = rowY;
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        int colX = j * (WIDTH / 5);
                        chunks[9 + j][0] = colX;
                        chunks[9 + j][1] = rowY;
                    }
                }
            }
        }

        return chunks;
    }

    public static int[][] getRoomLocations(int[][] chunks) { // determine the location for the rooms
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

    public static void makeRooms(TETile[][] world) { // actually make the rooms in the TETile
        chunks = getRoomNums();
        int[][] chunkLocations = chunkAreas(chunks);
        skippedRooms = new boolean[chunks];
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
                skippedRooms[i] = true;
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
    public static void makeHallwayPaths(TETile[][] world) {
        if (roomMap.size() < 2) {
            return; // Not enough rooms to create hallways
        }

        // Create a graph to represent room connectivity
        RoomGraph roomGraph = new RoomGraph();

        // Add all rooms to the graph
        for (int id : roomMap.keySet()) {
            int roomIndex = id - 1; // Convert room ID to array index
            if (roomIndex >= 0 && roomIndex < skippedRooms.length && skippedRooms[roomIndex]) {
                continue;
            }
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



        // Connect all rooms using Prim's algorithm with priority queue
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        Map<Integer, Double> distanceTo = new HashMap<>();
        Map<Integer, Integer> edgeTo = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        // Initialize with first room
        visited.add(firstRoom);
        connectedRooms.add(firstRoom);

        // Add all edges from first room to priority queue
        for (int toRoom : roomIds) {
            if (toRoom != firstRoom) {
                double weight = roomGraph.getEdgeWeight(firstRoom, toRoom);
                distanceTo.put(toRoom, weight);
                edgeTo.put(toRoom, firstRoom);
                pq.offer(new Edge(firstRoom, toRoom, weight));
            }
        }

        // Process edges in order of increasing weight
        while (!pq.isEmpty() && connectedRooms.size() < roomMap.size()) {
            Edge edge = pq.poll();
            int to = edge.to;

            // Skip if we've already visited this room
            if (visited.contains(to)) {
                continue;
            }

            // Add this edge to our solution
            roomGraph.addEdge(edgeTo.get(to), to);
            visited.add(to);
            connectedRooms.add(to);

            // Create the physical hallway between these two rooms
            createHallway(world, roomMap.get(edgeTo.get(to)), roomMap.get(to));

            // Add new edges from this room to the priority queue
            for (int nextRoom : roomIds) {
                if (!visited.contains(nextRoom)) {
                    double weight = roomGraph.getEdgeWeight(to, nextRoom);
                    if (!distanceTo.containsKey(nextRoom) || weight < distanceTo.get(nextRoom)) {
                        distanceTo.put(nextRoom, weight);
                        edgeTo.put(nextRoom, to);
                        pq.offer(new Edge(to, nextRoom, weight));
                    }
                }
            }
        }
    }


    private static void createHallway(TETile[][] world, HashMap<Integer, Integer> room1, HashMap<Integer, Integer> room2) {
        Random tempRand = new Random(seed);
        int x1 = room1.keySet().iterator().next();
        int y1 = room1.get(x1) ;
        int x2 = room2.keySet().iterator().next();
        int y2 = room2.get(x2);

        // Add random offset but ensure we don't go past walls
        //x1 += Math.min(tempRand.nextInt(4) + 1, WIDTH - x1 - 3); // Stay within right boundary
        x2 += Math.min(tempRand.nextInt(2), WIDTH - x2 - 2); // Stay within right boundary

        // Randomly decide if horizontal or vertical hallway should be created first
        boolean horizontalFirst = new Random(seed + x1 + y1 + x2 + y2).nextBoolean();

        // Create hallways in the determined order
        if (horizontalFirst) {
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
        } else {
            // Create vertical hallway first
            int minY = Math.min(y1, y2);
            int maxY = Math.max(y1, y2);
            for (int y = minY; y <= maxY; y++) {
                // Place floor tile
                world[x1][y] = Tileset.FLOOR;

                // Place wall tiles to the left and right if they're not already floor tiles
                if (x1 + 1 < WIDTH && world[x1 + 1][y] != Tileset.FLOOR) {
                    world[x1 + 1][y] = Tileset.WALL;
                }
                if (x1 - 1 >= 0 && world[x1 - 1][y] != Tileset.FLOOR) {
                    world[x1 - 1][y] = Tileset.WALL;
                }
            }

            // Create horizontal hallway second
            int minX = Math.min(x1, x2);
            int maxX = Math.max(x1, x2);
            for (int x = minX; x <= maxX; x++) {
                // Place floor tile
                world[x][y2] = Tileset.FLOOR;

                // Place wall tiles above and below if they're not already floor tiles
                if (y2 + 1 < HEIGHT && world[x][y2 + 1] != Tileset.FLOOR) {
                    world[x][y2 + 1] = Tileset.WALL;
                }
                if (y2 - 1 >= 0 && world[x][y2 - 1] != Tileset.FLOOR) {
                    world[x][y2 - 1] = Tileset.WALL;
                }
            }
        }
    }

    public static void makeNewWorld(TETile[][] world) {
        makeNothing(world);
        makeRooms(world);
        makeHallwayPaths(world);
        Avatar.placeAvatar(world);
        Main.gameData = new Metadata(seed, "Game" + Main.worldIndex);
        Main.ter.renderFrame(world);
    }

    public static void sightToggle() {
        if (!sightToggle) {
            // Get avatar position from Avatar class
            int[] avatarPos = Avatar.getAvatarPosition();
            int avatarX = avatarPos[0];
            int avatarY = avatarPos[1];

            // Vision radius
            int visionRadius = 5;

            // Reset the sight board to NOTHING
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    Main.sight[x][y] = Tileset.NOTHING;
                }
            }

            // Check visibility in all directions within radius
            for (int x = Math.max(0, avatarX - visionRadius); x < Math.min(WIDTH, avatarX + visionRadius + 1); x++) {
                for (int y = Math.max(0, avatarY - visionRadius); y < Math.min(HEIGHT, avatarY + visionRadius + 1); y++) {
                    // Skip if position is out of bounds
                    if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
                        continue;
                    }

                    // Check line of sight from avatar to this position
                    if (hasLineOfSight(avatarX, avatarY, x, y, Main.worlds.get(Main.worldIndex))) {
                        Main.sight[x][y] = Main.worlds.get(Main.worldIndex)[x][y];
                    }
                }
            }

            sightToggle = !sightToggle;
        } else {
            // Toggle back to normal view
            sightToggle = !sightToggle;
        }
    }

    // Checks if there's a clear line of sight between two points using Bresenham's line algorithm
    private static boolean hasLineOfSight(int x0, int y0, int x1, int y1, TETile[][] world) {
        if (x0 == x1 && y0 == y1) {
            return true;
        }

        int distance = (int) Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
        if (distance > 5) {
            return false; // Beyond vision radius
        }

        // @Source https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
        // @Source ChatGPT pointed me to this algorithm
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int x = x0;
        int y = y0;

        while (true) {
            if (x == x1 && y == y1) {
                return true;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }

            if ((x != x1 || y != y1) && world[x][y] == Tileset.WALL) {
                return false;
            }
        }
    }

    public static boolean getSightToggle() {
        return sightToggle;
    }

    public static void changeSeed(long newSeed) {
        seed = newSeed;
        rand = new Random(newSeed);
    }


    public static void saveWorld(TETile[][] world) {
        // use information to save world
    }

    public static void restoreWorld(TETile[][] world) {
        // use information to restore world
    }
}