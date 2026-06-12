import java.util.List;

public class Methode {

    /**
     * Calcule la distance entre deux couleurs RGB
     * @param r1 
     * @param g1
     * @param b1
     * @param r2
     * @param g2
     * @param b2
     * @return la distance entre les deux couleurs
     */
    public static double calculerDistanceRGB(int r1, int g1, int b1, int r2, int g2, int b2) {
        return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
    }

    /**
     * Trouve le biome de référence le plus proche d'un cluster de couleur
     * @param clusterR
     * @param clusterG
     * @param clusterB
     * @param references 
     * @return le nom du biome de référence le plus proche du cluster de couleur
     */
    public static String trouverBiomeLePlusProche(int clusterR, int clusterG, int clusterB, List<Biome> references) {
        String nomBiome = "Inconnu";
        double distanceMinimale = calculerDistanceRGB(clusterR, clusterG, clusterB, references.get(0).r, references.get(0).g, references.get(0).b);

        for (Biome ref : references) {
            double distance = calculerDistanceRGB(clusterR, clusterG, clusterB, ref.r, ref.g, ref.b);
            
            if (distance < distanceMinimale) {
                distanceMinimale = distance;
                nomBiome = ref.nom;
            }
        }
        return nomBiome;
    }


    /**
     * Eclaircit un canal de couleur en fonction d'un pourcentage donné
     * @param valeurAncienne la valeur actuelle du canal de couleur (entre 0 et 255)
     * @param pourcentage le pourcentage d'éclaircissement (entre 0 et 100)
     * @return la nouvelle valeur du canal de couleur après éclaircissement
     */
    public static int eclaircirCanal(int valeurAncienne, double pourcentage) {
        //int nouveau = Math.round(150+75/100∗(255−150)) = 229 ;
        return (int) Math.round(valeurAncienne + (pourcentage / 100.0) * (255 - valeurAncienne));
    }

    /**
     * Eclaircit un pixel RGB en fonction d'un pourcentage donné
     * @param pixelRGB un tableau de trois entiers représentant les valeurs R, G et B du pixel (entre 0 et 255)
     * @param pourcentage le pourcentage d'éclaircissement (entre 0 et 100)
     * @return un tableau de trois entiers représentant les nouvelles valeurs R, G et B du pixel après éclaircissement
     */
    public static int[] eclaircirPixel(int[] pixelRGB, double pourcentage) {
        int rNouveau = eclaircirCanal(pixelRGB[0], pourcentage);
        int gNouveau = eclaircirCanal(pixelRGB[1], pourcentage);
        int bNouveau = eclaircirCanal(pixelRGB[2], pourcentage);
        
        return new int[]{rNouveau, gNouveau, bNouveau};
    }
}
