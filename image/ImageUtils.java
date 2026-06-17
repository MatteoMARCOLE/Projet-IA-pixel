package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import biome.ResultatBiomes;
import ecosysteme.ResultatEcosystemes;
import utilitaire.Methode;

/**
 * Méthodes de chargement, d'enregistrement et de création des images résultat.
 */
public final class ImageUtils {

    private ImageUtils() {
    }

    public static BufferedImage chargerImage(String chemin) throws IOException {
        BufferedImage image = ImageIO.read(new File(chemin));

        if (image == null) {
            throw new IOException("Le fichier n'est pas une image valide.");
        }

        return image;
    }

    public static void enregistrerImage(BufferedImage image, String chemin) throws IOException {
        ImageIO.write(image, "png", new File(chemin));
    }

    /**
     * Transforme tous les pixels en descriptions [rouge, vert, bleu].
     */
    public static double[][] extraireRGB(BufferedImage image) {
        int largeur = image.getWidth();
        int hauteur = image.getHeight();
        double[][] descriptions = new double[largeur * hauteur][3];

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int indice = y * largeur + x;
                Color couleur = new Color(image.getRGB(x, y));

                descriptions[indice][0] = couleur.getRed();
                descriptions[indice][1] = couleur.getGreen();
                descriptions[indice][2] = couleur.getBlue();
            }
        }

        return descriptions;
    }

    /**
     * Crée l'image où chaque biome utilise la couleur moyenne de son cluster.
     */
    public static BufferedImage creerImageBiomes(ResultatBiomes biomes) {
        BufferedImage resultat = new BufferedImage(biomes.largeur, biomes.hauteur, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < biomes.hauteur; y++) {
            for (int x = 0; x < biomes.largeur; x++) {
                int indice = y * biomes.largeur + x;
                int cluster = biomes.affectations[indice];
                int rouge;
                int vert;
                int bleu;

                if (cluster < 0) {
                    /*
                     * Le contour de la carte est conservé visuellement, mais il ne possède aucun numéro de biome.
                     */
                    rouge = (int) biomes.descriptionsRGB[indice][0];
                    vert = (int) biomes.descriptionsRGB[indice][1];
                    bleu = (int) biomes.descriptionsRGB[indice][2];
                } else {
                    double[] couleurMoyenne
                        = biomes.couleursCentroides[cluster];

                    rouge = limiterCouleur((int) Math.round(couleurMoyenne[0]));
                    vert = limiterCouleur((int) Math.round(couleurMoyenne[1]));
                    bleu = limiterCouleur((int) Math.round(couleurMoyenne[2]));
                }

                resultat.setRGB(x, y, new Color(rouge, vert, bleu).getRGB());
            }
        }

        return resultat;
    }

    /**
     * Met un seul biome en avant et éclaircit le reste de l'image.
     */
    public static BufferedImage mettreEnAvantBiome(ResultatBiomes biomes, int idBiome, double pourcentageEclaircissement) {
        BufferedImage resultat = new BufferedImage(biomes.largeur, biomes.hauteur, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < biomes.hauteur; y++) {
            for (int x = 0; x < biomes.largeur; x++) {
                int indice = y * biomes.largeur + x;

                int rouge = (int) biomes.descriptionsRGB[indice][0];
                int vert = (int) biomes.descriptionsRGB[indice][1];
                int bleu = (int) biomes.descriptionsRGB[indice][2];

                if (biomes.affectations[indice] != idBiome) {
                    int[] couleurEclaircie = Methode.eclaircirPixel(new int[] {rouge, vert, bleu}, pourcentageEclaircissement);

                    rouge = couleurEclaircie[0];
                    vert = couleurEclaircie[1];
                    bleu = couleurEclaircie[2];
                }

                resultat.setRGB(x, y, new Color(rouge, vert, bleu).getRGB());
            }
        }

        return resultat;
    }

    /**
     * Crée une seule image par biome.
     * Chaque écosystème reçoit une couleur différente.
     */
    public static BufferedImage creerImageEcosystemes(ResultatBiomes biomes, ResultatEcosystemes ecosystemes) {
        BufferedImage resultat = new BufferedImage(biomes.largeur, biomes.hauteur, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < biomes.hauteur; y++) {
            for (int x = 0; x < biomes.largeur; x++) {
                int indice = y * biomes.largeur + x;
                int idEcosysteme
                    = ecosystemes.affectationsEcosystemes[indice];

                Color couleur;

                if (idEcosysteme >= 0) {
                    Color[] couleurs = {
                        Color.RED,
                        Color.BLUE,
                        Color.GREEN,
                        Color.ORANGE,
                        Color.MAGENTA,
                        Color.CYAN,
                        Color.YELLOW,
                        Color.PINK
                    };

                    couleur = couleurs[idEcosysteme % couleurs.length];
                } else {
                    int rouge = (int) biomes.descriptionsRGB[indice][0];
                    int vert = (int) biomes.descriptionsRGB[indice][1];
                    int bleu = (int) biomes.descriptionsRGB[indice][2];

                    int[] couleurEclaircie = Methode.eclaircirPixel(new int[] {rouge, vert, bleu}, 80.0);

                    couleur = new Color(couleurEclaircie[0], couleurEclaircie[1], couleurEclaircie[2]);
                }

                resultat.setRGB(x, y, couleur.getRGB());
            }
        }

        return resultat;
    }

    private static int limiterCouleur(int valeur) {
        return Math.max(0, Math.min(255, valeur));
    }
}
