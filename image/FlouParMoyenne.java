package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Classe implémentant un filtre de flou par moyenne simple.
 *
 * Le filtre 3×3 applique une moyenne uniforme sur le pixel central et ses
 * voisins. Les bords de l'image sont recopiés sans modification.
 */
public final class FlouParMoyenne {

    private FlouParMoyenne() {
    }

    /**
     * Applique un flou par moyenne 3×3 à l'image fournie.
     *
     * @param image image d'entrée
     * @return image floutée
     */
    public static BufferedImage appliquer(BufferedImage image) {
        int largeur = image.getWidth();
        int hauteur = image.getHeight();
        BufferedImage resultat = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_3BYTE_BGR);
        // Copier les bords tels quels
        for (int x = 0; x < largeur; x++) {
            resultat.setRGB(x, 0, image.getRGB(x, 0));
            resultat.setRGB(x, hauteur - 1, image.getRGB(x, hauteur - 1));
        }
        for (int y = 0; y < hauteur; y++) {
            resultat.setRGB(0, y, image.getRGB(0, y));
            resultat.setRGB(largeur - 1, y, image.getRGB(largeur - 1, y));
        }
        // Appliquer le noyau moyen 3×3 sur les pixels intérieurs
        for (int y = 1; y < hauteur - 1; y++) {
            for (int x = 1; x < largeur - 1; x++) {
                int sommeR = 0;
                int sommeG = 0;
                int sommeB = 0;
                // Somme des neuf pixels
                for (int j = y - 1; j <= y + 1; j++) {
                    for (int i = x - 1; i <= x + 1; i++) {
                        int couleur = image.getRGB(i, j);
                        int blue = couleur & 0xff;
                        int green = (couleur >> 8) & 0xff;
                        int red = (couleur >> 16) & 0xff;
                        sommeR += red;
                        sommeG += green;
                        sommeB += blue;
                    }
                }
                int moyenneR = sommeR / 9;
                int moyenneG = sommeG / 9;
                int moyenneB = sommeB / 9;
                Color nouvelleCouleur = new Color(moyenneR, moyenneG, moyenneB);
                resultat.setRGB(x, y, nouvelleCouleur.getRGB());
            }
        }
        return resultat;
    }
}