package core;
import java.util.*;
public class Metadata {
    private long seed;
    private ArrayList<Character> inputs;
    private String name;

    public Metadata(long seed, String name){
        this.seed = seed;
        this.name = name;
        this.inputs = new ArrayList<>();
    }

    public long getSeed() {
        return seed;
    }

    public String getName() {
        return name;
    }

    public void addInput(char input) {
        input = Character.toLowerCase(input);
        if (input == 'w' || input == 'a' || input == 's' || input == 'd') {
            inputs.add(input);
            System.out.println("Input added: " + input);
        }

    }

    public ArrayList<Character> getInputs() {
        return inputs;
    }
}
