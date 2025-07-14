import java.util.ArrayList;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static String INPUT = """
Bulbasaur
Ivysaur
Venusaur
Charmander
Charizard
Squirtle
Blastoise
Rattata
Pikachu
Sandshrew
Nidoran F
Nidoqueen
Nidoran M
Diglett
Psyduck
Golduck
Growlithe
Arcanine
Poliwrath
Alakazam
Machop
Machoke
Ponyta
Magnemite
Magneton
Shellder
Cloyster
Gengar
Onix
Exeggcute
Koffing
Rhyhorn
Rhydon
Chansey
Kangaskhan
Pinsir
Magikarp
Gyarados
Eevee
Jolteon
Flareon
Aerodactyl
Snorlax
Articuno
Zapdos
Moltres
Dratini
Dragonair
Mewtwo
Mew
Totodile
Croconaw
Feraligatr
Hoothoot
Crobat
Togepi
Ampharos
Wooper
Slowking
Unown
Scizor
Sneasel
Delibird
Skarmory
Porygon2
Smeargle
Blissey
Raikou
Entei
Suicune
Lugia
Ho-oh
Torchic
Blaziken
Poochyena
Linoone
Shiftry
Ralts
Gardevoir
Surskit
Shedinja
Mawile
Aggron
Roselia
Spoink
Trapinch
Barboach
Dusclops
Absol
Glalie
Sealeo
Clamperl
Bagon
Salamence
Beldum
Metagross
Regice
Registeel
Latias
Latios
Groudon
Rayquaza
Jirachi
Deoxys
Chimchar
Monferno
Kricketot
Kricketune
Luxray
Combee
Floatzel
Ambipom
Bonsly
Lucario
Drapion
Magnezone
Mamoswine
Gallade
Froslass
Rotom
Azelf
Dialga
Palkia
Heatran
Giratina
Cresselia
Manaphy
Tepig
Emboar
Oshawott
Blitzle
Gigalith
Oddish
Audino
Conkeldurr
Lilligant
Vanillite
Cubchoo
Shelmet
Hydreigon
Reshiram
Zekrom
Kyurem
Meloetta
Greninja
Talonflame
Gogoat
Aegislash
Avalugg
Xerneas
Zygarde
Rockruff
Tsareena
Silvally
Mimikyu
Kommo-o
Tapu Lele
Tapu Bulu
Celesteela
Blacephalon
Meltan
Corviknight
Applin
Toxel
Hatterene
Duraludon
Enamorus
Lechonk
Oinkologne
Nymble
Revavroom
Orthworm
Dondozo
Scream Tail
Flutter Mane
Dipplin
            """;

    public static void main(String[] args) {
        List<String> sorted = sort(INPUT);

        for (String s : sorted) {
            System.out.println(s);
        }

        System.out.println("Total Pokemon: " + sorted.size());
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