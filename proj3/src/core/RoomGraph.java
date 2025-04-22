package core;
import java.util.*;

public class RoomGraph<T> {
    private class roomNode {
        int x;
        int y;
        int width;
        int height;
        int id;
        HashMap<Integer, roomNode> edges;

        roomNode(int x, int y, int width, int height, int id) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.id = id;
            this.edges = new HashMap<>();
        }
    }

    private HashMap<Integer, roomNode> rooms;

    public RoomGraph() {
        rooms = new HashMap<>();
    }

    public void addRoom(int x, int y, int width, int height, int id) {
        roomNode newRoom = new roomNode(x, y, width, height, id);
        rooms.put(id, newRoom);
    }

    public void addEdge(int id1, int id2) {
        roomNode room1 = rooms.get(id1);
        roomNode room2 = rooms.get(id2);

        if (room1 != null && room2 != null) {
            room1.edges.put(id2, room2);
            room2.edges.put(id1, room1);
        }
    }

    public double getEdgeWeight(int id1, int id2) {
        roomNode room1 = rooms.get(id1);
        roomNode room2 = rooms.get(id2);

        if (room1 != null && room2 != null) {
            return Math.sqrt(Math.pow(room1.x - room2.x, 2) + Math.pow(room1.y - room2.y, 2));
        }
        return 0;
    }





}
