package ecosysteme;

import biome.ResultatBiomes;
import clustering.DBSCAN;
import distance.Distance;
import distance.DistanceEuclidienne;
import java.util.Arrays;

/**
 * Détecte les écosystèmes d'un biome avec DBSCAN.
 *
 * Chaque pixel du biome est décrit uniquement par sa position [x, y].
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

        return lancerDbscanSurPositions(biomes, idBiome, positions, indicesPixels, epsilon, nombrePointsMinimum);
    }

    public static ResultatEcosystemes detecterEcosystemesFusionnes(
            ResultatBiomes biomes,
            String nomBiomeRecherche,
            double epsilon,
            int nombrePointsMinimum) {

        int nombrePixelsBiome = compterPixelsBiomeFusionne(biomes, nomBiomeRecherche);

        int[] affectationsEcosystemes = new int[biomes.largeur * biomes.hauteur];
        Arrays.fill(affectationsEcosystemes, -1);

        if (nombrePixelsBiome == 0) {
            return new ResultatEcosystemes(biomes.largeur, biomes.hauteur, -1, affectationsEcosystemes, 0);
        }

        double[][] positions = new double[nombrePixelsBiome][2];
        int[] indicesPixels = new int[nombrePixelsBiome];
        int indicePosition = 0;

        for (int y = 0; y < biomes.hauteur; y++) {
            for (int x = 0; x < biomes.largeur; x++) {
                int indicePixel = y * biomes.largeur + x;
                int cluster = biomes.affectations[indicePixel];

                if (appartientAuBiomeFusionne(biomes, cluster, nomBiomeRecherche)) {
                    positions[indicePosition][0] = x;
                    positions[indicePosition][1] = y;
                    indicesPixels[indicePosition] = indicePixel;
                    indicePosition++;
                }
            }
        }

        return lancerDbscanSurPositions(biomes, -1, positions, indicesPixels, epsilon, nombrePointsMinimum);
    }

    private static ResultatEcosystemes lancerDbscanSurPositions(
            ResultatBiomes biomes,
            int idBiome,
            double[][] positions,
            int[] indicesPixels,
            double epsilon,
            int nombrePointsMinimum) {

        int[] affectationsEcosystemes = new int[biomes.largeur * biomes.hauteur];
        Arrays.fill(affectationsEcosystemes, -1);

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

    private static int compterPixelsBiomeFusionne(ResultatBiomes biomes, String nomBiomeRecherche) {
        int compteur = 0;

        for (int i = 0; i < biomes.affectations.length; i++) {
            int cluster = biomes.affectations[i];

            if (appartientAuBiomeFusionne(biomes, cluster, nomBiomeRecherche)) {
                compteur++;
            }
        }

        return compteur;
    }

    private static boolean appartientAuBiomeFusionne(ResultatBiomes biomes, int cluster, String nomBiomeRecherche) {
        if (cluster < 0 || biomes.estClusterIgnore(cluster)) {
            return false;
        }

        return biomes.nomsClusters[cluster].trim().equalsIgnoreCase(nomBiomeRecherche.trim());
    }
}