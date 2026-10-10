import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    @Test
    public void name() {
        String input = """
                Onix
                Blacephalon
                Sandshrew
                Sealeo
                Poochyena
                Luxray
                Shelmet
                Koffing
                Rhydon
                Jolteon
                Flareon
                Dratini
                Rattata
                Nidoqueen
                Diglett
                Growlithe
                Machoke
                Dragonair
                Totodile
                Croconaw
                Feraligatr
                Togepi
                """;
        List<String> sorted = Main.sort(input);
        Set<String> unsorted = Arrays.stream(input.split("\n")).filter(s -> !s.isBlank()).collect(Collectors.toSet());

        // After automatic deduping, check that no elements were added or removed
        assertEquals(unsorted.size(), sorted.size());

        // Check that each element is accounted for
        for (String s : sorted) {
            assertTrue(unsorted.contains(s));
        }
        for (String s : unsorted) {
            assertTrue(sorted.contains(s));
        }
    }

    @Test
    public void dedupe() {
        String input = """
                Pikachu
                Onix
                Pikachu
                Blacephalon
                Onix
                Pikachu
                """;
        List<String> sorted = Main.sort(input);

        assertEquals(3, sorted.size());
        assertEquals("Pikachu", sorted.get(0));
        assertEquals("Onix", sorted.get(1));
        assertEquals("Blacephalon", sorted.get(2));
    }

    @Test
    public void dedupeCaseInsensitiveAndWhitespace() {
        String input = """
                pikachu
                Pikachu
                  PIKACHU 
                """;
        List<String> sorted = Main.sort(input);

        assertEquals(1, sorted.size());
    }

    @Test
    public void dedupePrintsNotification() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            String input = """
                    Pikachu
                    Pikachu
                    """;
            Main.sort(input);
            assertTrue(outContent.toString().contains("Duplicates removed: 1"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void noDuplicatesDoesNotPrintNotification() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            String input = """
                    Pikachu
                    Raichu
                    """;
            Main.sort(input);
            assertTrue(outContent.toString().isEmpty());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void findMissingReturnsMissingPokemonInPokedexOrder() {
        String[] allPokemon = new String[]{"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon"};
        List<String> present = List.of("Venusaur", "Bulbasaur");

        List<String> missing = Main.findMissing(present, allPokemon);

        assertEquals(List.of("Ivysaur", "Charmander", "Charmeleon"), missing);
    }

    @Test
    public void findMissingCaseInsensitiveAndWhitespace() {
        String[] allPokemon = new String[]{"Bulbasaur", "Ivysaur", "Venusaur"};
        List<String> present = List.of("  bulbasaur  ", "IVYSAUR");

        List<String> missing = Main.findMissing(present, allPokemon);

        assertEquals(List.of("Venusaur"), missing);
    }

    @Test
    public void findMissingWhenAllPresent() {
        String[] allPokemon = new String[]{"Bulbasaur", "Ivysaur"};
        List<String> present = List.of("Bulbasaur", "Ivysaur");

        List<String> missing = Main.findMissing(present, allPokemon);

        assertTrue(missing.isEmpty());
    }

    @Test
    public void findMissingWhenNonePresent() {
        String[] allPokemon = new String[]{"Bulbasaur", "Ivysaur"};
        List<String> present = Collections.emptyList();

        List<String> missing = Main.findMissing(present, allPokemon);

        assertEquals(List.of("Bulbasaur", "Ivysaur"), missing);
    }

    @Test
    public void findMissingUsingPokemonFile() {
        List<String> missing = Main.findMissing(List.of("Bulbasaur"));
        assertFalse(missing.contains("Bulbasaur"));
        assertTrue(missing.contains("Ivysaur"));
        assertTrue(missing.contains("Butterfree"));
    }
}
