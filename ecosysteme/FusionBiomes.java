package ecosysteme;

import biome.ResultatBiomes;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FusionBiomes {

    public static void afficherFusion(ResultatBiomes biomes) {
        String[] biomesUniques = obtenirBiomesUniques(biomes);

        System.out.println("\nFusion des clusters par biome :");

        for (String nomBiome : biomesUniques) {
            System.out.print(nomBiome + " <- clusters ");

            for (int cluster = 0; cluster < biomes.nomsClusters.length; cluster++) {
                if (!biomes.estClusterIgnore(cluster)
                        && memeNomBiome(biomes.nomsClusters[cluster], nomBiome)) {
                    System.out.print(cluster + " ");
                }
            }

            System.out.println();
        }
    }

    public static String[] obtenirBiomesUniques(ResultatBiomes biomes) {
        ArrayList<String> nomsUniques = new ArrayList<>();

        for (int cluster = 0; cluster < biomes.nomsClusters.length; cluster++) {
            if (biomes.estClusterIgnore(cluster)) {
                continue;
            }

            String nomBiome = biomes.nomsClusters[cluster];

            if (!contientBiome(nomsUniques, nomBiome)) {
                nomsUniques.add(nomBiome);
            }
        }

        String[] resultat = new String[nomsUniques.size()];

        for (int i = 0; i < nomsUniques.size(); i++) {
            resultat[i] = nomsUniques.get(i);
        }

        return resultat;
    }

    public static BufferedImage creerImageBiomeFusionne(ResultatBiomes biomes, String biomeRecherche) {
        return creerImageBiomeFusionne(
                biomes.imageFloutee,
                biomes.affectations,
                biomes.nomsClusters,
                biomeRecherche
        );
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
                boolean appartientAuBiome = false;

                if (numeroCluster >= 0 && numeroCluster < etiquettes.length) {
                    String nomBiome = etiquettes[numeroCluster];
                    appartientAuBiome = memeNomBiome(nomBiome, biomeRecherche);
                }

                if (appartientAuBiome) {
                    // On garde le pixel original pour tous les clusters du biome recherche
                    resultat.setRGB(x, y, imageOriginale.getRGB(x, y));
                } else {
                    // Le reste de l'image est mis en gris pour mieux voir la fusion
                    resultat.setRGB(x, y, Color.LIGHT_GRAY.getRGB());
                }

                index++;
            }
        }

        return resultat;
    }

    public static String nomFichierPropre(String nomBiome) {
        return nomBiome
                .trim()
                .toLowerCase()
                .replace(" ", "_")
                .replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .replace("à", "a")
                .replace("ù", "u")
                .replace("ï", "i")
                .replace("î", "i")
                .replace("ç", "c");
    }

    private static boolean contientBiome(ArrayList<String> noms, String nomBiome) {
        for (String nom : noms) {
            if (memeNomBiome(nom, nomBiome)) {
                return true;
            }
        }

        return false;
    }

    private static boolean memeNomBiome(String nom1, String nom2) {
        return nom1.trim().equalsIgnoreCase(nom2.trim());
    }
}