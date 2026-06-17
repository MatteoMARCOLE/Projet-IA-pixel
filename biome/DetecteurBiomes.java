package biome;

import java.awt.image.BufferedImage;
import java.util.List;

import clustering.KMeans;
import distance.Distance;
import distance.DistanceEuclidienne;
import image.FlouGaussien;
import image.FlouParMoyenne;
import image.ImageUtils;
import utilitaire.Methode;

/**
 * Détecte les biomes grâce à K-Means sur les couleurs RGB.
 */
public final class DetecteurBiomes {

    /*
     * Le contour dessiné sur la carte est très sombre et ne représente pas un biome.
     * On ignore donc les petits clusters dont la couleur moyenne est très sombre.
     */
    private static final int LUMINOSITE_MAX_CONTOUR = 70;
    private static final double PROPORTION_MAX_CONTOUR = 0.06;

    private DetecteurBiomes() {
    }

    public static ResultatBiomes detecterBiomes(BufferedImage image, int nombreClusters, boolean utiliserFlouGaussien) {
        BufferedImage imageFloutee;

        if (utiliserFlouGaussien) {
            imageFloutee = FlouGaussien.appliquer(image);
        } else {
            imageFloutee = FlouParMoyenne.appliquer(image);
        }

        double[][] descriptionsRGB = ImageUtils.extraireRGB(imageFloutee);
        int largeur = imageFloutee.getWidth();
        int hauteur = imageFloutee.getHeight();

        Distance distance = new DistanceEuclidienne();
        KMeans kMeans = new KMeans(nombreClusters, distance);
        int[] affectations = kMeans.cluster(descriptionsRGB);

        int[] effectifs = calculerEffectifs(affectations, nombreClusters);

        double[][] couleursCentroides = calculerCouleursCentroides(descriptionsRGB, affectations, nombreClusters, effectifs);

        boolean[] clustersIgnores = detecterClustersDeContour(couleursCentroides, effectifs, largeur * hauteur);

        /*
         * Les pixels du contour reçoivent l'affectation -1.
         * Ils ne seront donc ni considérés comme un biome, ni envoyés à DBSCAN.
         */
        for (int pixel = 0; pixel < affectations.length; pixel++) {
            int cluster = affectations[pixel];

            if (clustersIgnores[cluster]) {
                affectations[pixel] = -1;
            }
        }

        String[] nomsClusters = nommerClusters(couleursCentroides, clustersIgnores);

        return new ResultatBiomes(largeur, hauteur, affectations, descriptionsRGB, couleursCentroides, nomsClusters, clustersIgnores, imageFloutee);
    }

    /**
     * Compte le nombre de pixels affectés à chaque cluster.
     */
    private static int[] calculerEffectifs(int[] affectations, int nombreClusters) {
        int[] effectifs = new int[nombreClusters];

        for (int cluster : affectations) {
            effectifs[cluster]++;
        }

        return effectifs;
    }

    /**
     * Calcule la couleur moyenne de chaque cluster.
     */
    private static double[][] calculerCouleursCentroides(double[][] descriptionsRGB, int[] affectations, int nombreClusters, int[] effectifs) {
        double[][] sommes = new double[nombreClusters][3];

        for (int pixel = 0; pixel < affectations.length; pixel++) {
            int cluster = affectations[pixel];

            sommes[cluster][0] += descriptionsRGB[pixel][0];
            sommes[cluster][1] += descriptionsRGB[pixel][1];
            sommes[cluster][2] += descriptionsRGB[pixel][2];
        }

        double[][] couleursCentroides = new double[nombreClusters][3];

        for (int cluster = 0; cluster < nombreClusters; cluster++) {
            if (effectifs[cluster] == 0) {
                continue;
            }

            couleursCentroides[cluster][0]
                = sommes[cluster][0] / effectifs[cluster];
            couleursCentroides[cluster][1]
                = sommes[cluster][1] / effectifs[cluster];
            couleursCentroides[cluster][2]
                = sommes[cluster][2] / effectifs[cluster];
        }

        return couleursCentroides;
    }

    /**
     * Repère les clusters qui correspondent au trait sombre entourant la carte.
     *
     * Un contour est reconnu uniquement s'il est :
     * - très sombre ;
     * - petit par rapport à toute l'image.
     *
     * Cette seconde condition évite de supprimer un vrai grand biome sombre,
     * comme l'eau profonde.
     */
    private static boolean[] detecterClustersDeContour(double[][] couleursCentroides, int[] effectifs, int nombreTotalPixels) {
        boolean[] clustersIgnores = new boolean[couleursCentroides.length];

        for (int cluster = 0; cluster < couleursCentroides.length; cluster++) {
            double rouge = couleursCentroides[cluster][0];
            double vert = couleursCentroides[cluster][1];
            double bleu = couleursCentroides[cluster][2];
            double luminosite = (rouge + vert + bleu) / 3.0;
            double proportion = effectifs[cluster] / (double) nombreTotalPixels;

            clustersIgnores[cluster] = luminosite < LUMINOSITE_MAX_CONTOUR && proportion < PROPORTION_MAX_CONTOUR;
        }

        return clustersIgnores;
    }

    /**
     * Associe chaque cluster à la couleur de biome la plus proche.
     */
    private static String[] nommerClusters(double[][] couleursCentroides, boolean[] clustersIgnores) {
        List<Biome> references = ReferenceBiomes.obtenirBiomes();
        String[] noms = new String[couleursCentroides.length];

        for (int cluster = 0; cluster < couleursCentroides.length; cluster++) {
            if (clustersIgnores[cluster]) {
                noms[cluster] = "Contour ignoré";
                continue;
            }

            int rouge = (int) Math.round(couleursCentroides[cluster][0]);
            int vert = (int) Math.round(couleursCentroides[cluster][1]);
            int bleu = (int) Math.round(couleursCentroides[cluster][2]);

            noms[cluster] = Methode.trouverBiomeLePlusProche(rouge, vert, bleu, references);
        }

        return noms;
    }
}
