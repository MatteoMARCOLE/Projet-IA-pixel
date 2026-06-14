import java.awt.image.BufferedImage;

public class ImageVersDonnees {

    public static double[][] convertirImageEnDonneesRGB(BufferedImage image) {

        int largeur = image.getWidth();
        int hauteur = image.getHeight();

        double[][] donnees = new double[largeur * hauteur][3];

        int index = 0;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                int couleur = image.getRGB(x, y);

                int blue = couleur & 0xff;
                int green = (couleur & 0xff00) >> 8;
                int red = (couleur & 0xff0000) >> 16;

                donnees[index][0] = red;
                donnees[index][1] = green;
                donnees[index][2] = blue;

                index++;
            }
        }

        return donnees;
    }
}