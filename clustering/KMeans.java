package clustering;

import java.util.Arrays;
import java.util.Random;

import distance.Distance;

/**
 * Implémentation de K-Means.
 *
 * Étapes :
 * 1. choisir des centroïdes de départ ;
 * 2. affecter chaque objet au centroïde le plus proche ;
 * 3. recalculer les centroïdes avec la moyenne des objets du cluster ;
 * 4. recommencer tant qu'une affectation change.
 */
public class KMeans implements ClusteringInterface {

    private int nombreClusters;
    private Distance distance;
    private Random aleatoire;

    public KMeans(int nombreClusters, Distance distance) {
        this.nombreClusters = nombreClusters;
        this.distance = distance;
        this.aleatoire = new Random();
    }

    @Override
    public int[] cluster(double[][] descriptions) {
        int nombreObjets = descriptions.length;

        if (nombreObjets == 0) {
            return new int[0];
        }

        int nombreCaracteristiques = descriptions[0].length;

        double[][] centroides = initialiserCentroides(descriptions, nombreCaracteristiques);

        int[] affectations = new int[nombreObjets];
        Arrays.fill(affectations, -1);

        boolean changement;

        do {
            changement = affecterObjets(descriptions, centroides, affectations);

            recalculerCentroides(descriptions, centroides, affectations, nombreCaracteristiques);
        } while (changement);

        return affectations;
    }

    /**
     * Choisit au hasard des objets comme centroïdes de départ.
     */
    private double[][] initialiserCentroides(double[][] descriptions, int nombreCaracteristiques) {
        double[][] centroides = new double[nombreClusters][nombreCaracteristiques];
        boolean[] dejaChoisi = new boolean[descriptions.length];

        for (int cluster = 0; cluster < nombreClusters; cluster++) {
            int indice;

            do {
                indice = aleatoire.nextInt(descriptions.length);
            } while (dejaChoisi[indice]);

            dejaChoisi[indice] = true;
            centroides[cluster] = Arrays.copyOf(descriptions[indice], nombreCaracteristiques);
        }

        return centroides;
    }

    /**
     * Affecte chaque objet au centroïde le plus proche.
     *
     * @return vrai si au moins une affectation a changé
     */
    private boolean affecterObjets(double[][] descriptions, double[][] centroides, int[] affectations) {
        boolean changement = false;

        for (int objet = 0; objet < descriptions.length; objet++) {
            int clusterLePlusProche = 0;
            double distanceMinimale = distance.calculer(descriptions[objet], centroides[0]);

            for (int cluster = 1; cluster < nombreClusters; cluster++) {
                double distanceActuelle = distance.calculer(descriptions[objet], centroides[cluster]);

                if (distanceActuelle < distanceMinimale) {
                    distanceMinimale = distanceActuelle;
                    clusterLePlusProche = cluster;
                }
            }

            if (affectations[objet] != clusterLePlusProche) {
                affectations[objet] = clusterLePlusProche;
                changement = true;
            }
        }

        return changement;
    }

    /**
     * Recalcule les centroïdes en faisant la moyenne des objets du cluster.
     */
    private void recalculerCentroides(double[][] descriptions, double[][] centroides, int[] affectations, int nombreCaracteristiques) {
        double[][] sommes = new double[nombreClusters][nombreCaracteristiques];
        int[] effectifs = new int[nombreClusters];

        for (int objet = 0; objet < descriptions.length; objet++) {
            int cluster = affectations[objet];
            effectifs[cluster]++;

            for (int caracteristique = 0; caracteristique < nombreCaracteristiques; caracteristique++) {
                sommes[cluster][caracteristique]
                    += descriptions[objet][caracteristique];
            }
        }

        for (int cluster = 0; cluster < nombreClusters; cluster++) {
            if (effectifs[cluster] == 0) {
                continue;
            }

            for (int caracteristique = 0; caracteristique < nombreCaracteristiques; caracteristique++) {
                centroides[cluster][caracteristique]
                    = sommes[cluster][caracteristique] / effectifs[cluster];
            }
        }
    }
}
