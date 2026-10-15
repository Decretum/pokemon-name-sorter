import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    // ==========================================
    // File Constants
    // ==========================================
    private static final String POKEMON_FILE = "src/pokemon.txt";
    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";
    private static final String MISSING_FILE = "missing.txt";
    private static final String TRANSIT_FILE = "transit.txt";
    private static final String UNAVAILABLE_FILE = "unavailable.txt";

    // ==========================================
    // Main Entry Point
    // ==========================================
    public static void main(String[] args) {
        // 1. Read all input files
        String[] allPokemon = readPokemon();
        List<String> input = readInput();
        List<String> transit = readTransit();
        List<String> unavailable = readUnavailable();

        // 2. Validate all input files
        validateInput(input, allPokemon);
        validateTransit(transit, allPokemon);
        validateUnavailable(unavailable, allPokemon);
        checkTransitOverlap(input, transit);

        // 3. Calculate and write output files one at a time
        List<String> sorted = writeOutputFile(input, allPokemon);
        writeMissingFile(sorted, transit, unavailable, allPokemon);
    }

    // ==========================================
    // 1. Input File Reading
    // ==========================================

    private static String[] readPokemon() {
        return readFile(POKEMON_FILE).toArray(new String[0]);
    }

    private static List<String> readInput() {
        return readFile(INPUT_FILE);
    }

    private static List<String> readTransit() {
        return readFile(TRANSIT_FILE);
    }

    private static List<String> readUnavailable() {
        return readFile(UNAVAILABLE_FILE);
    }

    private static List<String> readFile(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return Collections.emptyList();
        }
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================================
    // 2. Input File Validation
    // ==========================================

    public static void validateInput(List<String> input) {
        validateInput(input, readPokemon());
    }

    public static void validateInput(List<String> input, String[] allPokemon) {
        validatePokemon(input, allPokemon);
    }

    public static void validateTransit(List<String> transit) {
        validateTransit(transit, readPokemon());
    }

    public static void validateTransit(List<String> transit, String[] allPokemon) {
        validatePokemon(transit, allPokemon);
    }

    public static void validateUnavailable(List<String> unavailable) {
        validateUnavailable(unavailable, readPokemon());
    }

    public static void validateUnavailable(List<String> unavailable, String[] allPokemon) {
        validatePokemon(unavailable, allPokemon);
    }

    public static void validatePokemon(List<String> pokemonList, String[] allPokemon) {
        for (String p : pokemonList) {
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

    // ==========================================
    // 3. Output File Calculation & Writing
    // ==========================================

    private static List<String> writeOutputFile(List<String> input, String[] allPokemon) {
        List<String> sorted = sort(input, allPokemon);
        writeOutput(sorted);
        System.out.println("Total Pokemon: " + sorted.size());
        return sorted;
    }

    private static List<String> writeMissingFile(List<String> present, List<String> inTransit, List<String> unavailable, String[] allPokemon) {
        List<String> missing = findMissing(present, inTransit, unavailable, allPokemon);
        writeMissing(missing);
        return missing;
    }

    private static void writeOutput(List<String> sorted) {
        writeFile(OUTPUT_FILE, sorted);
    }

    private static void writeMissing(List<String> missing) {
        writeFile(MISSING_FILE, missing);
    }

    private static void writeFile(String filePath, List<String> lines) {
        try {
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================================
    // Business Logic / Processing
    // ==========================================

    public static List<String> sort(String unsorted) {
        return sort(Arrays.asList(unsorted.split("\n")));
    }

    public static List<String> sort(List<String> input) {
        return sort(input, readPokemon());
    }

    public static List<String> sort(List<String> input, String[] pokemon) {
        ArrayList<String> sorted = new ArrayList<>();
        for (String entry : input) {
            if (entry != null && !entry.isBlank()) {
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
        return findMissing(present, Collections.emptyList(), Collections.emptyList(), readPokemon());
    }

    public static List<String> findMissing(List<String> present, List<String> inTransit) {
        return findMissing(present, inTransit, Collections.emptyList(), readPokemon());
    }

    public static List<String> findMissing(List<String> present, List<String> inTransit, List<String> unavailable) {
        return findMissing(present, inTransit, unavailable, readPokemon());
    }

    public static List<String> findMissing(List<String> present, String[] allPokemon) {
        return findMissing(present, Collections.emptyList(), Collections.emptyList(), allPokemon);
    }

    public static List<String> findMissing(List<String> present, List<String> inTransit, String[] allPokemon) {
        return findMissing(present, inTransit, Collections.emptyList(), allPokemon);
    }

    public static List<String> findMissing(List<String> present, List<String> inTransit, List<String> unavailable, String[] allPokemon) {
        validateTransit(inTransit, allPokemon);
        validateUnavailable(unavailable, allPokemon);

        Set<String> accountedFor = new HashSet<>();
        accountFor(accountedFor, present);
        accountFor(accountedFor, inTransit);
        accountFor(accountedFor, unavailable);

        List<String> missing = new ArrayList<>();
        for (String p : allPokemon) {
            if (p != null && !p.isBlank() && !accountedFor.contains(p.trim().toLowerCase())) {
                missing.add(p.trim());
            }
        }
        return missing;
    }

    private static void accountFor(Set<String> accountedFor, List<String> pokemonList) {
        for (String p : pokemonList) {
            if (p != null && !p.isBlank()) {
                accountedFor.add(p.trim().toLowerCase());
            }
        }
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
