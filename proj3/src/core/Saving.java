package core;

import tileengine.TETile;

import java.io.*;
import java.util.*;

public class Saving {
    private static final String SAVE_PREFIX = "save";
    private static final String EXTENSION = ".txt";

    public static void saveGame(Metadata gameData) {
        String fileName = SAVE_PREFIX + Main.worldIndex + EXTENSION;
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            // Save seed
            writer.println("SEED:" + gameData.getSeed());
            // Save name
            writer.println("NAME:" + gameData.getName());
            // Save movements
            writer.print("MOVEMENTS:");
            ArrayList<Character> inputs = gameData.getInputs();
            for (Character input : inputs) {
                writer.print(input);
            }
            writer.println();
            System.out.println("Game saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static void loadGame() {
        String fileName = SAVE_PREFIX + Main.worldIndex + EXTENSION;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            long seed = 0;
            String name = "";
            String movements = "";

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("SEED:")) {
                    seed = Long.parseLong(line.substring(5));
                } else if (line.startsWith("NAME:")) {
                    name = line.substring(5);
                } else if (line.startsWith("MOVEMENTS:")) {
                    movements = line.substring(10);
                }
            }

            // Create a new world with the saved seed
            World.changeSeed(seed);
            TETile[][] world = new TETile[Main.WIDTH][Main.HEIGHT];
            World.makeNewWorld(world);
            Main.worlds.set(Main.worldIndex, world);

            // Replay the movements
            for (char c : movements.toCharArray()) {
                Avatar.moveAvatar(c, world);
            }

            System.out.println("Game loaded successfully from " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("No save file found: " + e.getMessage());
        }
    }
}