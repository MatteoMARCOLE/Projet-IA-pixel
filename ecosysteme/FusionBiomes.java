package ecosysteme;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class FusionBiomes {

    public static void afficherFusion(String[] etiquettes) {
        boolean[] dejaAffiche = new boolean[etiquettes.length];

        System.out.println("\nFusion des clusters par biome :");

        for (int i = 0; i < etiquettes.length; i++) {
            if (!dejaAffiche[i]) {
                System.out.print(etiquettes[i] + " <- clusters ");

                for (int j = 0; j < etiquettes.length; j++) {
                    if (etiquettes[j].trim().equalsIgnoreCase(etiquettes[i].trim())) {
                        System.out.print(j + " ");
                        dejaAffiche[j] = true;
                    }
                }

                System.out.println();
            }
        }
    }

    public static BufferedImage creerImageBiomeFusionne(
            BufferedImage imageOriginale,
            int[] clusters,
            String[] etiquettes,
            String biomeRecherche) {

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

                int numeroCluster = clusters[index];
                String nomBiome = etiquettes[numeroCluster];

                if (nomBiome.trim().equalsIgnoreCase(biomeRecherche.trim())) {
                    // On garde le pixel original pour tous les clusters du biome recherche
                    resultat.setRGB(x, y, imageOriginale.getRGB(x, y));
                } else {
                    // Le reste de l'image est mis en gris
                    resultat.setRGB(x, y, Color.LIGHT_GRAY.getRGB());
                }

                index++;
            }
        }

        return resultat;
    }
}