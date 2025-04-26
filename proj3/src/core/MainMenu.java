package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

public class MainMenu {

    TERenderer ter;

    public MainMenu(TERenderer ter) {
        // Constructor logic if needed
        this.ter = ter;
    }

    public MainMenu() {
        // Default constructor
        this.ter = new TERenderer();
    }

    public  void generateMenu() {
        int xCenter = Main.WIDTH / 2;
        int yCenter = Main.HEIGHT / 2;


        String title = "Welcome to the Game!";


        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setCanvasSize(Main.WIDTH * 16, Main.HEIGHT * 16);
        StdDraw.setXscale(0, Main.WIDTH);
        StdDraw.setYscale(0, Main.HEIGHT);
        StdDraw.text(xCenter, yCenter + 2, title);
        StdDraw.show();
    }
}