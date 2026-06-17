package clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import distance.Distance;

/**
 * Implémentation de DBSCAN en suivant directement le pseudo-code du cours.
 *
 * Un point est un point cœur s'il possède au moins MinPts voisins dans un
 * rayon Epsilon. Les points accessibles depuis un point cœur sont ajoutés au
 * même cluster. Les points qui ne rejoignent aucun cluster sont du bruit.
 */
public class DBSCAN implements ClusteringInterface {

    private double epsilon;
    private int nombrePointsMinimum;
    private Distance distance;

    private double[][] descriptions;
    private boolean[] traites;
    private int[] affectations;

    public DBSCAN(double epsilon, int nombrePointsMinimum, Distance distance) {
        this.epsilon = epsilon;
        this.nombrePointsMinimum = nombrePointsMinimum;
        this.distance = distance;
    }

    @Override
    public int[] cluster(double[][] descriptions) {
        this.descriptions = descriptions;
        this.traites = new boolean[descriptions.length];
        this.affectations = new int[descriptions.length];

        // -1 signifie : aucun cluster, donc bruit pour le moment.
        Arrays.fill(affectations, -1);

        int numeroCluster = 0;

        for (int point = 0; point < descriptions.length; point++) {
            if (!traites[point]) {
                traites[point] = true;

                List<Integer> voisins = regionQuery(point);

                if (voisins.size() >= nombrePointsMinimum) {
                    expandCluster(point, voisins, numeroCluster);
                    numeroCluster++;
                }
            }
        }

        return affectations;
    }

    /**
     * Agrandit le cluster à partir d'un point cœur.
     */
    private void expandCluster(int pointDepart, List<Integer> voisins, int numeroCluster) {
        affectations[pointDepart] = numeroCluster;

        /*
         * La taille de la liste peut augmenter pendant la boucle.
         * Cela correspond à Vn = Vn + Vi dans le pseudo-code du cours.
         */
        for (int i = 0; i < voisins.size(); i++) {
            int point = voisins.get(i);

            if (!traites[point]) {
                traites[point] = true;

                List<Integer> voisinsDuPoint = regionQuery(point);

                if (voisinsDuPoint.size() > nombrePointsMinimum) {
                    for (int voisin : voisinsDuPoint) {
                        if (!voisins.contains(voisin)) {
                            voisins.add(voisin);
                        }
                    }
                }
            }

            if (affectations[point] == -1) {
                affectations[point] = numeroCluster;
            }
        }
    }

    /**
     * Retourne les points situés à une distance inférieure ou égale à epsilon.
     * Le point lui-même n'est pas ajouté, comme dans le pseudo-code du cours.
     */
    private List<Integer> regionQuery(int point) {
        List<Integer> voisins = new ArrayList<>();

        for (int autrePoint = 0; autrePoint < descriptions.length; autrePoint++) {

            if (autrePoint == point) {
                continue;
            }

            double distanceEntrePoints = distance.calculer(descriptions[point], descriptions[autrePoint]);

            if (distanceEntrePoints <= epsilon) {
                voisins.add(autrePoint);
            }
        }

        return voisins;
    }
}
