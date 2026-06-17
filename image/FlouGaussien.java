package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Classe implémentant un flou gaussien 3×3 classique.
 *
 * Le noyau utilisé est :
 * <pre>
 * 1 2 1
 * 2 4 2
 * 1 2 1
 * </pre>
 * Le diviseur est 16. Les bords sont recopiés sans modification.
 */
public final class FlouGaussien {

    private FlouGaussien() {
    }

    /**
     * Applique un flou gaussien 3×3 à l'image fournie.
     *
     * @param image image source
     * @return image floutée
     */
    public static BufferedImage appliquer(BufferedImage image) {
        int largeur = image.getWidth();
        int hauteur = image.getHeight();
        BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_3BYTE_BGR);
        int[][] noyau = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
        };
        int diviseur = 16;
        // Copier les bords tels quels
        for (int x = 0; x < largeur; x++) {
            resultat.setRGB(x, 0, image.getRGB(x, 0));
            resultat.setRGB(x, hauteur - 1, image.getRGB(x, hauteur - 1));
        }
        for (int y = 0; y < hauteur; y++) {
            resultat.setRGB(0, y, image.getRGB(0, y));
            resultat.setRGB(largeur - 1, y, image.getRGB(largeur - 1, y));
        }
        // Appliquer le noyau sur les pixels intérieurs
        for (int y = 1; y < hauteur - 1; y++) {
            for (int x = 1; x < largeur - 1; x++) {
                int sommeR = 0;
                int sommeG = 0;
                int sommeB = 0;
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int couleur = image.getRGB(x + i, y + j);
                        int blue = couleur & 0xff;
                        int green = (couleur >> 8) & 0xff;
                        int red = (couleur >> 16) & 0xff;
                        int coeff = noyau[j + 1][i + 1];
                        sommeR += red * coeff;
                        sommeG += green * coeff;
                        sommeB += blue * coeff;
                    }
                }
                int moyenneR = sommeR / diviseur;
                int moyenneG = sommeG / diviseur;
                int moyenneB = sommeB / diviseur;
                Color nouvelleCouleur = new Color(moyenneR, moyenneG, moyenneB);
                resultat.setRGB(x, y, nouvelleCouleur.getRGB());
            }
        }
        return resultat;
    }
}