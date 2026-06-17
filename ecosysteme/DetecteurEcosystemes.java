package ecosysteme;

import java.util.Arrays;

import biome.ResultatBiomes;
import clustering.DBSCAN;
import distance.Distance;
import distance.DistanceEuclidienne;

/**
 * Détecte les écosystèmes d'un biome avec DBSCAN.
 *
 * Chaque pixel du biome est décrit uniquement par sa position [x, y], comme
 * demandé dans le sujet.
 */
public final class DetecteurEcosystemes {

    private DetecteurEcosystemes() {
    }

    public static ResultatEcosystemes detecterEcosystemes(ResultatBiomes biomes, int idBiome, double epsilon, int nombrePointsMinimum) {
        int nombrePixelsBiome = compterPixelsBiome(biomes.affectations, idBiome);

        int[] affectationsEcosystemes = new int[biomes.largeur * biomes.hauteur];
        Arrays.fill(affectationsEcosystemes, -1);

        if (nombrePixelsBiome == 0) {
            return new ResultatEcosystemes(biomes.largeur, biomes.hauteur, idBiome, affectationsEcosystemes, 0);
        }

        double[][] positions = new double[nombrePixelsBiome][2];
        int[] indicesPixels = new int[nombrePixelsBiome];
        int indicePosition = 0;

        for (int y = 0; y < biomes.hauteur; y++) {
            for (int x = 0; x < biomes.largeur; x++) {
                int indicePixel = y * biomes.largeur + x;

                if (biomes.affectations[indicePixel] == idBiome) {
                    positions[indicePosition][0] = x;
                    positions[indicePosition][1] = y;
                    indicesPixels[indicePosition] = indicePixel;
                    indicePosition++;
                }
            }
        }

        Distance distance = new DistanceEuclidienne();
        DBSCAN dbscan = new DBSCAN(epsilon, nombrePointsMinimum, distance);

        int[] affectationsLocales = dbscan.cluster(positions);

        int numeroMaximum = -1;

        for (int i = 0; i < affectationsLocales.length; i++) {
            int indicePixel = indicesPixels[i];
            int numeroEcosysteme = affectationsLocales[i];

            affectationsEcosystemes[indicePixel] = numeroEcosysteme;

            if (numeroEcosysteme > numeroMaximum) {
                numeroMaximum = numeroEcosysteme;
            }
        }

        int nombreEcosystemes = numeroMaximum + 1;

        return new ResultatEcosystemes(biomes.largeur, biomes.hauteur, idBiome, affectationsEcosystemes, nombreEcosystemes);
    }

    private static int compterPixelsBiome(int[] affectationsBiomes, int idBiome) {
        int compteur = 0;

        for (int affectation : affectationsBiomes) {
            if (affectation == idBiome) {
                compteur++;
            }
        }

        return compteur;
    }
}
