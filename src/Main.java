import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        List<String> sorted = sort(readInput());

        writeOutput(sorted);

        System.out.println("Total Pokemon: " + sorted.size());
    }

    private static void writeOutput(List<String> sorted) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(OUTPUT_FILE), sorted);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readInput() {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(INPUT_FILE)));
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> sort(String unsorted) {
        String[] pokemon = AllPokemon.POKEMON.split("\n");
        String[] input = unsorted.split("\n");

        ArrayList<String> sorted = new ArrayList<>();
        for (String entry : input) {
            if (!entry.isBlank()) {
                int dexNumber = getDexNumber(entry, pokemon);

                boolean added = false;
                for (int x = 0; x < sorted.size(); x++) {
                    if (!added) {
                        if (dexNumber < getDexNumber(sorted.get(x), pokemon)) {
                            sorted.add(x, entry);
                            added = true;
                        }
                    }
                }
                if (!added) {
                    sorted.add(entry);
                }
            }
        }

        return sorted;
    }

    private static int getDexNumber(String name, String[] pokemon) {
        for (int x = 1; x <= pokemon.length; x++) {
            if (name.trim().equalsIgnoreCase(pokemon[x - 1].trim())) {
                return x;
            }
        }
        throw new RuntimeException("Unknown Pokemon: " + name);
    }
}