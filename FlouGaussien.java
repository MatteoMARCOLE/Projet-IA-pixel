import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FlouGaussien {

    public static void main(String[] args) throws IOException {

        File fichier = new File("Planete1.jpg");
        BufferedImage image = ImageIO.read(fichier);

        BufferedImage resultat = appliquerFlouGaussien(image);

        ImageIO.write(resultat, "PNG", new File("planete_flou_gaussien.png"));

        BufferedImage resultat2 = appliquerFlouGaussien5x5(image);

        ImageIO.write(resultat2, "PNG", new File("planete_flou_gaussien_5x5.png"));

        BufferedImage resultat3 = appliquerFlouGaussien5x5(image);

        ImageIO.write(resultat3, "PNG", new File("planete_flou_gaussien_7x7.png"));
    }

    public static BufferedImage appliquerFlouGaussien(BufferedImage image) {

        BufferedImage resultat = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_3BYTE_BGR
        );

        int[][] filtre = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
        };

        int diviseur = 16;

        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {

                int sommeR = 0;
                int sommeG = 0;
                int sommeB = 0;

                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {

                        int couleur = image.getRGB(i, j);

                        int blue = couleur & 0xff;
                        int green = (couleur & 0xff00) >> 8;
                        int red = (couleur & 0xff0000) >> 16;

                        int coefficient = filtre[i - (x - 1)][j - (y - 1)];

                        sommeR += red * coefficient;
                        sommeG += green * coefficient;
                        sommeB += blue * coefficient;   
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

    public static BufferedImage appliquerFlouGaussien5x5(BufferedImage image) {

        BufferedImage resultat = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_3BYTE_BGR
        );

        int[][] filtre = {
            {1, 4, 7, 4, 1},
            {4, 16, 26, 16, 4},
            {7, 26, 41, 26, 7},
            {4, 16, 26, 16, 4},
            {1, 4, 7, 4, 1}
        };

        int diviseur = 273;
        int rayon = 2;

        for (int x = rayon; x < image.getWidth() - rayon; x++) {
            for (int y = rayon; y < image.getHeight() - rayon; y++) {

                int sommeR = 0;
                int sommeG = 0;
                int sommeB = 0;

                for (int i = x - rayon; i <= x + rayon; i++) {
                    for (int j = y - rayon; j <= y + rayon; j++) {

                        int couleur = image.getRGB(i, j);

                        int blue = couleur & 0xff;
                        int green = (couleur & 0xff00) >> 8;
                        int red = (couleur & 0xff0000) >> 16;

                        int indiceFiltreX = i - (x - rayon);
                        int indiceFiltreY = j - (y - rayon);

                        int coefficient = filtre[indiceFiltreX][indiceFiltreY];

                        sommeR += red * coefficient;
                        sommeG += green * coefficient;
                        sommeB += blue * coefficient;
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

    public static BufferedImage appliquerFlouGaussien7x7(BufferedImage image) {

        BufferedImage resultat = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_3BYTE_BGR
        );

        int[][] filtre = {
            {0, 0, 1, 2, 1, 0, 0},
            {0, 3, 13, 22, 13, 3, 0},
            {1, 13, 59, 97, 59, 13, 1},
            {2, 22, 97, 159, 97, 22, 2},
            {1, 13, 59, 97, 59, 13, 1},
            {0, 3, 13, 22, 13, 3, 0},
            {0, 0, 1, 2, 1, 0, 0}
        };

        int diviseur = 1003;
        int rayon = 3;

        for (int x = rayon; x < image.getWidth() - rayon; x++) {
            for (int y = rayon; y < image.getHeight() - rayon; y++) {

                int sommeR = 0;
                int sommeG = 0;
                int sommeB = 0;

                for (int i = x - rayon; i <= x + rayon; i++) {
                    for (int j = y - rayon; j <= y + rayon; j++) {

                        int couleur = image.getRGB(i, j);

                        int blue = couleur & 0xff;
                        int green = (couleur & 0xff00) >> 8;
                        int red = (couleur & 0xff0000) >> 16;

                        int indiceFiltreX = i - (x - rayon);
                        int indiceFiltreY = j - (y - rayon);

                        int coefficient = filtre[indiceFiltreX][indiceFiltreY];

                        sommeR += red * coefficient;
                        sommeG += green * coefficient;
                        sommeB += blue * coefficient;
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