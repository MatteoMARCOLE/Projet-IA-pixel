import java.awt.Color;
import java.awt.image.BufferedImage;

public class VisualisationEcosystemes {

    public static BufferedImage creerImageEcosystemes(
            BufferedImage imageOriginale,
            int[] ecosystemes) {

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

                int numeroEcosysteme = ecosystemes[index];

                if (numeroEcosysteme == -1) {
                    resultat.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    Color couleur = couleurPourEcosysteme(numeroEcosysteme);
                    resultat.setRGB(x, y, couleur.getRGB());
                }

                index++;
            }
        }

        return resultat;
    }

    private static Color couleurPourEcosysteme(int numero) {
        int rouge = (numero * 70 + 80) % 256;
        int vert = (numero * 130 + 120) % 256;
        int bleu = (numero * 200 + 160) % 256;

        return new Color(rouge, vert, bleu);
    }
}