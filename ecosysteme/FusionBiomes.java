package ecosysteme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FusionBiomes {

    public static void afficherFusion(String[] etiquettes) {
        Map<String, ArrayList<Integer>> groupes = new HashMap<>();

        // On regroupe les numeros de clusters qui ont le meme nom de biome
        for (int i = 0; i < etiquettes.length; i++) {
            String nomBiome = etiquettes[i];

            if (!groupes.containsKey(nomBiome)) {
                groupes.put(nomBiome, new ArrayList<Integer>());
            }

            groupes.get(nomBiome).add(i);
        }

        // Affichage console pour montrer la fusion effectuee
        System.out.println("\nFusion des clusters par biome :");

        for (String nomBiome : groupes.keySet()) {
            System.out.println(nomBiome + " <- clusters " + groupes.get(nomBiome));
        }
    }

    public static BufferedImage creerImageBiomesFusionnes(
            BufferedImage imageOriginale,
            int[] clusters,
            String[] etiquettes) {

        int largeur = imageOriginale.getWidth();
        int hauteur = imageOriginale.getHeight();

        BufferedImage resultat = new BufferedImage(
                largeur,
                hauteur,
                BufferedImage.TYPE_3BYTE_BGR
        );

        int index = 0;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                // On recupere le cluster du pixel courant
                int numeroCluster = clusters[index];

                // On recupere le nom du biome associe a ce cluster
                String nomBiome = etiquettes[numeroCluster];

                // Plusieurs clusters peuvent avoir le meme biome
                // On leur donne donc la meme couleur finale
                Color couleur = couleurPourBiome(nomBiome);
                resultat.setRGB(x, y, couleur.getRGB());

                index++;
            }
        }

        return resultat;
    }

    private static Color couleurPourBiome(String nomBiome) {

        // Couleurs de reference utilisees pour afficher les biomes fusionnes
        switch (nomBiome) {
            case "Toundra":
                return new Color(71, 70, 61);

            case "Taiga":
                return new Color(43, 50, 35);

            case "Foret temperee":
                return new Color(59, 66, 43);

            case "Foret tropicale":
                return new Color(46, 64, 34);

            case "Savane":
                return new Color(84, 106, 70);

            case "Prairie":
                return new Color(104, 95, 82);

            case "Desert":
                return new Color(152, 140, 120);

            case "Glacier":
                return new Color(200, 200, 200);

            case "Eau peu profonde":
                return new Color(49, 83, 100);

            case "Eau profonde":
                return new Color(12, 31, 47);

            default:
                // Couleur visible si un biome n'est pas reconnu
                return Color.MAGENTA;
        }
    }
}