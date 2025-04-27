package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;

import java.awt.*;

public class MainMenu {

    final String title = "Welcome to the Game!";
    final String newGame = "(N) New Game";
    final String loadGame = "(L) Load Game";
    final String quit = "(Q) Quit";
    Font titleFont = new Font("Arcade Classic", Font.BOLD, 40);
    Font menuFont = new Font("Arcade Classic", Font.PLAIN, 20);



    public  void generateMenu() {
        int xCenter = Main.WIDTH / 2;
        int yCenter = Main.HEIGHT / 2;

        StdDraw.setCanvasSize(Main.WIDTH * 16, Main.HEIGHT * 16);
        StdDraw.setXscale(0, Main.WIDTH);
        StdDraw.setYscale(0, Main.HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(titleFont);
        StdDraw.text(xCenter, yCenter + 6, title);
        StdDraw.setFont(menuFont);
        StdDraw.text(xCenter, yCenter + 4, newGame);
        StdDraw.text(xCenter, yCenter + 2, loadGame);
        StdDraw.text(xCenter, yCenter, quit);
        StdDraw.show();
    }
}