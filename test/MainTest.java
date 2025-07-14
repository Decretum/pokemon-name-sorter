import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
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
        Set<String> sorted = new HashSet<>(Main.sort(input));
        Set<String> unsorted = Arrays.stream(input.split("\n")).filter(s -> !s.isBlank()).collect(Collectors.toSet());

        // After set deduping, check that no elements were added ore removed
        assertEquals(sorted.size(), unsorted.size());

        // Check that each element is accounted for
        for (String s : sorted) {
            assertTrue(unsorted.contains(s));
        }
        for (String s : unsorted) {
            assertTrue(sorted.contains(s));
        }
    }


}