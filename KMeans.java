import java.util.Random;

public class KMeans implements ClusteringInterface {

    private int k;                  // nombre de clusters / biomes
    private int maxIterations;      // limite pour éviter de tourner trop longtemps
    private double[][] centres;     // centres des clusters

    public KMeans(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    @Override
    public int[] cluster(double[][] descriptions) {

        int nbObjets = descriptions.length;              // nombre de pixels
        int nbCaracteristiques = descriptions[0].length; // R, G, B

        int[] clusters = new int[nbObjets];

        // Au départ, aucun pixel n'est affecté
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = -1;
        }

        initialiserCentres(descriptions);

        for (int iteration = 0; iteration < maxIterations; iteration++) {

            boolean changement = false;

            // 1) Affecter chaque pixel au centre le plus proche
            for (int i = 0; i < nbObjets; i++) {
                int nouveauCluster = centreLePlusProche(descriptions[i]);

                if (nouveauCluster != clusters[i]) {
                    clusters[i] = nouveauCluster;
                    changement = true;
                }
            }

            // 2) Recalculer les centres avec la moyenne des pixels du cluster
            double[][] nouveauxCentres = new double[k][nbCaracteristiques];
            int[] compteurs = new int[k];

            for (int i = 0; i < nbObjets; i++) {
                int cluster = clusters[i];
                compteurs[cluster]++;

                for (int j = 0; j < nbCaracteristiques; j++) {
                    nouveauxCentres[cluster][j] += descriptions[i][j];
                }
            }

            for (int c = 0; c < k; c++) {
                if (compteurs[c] > 0) {
                    for (int j = 0; j < nbCaracteristiques; j++) {
                        nouveauxCentres[c][j] /= compteurs[c];
                    }
                } else {
                    // Si un cluster est vide, on garde son ancien centre
                    for (int j = 0; j < nbCaracteristiques; j++) {
                        nouveauxCentres[c][j] = centres[c][j];
                    }
                }
            }

            centres = nouveauxCentres;

            // Si plus aucun pixel ne change de cluster, on arrête
            if (!changement) {
                break;
            }
        }

        return clusters;
    }

    private void initialiserCentres(double[][] descriptions) {
        Random random = new Random();
        centres = new double[k][descriptions[0].length];

        for (int c = 0; c < k; c++) {
            int index = random.nextInt(descriptions.length);

            for (int j = 0; j < descriptions[index].length; j++) {
                centres[c][j] = descriptions[index][j];
            }
        }
    }

    private int centreLePlusProche(double[] description) {
        int meilleurCluster = 0;
        double meilleureDistance = distance(description, centres[0]);

        for (int c = 1; c < k; c++) {
            double d = distance(description, centres[c]);

            if (d < meilleureDistance) {
                meilleureDistance = d;
                meilleurCluster = c;
            }
        }

        return meilleurCluster;
    }

    private double distance(double[] a, double[] b) {
        double somme = 0;

        for (int i = 0; i < a.length; i++) {
            double difference = a[i] - b[i];
            somme += difference * difference;
        }

        return Math.sqrt(somme);
    }

    public double[][] getCentres() {
        return centres;
    }
}