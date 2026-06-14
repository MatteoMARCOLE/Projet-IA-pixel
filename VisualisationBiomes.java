import java.awt.Color;
import java.awt.image.BufferedImage;

public class VisualisationBiomes {

    public static BufferedImage creerImageBiome(
            BufferedImage imageOriginale,
            int[] clusters,
            String[] etiquettes,
            double[][] centres,
            String biomeRecherche) {

        int largeur = imageOriginale.getWidth();
        int hauteur = imageOriginale.getHeight();

        BufferedImage resultat = new BufferedImage(
                largeur,
                hauteur,
                BufferedImage.TYPE_3BYTE_BGR
        );

        int index = 0;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                int numeroCluster = clusters[index];
                String nomBiome = etiquettes[numeroCluster];

                if (nomBiome.equals(biomeRecherche)) {
                    int rouge = (int) Math.round(centres[numeroCluster][0]);
                    int vert = (int) Math.round(centres[numeroCluster][1]);
                    int bleu = (int) Math.round(centres[numeroCluster][2]);

                    rouge = limiter(rouge);
                    vert = limiter(vert);
                    bleu = limiter(bleu);

                    Color couleur = new Color(rouge, vert, bleu);
                    resultat.setRGB(x, y, couleur.getRGB());
                } else {
                    resultat.setRGB(x, y, Color.WHITE.getRGB());
                }

                index++;
            }
        }

        return resultat;
    }

    private static int limiter(int valeur) {
        if (valeur < 0) {
            return 0;
        }

        if (valeur > 255) {
            return 255;
        }

        return valeur;
    }
}