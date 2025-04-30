package core;

import tileengine.TETile;

import java.io.*;
import java.util.*;

public class Saving {
    private static final String SAVE_PREFIX = "src/saves/save";
    private static final String EXTENSION = ".txt";

    public static void saveGame(Metadata gameData) {
        saveGameIndex();
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
            writer.print("Game Index: " + Main.worldIndex);
            writer.println();
            System.out.println("Game saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static void saveGameIndex(){
        String fileName = "src/saves/counter.txt";
        Main.updateWorldIndex(Main.worldIndex + 1);
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            writer.print(Main.worldIndex);
        } catch (IOException e) {
            System.out.println("Error saving game index: " + e.getMessage());
        }
    }

    public static int loadGameIndex() {
        String filename = "src/saves/counter.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line != null) {
                System.out.println("Game index loaded: " + line);
                return Integer.parseInt(line.trim());
            } else {
                return 0; // Default to 0 if the file is empty
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading game index: " + e.getMessage());
            return 0; // Default to 0 if there's an error
        }
    }

    public static void loadGame(int index) {
        String fileName = SAVE_PREFIX + index + EXTENSION;
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
            Main.worlds.set(index, world);

            // Replay the movements
            for (char c : movements.toCharArray()) {
                Avatar.moveAvatar(c, world);
            }

            System.out.println("Game loaded successfully from " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("No save file found: " + e.getMessage());
        }
    }

    public static void loadGame() {
        // Load the most recent game using the current worldIndex
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
            Main.worlds.add(world);

            // Create metadata object with the loaded information
            Metadata gameData = new Metadata(seed, name);

            // Replay the movements
            for (char c : movements.toCharArray()) {
                Avatar.moveAvatar(c, world);
                gameData.addInput(c);
            }

            // Set the current game data in Main
            Main.gameData = gameData;
            System.out.println("Game loaded successfully from " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("No save file found: " + e.getMessage());
        }
    }
}