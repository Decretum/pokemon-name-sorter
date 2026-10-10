import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static final String POKEMON_FILE = "src/pokemon.txt";
    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";
    private static final String MISSING_FILE = "missing.txt";

    public static void main(String[] args) {
        List<String> sorted = sort(readInput());

        writeOutput(sorted);

        List<String> missing = findMissing(sorted);
        writeMissing(missing);

        System.out.println("Total Pokemon: " + sorted.size());
    }

    private static void writeOutput(List<String> sorted) {
        writeFile(OUTPUT_FILE, sorted);
    }

    private static void writeMissing(List<String> missing) {
        writeFile(MISSING_FILE, missing);
    }

    private static void writeFile(String filePath, List<String> lines) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), lines);
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
        String[] pokemon = readPokemon();
        String[] input = unsorted.split("\n");

        ArrayList<String> sorted = new ArrayList<>();
        for (String entry : input) {
            if (!entry.isBlank()) {
                int dexNumber = getDexNumber(entry, pokemon);

                boolean added = false;
                for (int x = 0; x < sorted.size(); x++) {
                    if (dexNumber < getDexNumber(sorted.get(x), pokemon)) {
                        sorted.add(x, entry);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    sorted.add(entry);
                }
            }
        }

        ArrayList<String> deduped = new ArrayList<>();
        int duplicatesRemoved = 0;
        for (String entry : sorted) {
            if (!deduped.isEmpty() && deduped.get(deduped.size() - 1).trim().equalsIgnoreCase(entry.trim())) {
                duplicatesRemoved++;
            } else {
                deduped.add(entry);
            }
        }

        if (duplicatesRemoved > 0) {
            System.out.println("Duplicates removed: " + duplicatesRemoved);
        }

        return deduped;
    }

    public static List<String> findMissing(List<String> present) {
        return findMissing(present, readPokemon());
    }

    public static List<String> findMissing(List<String> present, String[] allPokemon) {
        Set<String> presentSet = new HashSet<>();
        for (String p : present) {
            if (p != null && !p.isBlank()) {
                presentSet.add(p.trim().toLowerCase());
            }
        }

        List<String> missing = new ArrayList<>();
        for (String p : allPokemon) {
            if (p != null && !p.isBlank() && !presentSet.contains(p.trim().toLowerCase())) {
                missing.add(p.trim());
            }
        }
        return missing;
    }

    private static int getDexNumber(String name, String[] pokemon) {
        for (int x = 1; x <= pokemon.length; x++) {
            if (name.trim().equalsIgnoreCase(pokemon[x - 1].trim())) {
                return x;
            }
        }
        throw new RuntimeException("Unknown Pokemon: " + name);
    }

    private static String[] readPokemon() {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(POKEMON_FILE))).split("\n");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
