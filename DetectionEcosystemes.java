import java.awt.image.BufferedImage;

public class DetectionEcosystemes {

    public static int[] detecterEcosystemes(
            BufferedImage imageOriginale,
            int[] clusters,
            String[] etiquettes,
            String biomeRecherche) {

        int largeur = imageOriginale.getWidth();
        int hauteur = imageOriginale.getHeight();

        int[] ecosystemes = new int[largeur * hauteur];

        // -1 signifie : ce pixel n'appartient pas au biome recherche
        for (int i = 0; i < ecosystemes.length; i++) {
            ecosystemes[i] = -1;
        }

        int numeroEcosysteme = 0;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                int index = y * largeur + x;

                if (ecosystemes[index] == -1 && appartientAuBiome(index, clusters, etiquettes, biomeRecherche)) {
                    remplirZone(
                            index,
                            largeur,
                            hauteur,
                            clusters,
                            etiquettes,
                            biomeRecherche,
                            ecosystemes,
                            numeroEcosysteme
                    );

                    numeroEcosysteme++;
                }
            }
        }

        return ecosystemes;
    }

    private static void remplirZone(
            int indexDepart,
            int largeur,
            int hauteur,
            int[] clusters,
            String[] etiquettes,
            String biomeRecherche,
            int[] ecosystemes,
            int numeroEcosysteme) {

        int[] file = new int[largeur * hauteur];

        int debut = 0;
        int fin = 0;

        file[fin] = indexDepart;
        fin++;

        ecosystemes[indexDepart] = numeroEcosysteme;

        while (debut < fin) {

            int indexCourant = file[debut];
            debut++;

            int x = indexCourant % largeur;
            int y = indexCourant / largeur;

            // gauche
            if (x > 0) {
                fin = ajouterVoisin(indexCourant - 1, clusters, etiquettes, biomeRecherche,
                        ecosystemes, numeroEcosysteme, file, fin);
            }

            // droite
            if (x < largeur - 1) {
                fin = ajouterVoisin(indexCourant + 1, clusters, etiquettes, biomeRecherche,
                        ecosystemes, numeroEcosysteme, file, fin);
            }

            // haut
            if (y > 0) {
                fin = ajouterVoisin(indexCourant - largeur, clusters, etiquettes, biomeRecherche,
                        ecosystemes, numeroEcosysteme, file, fin);
            }

            // bas
            if (y < hauteur - 1) {
                fin = ajouterVoisin(indexCourant + largeur, clusters, etiquettes, biomeRecherche,
                        ecosystemes, numeroEcosysteme, file, fin);
            }
        }
    }

    private static int ajouterVoisin(
            int indexVoisin,
            int[] clusters,
            String[] etiquettes,
            String biomeRecherche,
            int[] ecosystemes,
            int numeroEcosysteme,
            int[] file,
            int fin) {

        if (ecosystemes[indexVoisin] == -1 && appartientAuBiome(indexVoisin, clusters, etiquettes, biomeRecherche)) {
            ecosystemes[indexVoisin] = numeroEcosysteme;
            file[fin] = indexVoisin;
            fin++;
        }

        return fin;
    }

    private static boolean appartientAuBiome(
            int index,
            int[] clusters,
            String[] etiquettes,
            String biomeRecherche) {

        int numeroCluster = clusters[index];
        String nomBiome = etiquettes[numeroCluster];

        return nomBiome.equals(biomeRecherche);
    }

    public static int compterEcosystemes(int[] ecosystemes) {
        int max = -1;

        for (int i = 0; i < ecosystemes.length; i++) {
            if (ecosystemes[i] > max) {
                max = ecosystemes[i];
            }
        }

        return max + 1;
    }
}