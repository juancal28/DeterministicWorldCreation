package core;
import java.util.*;
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

        while (true){
            while (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'n' || key == 'N') {
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.setFont(menuFont);
                    StdDraw.text(xCenter, yCenter + 6, "Enter 'G' to start a new game or a seed and then 'G' to start a game");
                    StdDraw.show();
                    StringBuilder seedBuilder = new StringBuilder();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char input = StdDraw.nextKeyTyped();
                            if (input == 'g' || input == 'G') {
                                TETile[][] newWorld = new TETile[Main.WIDTH][Main.HEIGHT];
                                World.makeNewWorld(newWorld);
                                Main.worlds.add(newWorld);
                                return;
                            } else if (Character.isDigit(input)) {
                                seedBuilder.append(input);
                                // Update displayed seed
                                StdDraw.clear(StdDraw.BLACK);
                                StdDraw.setPenColor(StdDraw.WHITE);
                                StdDraw.setFont(menuFont);
                                StdDraw.text(xCenter, yCenter + 6, "Enter 'G' to start a new game or a seed and then 'G' to start a game");
                                StdDraw.text(xCenter, yCenter + 4, "Current Seed: " + seedBuilder.toString());
                                StdDraw.show();

                                // Convert seed string to long
                                long seed = Long.parseLong(seedBuilder.toString());
                                World.changeSeed(seed);
                            }
                        }
                    }

                } else if (key == 'l' || key == 'L') {
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.setFont(menuFont);
                    StdDraw.text(xCenter, yCenter + 6, "click 'l' again to open most recent game or choose a game");
                    // show worlds fromthe main.worlds array
                    for (int i = 0; i < Main.worlds.size(); i++) {
                        StdDraw.text(xCenter, yCenter + 4 - i, "Game " + i);
                    }
                    StdDraw.show();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char input = StdDraw.nextKeyTyped();
                            if (input == 'l' || input == 'L') {
                                Saving.loadGame();
                                return;
                            } else if (Character.isDigit(input)) {
                                int index = Character.getNumericValue(input);
                                if (index >= 0 && index < Main.worlds.size()) {
                                    //loadGame();
                                    return;
                                }
                            }
                        }
                    }
                    //break;
                } else if (key == 'q' || key == 'Q') {
                    System.exit(0);
                }
            }
        }
    }

    public void loadGame(Metadata info) {
        TETile[][] newWorld = new TETile[Main.WIDTH][Main.HEIGHT];
        World.changeSeed(info.getSeed());
        World.makeNewWorld(newWorld);
        Main.worlds.add(newWorld);
        Main.worldIndex++;

        for (char input : info.getInputs()) {
            Avatar.moveAvatar(input, newWorld);
        }
    }
}