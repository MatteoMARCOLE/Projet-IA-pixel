package distance;

/**
 * Distance euclidienne classique.
 */
public class DistanceEuclidienne implements Distance {

    @Override
    public double calculer(double[] premier, double[] second) {
        if (premier.length != second.length) {
            throw new IllegalArgumentException("Les deux vecteurs doivent avoir la même taille.");
        }

        double somme = 0.0;

        for (int i = 0; i < premier.length; i++) {
            double difference = premier[i] - second[i];
            somme += difference * difference;
        }

        return Math.sqrt(somme);
    }
}
