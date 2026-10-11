import java.util.ArrayList;
import java.util.Collections;
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
    private static final String TRANSIT_FILE = "transit.txt";

    public static void main(String[] args) {
        List<String> sorted = sort(readInput());

        writeOutput(sorted);

        List<String> transit = readTransit();
        validateTransit(transit);
        checkTransitOverlap(sorted, transit);

        List<String> missing = findMissing(sorted, transit);
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

    private static List<String> readTransit() {
        java.nio.file.Path path = java.nio.file.Paths.get(TRANSIT_FILE);
        if (!java.nio.file.Files.exists(path)) {
            return Collections.emptyList();
        }
        try {
            return java.nio.file.Files.readAllLines(path);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void validateTransit(List<String> transit) {
        validateTransit(transit, readPokemon());
    }

    public static void validateTransit(List<String> transit, String[] allPokemon) {
        for (String p : transit) {
            if (p != null && !p.isBlank()) {
                getDexNumber(p, allPokemon);
            }
        }
    }

    public static List<String> checkTransitOverlap(List<String> inputList, List<String> transitList) {
        Set<String> inputSet = new HashSet<>();
        for (String p : inputList) {
            if (p != null && !p.isBlank()) {
                inputSet.add(p.trim().toLowerCase());
            }
        }

        List<String> overlapping = new ArrayList<>();
        Set<String> seenOverlapping = new HashSet<>();
        for (String p : transitList) {
            if (p != null && !p.isBlank()) {
                String normalized = p.trim().toLowerCase();
                if (inputSet.contains(normalized) && seenOverlapping.add(normalized)) {
                    overlapping.add(p.trim());
                }
            }
        }

        if (!overlapping.isEmpty()) {
            System.out.println("Warning: Pokemon present in both transit.txt and input.txt: " + String.join(", ", overlapping));
        }

        return overlapping;
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
        return findMissing(present, Collections.emptyList(), readPokemon());
    }

    public static List<String> findMissing(List<String> present, List<String> inTransit) {
        return findMissing(present, inTransit, readPokemon());
    }

    public static List<String> findMissing(List<String> present, String[] allPokemon) {
        return findMissing(present, Collections.emptyList(), allPokemon);
    }

    public static List<String> findMissing(List<String> present, List<String> inTransit, String[] allPokemon) {
        validateTransit(inTransit, allPokemon);

        Set<String> accountedFor = new HashSet<>();
        for (String p : present) {
            if (p != null && !p.isBlank()) {
                accountedFor.add(p.trim().toLowerCase());
            }
        }
        for (String p : inTransit) {
            if (p != null && !p.isBlank()) {
                accountedFor.add(p.trim().toLowerCase());
            }
        }

        List<String> missing = new ArrayList<>();
        for (String p : allPokemon) {
            if (p != null && !p.isBlank() && !accountedFor.contains(p.trim().toLowerCase())) {
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
