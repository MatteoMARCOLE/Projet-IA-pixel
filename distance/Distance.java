package distance;

/**
 * Interface représentant une distance entre deux vecteurs.
 */
@FunctionalInterface
public interface Distance {
    double calculer(double[] premier, double[] second);
}
