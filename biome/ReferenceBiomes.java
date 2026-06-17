package biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contient les couleurs de référence fournies dans le sujet.
 */
public final class ReferenceBiomes {

    private static final List<Biome> BIOMES;

    static {
        List<Biome> biomes = new ArrayList<>();

        biomes.add(new Biome("Toundra", 71, 70, 61));
        biomes.add(new Biome("Taïga", 43, 50, 35));
        biomes.add(new Biome("Forêt tempérée", 59, 66, 43));
        biomes.add(new Biome("Forêt tropicale", 46, 64, 34));
        biomes.add(new Biome("Savane", 84, 106, 70));
        biomes.add(new Biome("Prairie", 104, 95, 82));
        biomes.add(new Biome("Désert", 152, 140, 120));
        biomes.add(new Biome("Glacier", 200, 200, 200));
        biomes.add(new Biome("Eau peu profonde", 49, 83, 100));
        biomes.add(new Biome("Eau profonde", 12, 31, 47));

        BIOMES = Collections.unmodifiableList(biomes);
    }

    private ReferenceBiomes() {
    }

    public static List<Biome> obtenirBiomes() {
        return BIOMES;
    }
}
