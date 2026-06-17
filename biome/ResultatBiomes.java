package biome;

import java.awt.image.BufferedImage;

/**
 * Contient le résultat de la détection des biomes.
 */
public class ResultatBiomes {
    public final int largeur;
    public final int hauteur;
    public final int[] affectations;
    public final double[][] descriptionsRGB;
    public final double[][] couleursCentroides;
    public final String[] nomsClusters;
    public final boolean[] clustersIgnores;
    public final BufferedImage imageFloutee;

    public ResultatBiomes(int largeur, int hauteur, int[] affectations, double[][] descriptionsRGB, double[][] couleursCentroides, String[] nomsClusters, boolean[] clustersIgnores, BufferedImage imageFloutee) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.affectations = affectations;
        this.descriptionsRGB = descriptionsRGB;
        this.couleursCentroides = couleursCentroides;
        this.nomsClusters = nomsClusters;
        this.clustersIgnores = clustersIgnores;
        this.imageFloutee = imageFloutee;
    }

    /**
     * Indique si le cluster correspond à un contour artificiel de la carte.
     */
    public boolean estClusterIgnore(int idCluster) {
        return idCluster < 0 || idCluster >= clustersIgnores.length || clustersIgnores[idCluster];
    }
}
