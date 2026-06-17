package utilitaire;

import java.util.List;

import biome.Biome;

/**
 * Méthodes simples utilisées dans plusieurs parties du projet.
 */
public final class Methode {

    private Methode() {
        // Classe utilitaire.
    }

    /**
     * Calcule la distance euclidienne entre deux couleurs RGB.
     */
    public static double calculerDistanceRGB(int rouge1, int vert1, int bleu1, int rouge2, int vert2, int bleu2) {
        int differenceRouge = rouge1 - rouge2;
        int differenceVert = vert1 - vert2;
        int differenceBleu = bleu1 - bleu2;

        return Math.sqrt(differenceRouge * differenceRouge + differenceVert * differenceVert + differenceBleu * differenceBleu);
    }

    /**
     * Trouve le biome de référence dont la couleur est la plus proche.
     */
    public static String trouverBiomeLePlusProche(int rougeCluster, int vertCluster, int bleuCluster, List<Biome> references) {
        String nomBiome = "Inconnu";
        double distanceMinimale = Double.POSITIVE_INFINITY;

        for (Biome reference : references) {
            double distance = calculerDistanceRGB(rougeCluster, vertCluster, bleuCluster, reference.rouge, reference.vert, reference.bleu);

            if (distance < distanceMinimale) {
                distanceMinimale = distance;
                nomBiome = reference.nom;
            }
        }

        return nomBiome;
    }

    /**
     * Éclaircit une composante de couleur.
     */
    public static int eclaircirCanal(int valeurAncienne, double pourcentage) {
        return (int) Math.round(valeurAncienne + (pourcentage / 100.0) * (255 - valeurAncienne));
    }

    /**
     * Éclaircit un pixel RGB.
     */
    public static int[] eclaircirPixel(int[] pixelRGB, double pourcentage) {
        return new int[] {
            eclaircirCanal(pixelRGB[0], pourcentage),
            eclaircirCanal(pixelRGB[1], pourcentage),
            eclaircirCanal(pixelRGB[2], pourcentage)
        };
    }
}
