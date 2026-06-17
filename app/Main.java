package app;

import biome.DetecteurBiomes;
import biome.ResultatBiomes;
import ecosysteme.DetecteurEcosystemes;
import ecosysteme.FusionBiomes;
import ecosysteme.ResultatEcosystemes;
import image.ImageUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Programme principal du projet.
 *
 * Exemple : java app.Main Planete1.jpg 10 gaussien
 */
public class Main {

    public static void main(String[] arguments) {
        if (arguments.length == 0) {
            System.out.println("Utilisation : java app.Main <image> [nombreBiomes] [flou]");
            System.out.println("Exemple : java app.Main Planete1.jpg 10 gaussien");
            System.out.println("Flous disponibles : gaussien ou moyenne");
            return;
        }

        String cheminImage = arguments[0];
        int nombreBiomes = 10;
        boolean utiliserFlouGaussien = true;
        double epsilon = 6.0;
        int nombrePointsMinimum = 3;

        if (arguments.length >= 2) {
            try {
                nombreBiomes = Integer.parseInt(arguments[1]);
            } catch (NumberFormatException exception) {
                System.out.println("Nombre de biomes incorrect : utilisation de la valeur 10.");
            }
        }

        if (arguments.length >= 3 && arguments[2].equalsIgnoreCase("moyenne")) {
            utiliserFlouGaussien = false;
        }

        try {
            File dossierResultats = new File("resultats");

            if (!dossierResultats.exists()) {
                dossierResultats.mkdirs();
            }

            File[] anciensFichiers = dossierResultats.listFiles();

            if (anciensFichiers != null) {
                for (File fichier : anciensFichiers) {
                    if (fichier.isFile() && fichier.getName().toLowerCase().endsWith(".png")) {
                        fichier.delete();
                    }
                }
            }

            BufferedImage imageOriginale = ImageUtils.chargerImage(cheminImage);

            System.out.println("1. Detection des biomes...");

            ResultatBiomes biomes = DetecteurBiomes.detecterBiomes(
                    imageOriginale,
                    nombreBiomes,
                    utiliserFlouGaussien
            );

            ImageUtils.enregistrerImage(biomes.imageFloutee, "resultats/image_floutee.png");
            ImageUtils.enregistrerImage(ImageUtils.creerImageBiomes(biomes), "resultats/biomes.png");

            FusionBiomes.afficherFusion(biomes);

            // Images des clusters separes
            for (int biome = 0; biome < nombreBiomes; biome++) {
                if (biomes.estClusterIgnore(biome)) {
                    System.out.println("Cluster " + biome + " : contour de carte ignore");
                    continue;
                }

                int rouge = (int) Math.round(biomes.couleursCentroides[biome][0]);
                int vert = (int) Math.round(biomes.couleursCentroides[biome][1]);
                int bleu = (int) Math.round(biomes.couleursCentroides[biome][2]);

                System.out.println(
                        "Biome " + biome + " : " +
                        biomes.nomsClusters[biome] +
                        " [" + rouge + ", " + vert + ", " + bleu + "]"
                );

                BufferedImage imageBiome = ImageUtils.mettreEnAvantBiome(biomes, biome, 75.0);
                ImageUtils.enregistrerImage(imageBiome, "resultats/biome_" + biome + ".png");
            }

            // Images fusionnees par nom de biome
            String[] biomesUniques = FusionBiomes.obtenirBiomesUniques(biomes);

            for (String nomBiome : biomesUniques) {
                String nomFichier = FusionBiomes.nomFichierPropre(nomBiome);

                BufferedImage imageBiomeFusionne = FusionBiomes.creerImageBiomeFusionne(biomes, nomBiome);

                ImageUtils.enregistrerImage(
                        imageBiomeFusionne,
                        "resultats/fusion_" + nomFichier + ".png"
                );

                System.out.println("Image fusionnee sauvegardee : resultats/fusion_" + nomFichier + ".png");
            }

            System.out.println("2. Detection des ecosystemes basee sur les biomes fusionnes...");

            // Les ecosystemes sont maintenant calcules par nom de biome fusionne
            for (String nomBiome : biomesUniques) {
                String nomFichier = FusionBiomes.nomFichierPropre(nomBiome);

                ResultatEcosystemes ecosystemes = DetecteurEcosystemes.detecterEcosystemesFusionnes(
                        biomes,
                        nomBiome,
                        epsilon,
                        nombrePointsMinimum
                );

                System.out.println(
                        "Biome fusionne " + nomBiome + " : " +
                        ecosystemes.nombreEcosystemes +
                        " ecosysteme(s)"
                );

                BufferedImage imageEcosystemes = ImageUtils.creerImageEcosystemes(biomes, ecosystemes);
                ImageUtils.enregistrerImage(
                        imageEcosystemes,
                        "resultats/ecosystemes_" + nomFichier + ".png"
                );
            }

            System.out.println("Traitement termine. Les images sont dans le dossier resultats.");

        } catch (IOException exception) {
            System.err.println("Erreur avec l'image : " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            System.err.println("Erreur de parametre : " + exception.getMessage());
        }
    }
}