public class NomDesBiomes {

    private Biome[] biomes;

    public NomDesBiomes() {
        biomes = new Biome[] {
            new Biome("Toundra", 71, 70, 61),
            new Biome("Taiga", 43, 50, 35),
            new Biome("Foret temperee", 59, 66, 43),
            new Biome("Foret tropicale", 46, 64, 34),
            new Biome("Savane", 84, 106, 70),
            new Biome("Prairie", 104, 95, 82),
            new Biome("Desert", 152, 140, 120),
            new Biome("Glacier", 200, 200, 200),
            new Biome("Eau peu profonde", 49, 83, 100),
            new Biome("Eau profonde", 12, 31, 47)
        };
    }

    public String[] etiqueterClusters(double[][] centres) {
        String[] etiquettes = new String[centres.length];

        for (int i = 0; i < centres.length; i++) {
            etiquettes[i] = trouverBiomeLePlusProche(centres[i]).getNom();
        }

        return etiquettes;
    }

    private Biome trouverBiomeLePlusProche(double[] centre) {
        Biome meilleurBiome = biomes[0];
        double meilleureDistance = distance(centre, biomes[0].getCouleur());

        for (int i = 1; i < biomes.length; i++) {
            double distance = distance(centre, biomes[i].getCouleur());

            if (distance < meilleureDistance) {
                meilleureDistance = distance;
                meilleurBiome = biomes[i];
            }
        }

        return meilleurBiome;
    }

    private double distance(double[] couleur1, double[] couleur2) {
        double somme = 0;

        for (int i = 0; i < couleur1.length; i++) {
            double difference = couleur1[i] - couleur2[i];
            somme += difference * difference;
        }

        return Math.sqrt(somme);
    }
}