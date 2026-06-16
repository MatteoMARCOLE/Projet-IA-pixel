import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) throws IOException {

        // 1) Charger l'image
        BufferedImage image = ImageIO.read(new File("Planete1.jpg"));
        System.out.println("Image chargee");

        // 2) Appliquer un flou gaussien pour lisser les couleurs
        BufferedImage imageFloutee = FlouGaussien.appliquerFlouGaussien5x5(image);
        System.out.println("Flou gaussien applique");

        // 3) Convertir l'image en tableau de descriptions RGB
        double[][] descriptions = ImageVersDonnees.convertirImageEnDonneesRGB(imageFloutee);
        System.out.println("Descriptions creees : " + descriptions.length + " pixels");

        // 4) Lancer KMeans
        KMeans kmeans = new KMeans(8, 20);
        int[] clusters = kmeans.cluster(descriptions);
        System.out.println("Clustering termine");

        // 5) Associer chaque cluster a un nom de biome
        NomDesBiomes etiqueteur = new NomDesBiomes();
        String[] etiquettes = etiqueteur.etiqueterClusters(kmeans.getCentres());

        for (int i = 0; i < etiquettes.length; i++) {
            System.out.println("Cluster " + i + " -> " + etiquettes[i]);
        }

        // 6) Creer l'image globale des clusters
        BufferedImage imageClusters = VisualisationClusters.creerImageClusters(
                imageFloutee,
                clusters,
                kmeans.getCentres()
        );

        ImageIO.write(imageClusters, "PNG", new File("planete_8clusters.png"));
        System.out.println("Image sauvegardee : planete_8clusters.png");

        // 7) Creer des images pour visualiser certains biomes separement
        BufferedImage imageEauProfonde = VisualisationBiomes.creerImageBiome(
                imageFloutee,
                clusters,
                etiquettes,
                kmeans.getCentres(),
                "Eau profonde"
        );

        ImageIO.write(imageEauProfonde, "PNG", new File("biome_eau_profonde.png"));
        System.out.println("Image sauvegardee : biome_eau_profonde.png");

        BufferedImage imageEauPeuProfonde = VisualisationBiomes.creerImageBiome(
                imageFloutee,
                clusters,
                etiquettes,
                kmeans.getCentres(),
                "Eau peu profonde"
        );

        ImageIO.write(imageEauPeuProfonde, "PNG", new File("biome_eau_peu_profonde.png"));
        System.out.println("Image sauvegardee : biome_eau_peu_profonde.png");

        BufferedImage imageDesert = VisualisationBiomes.creerImageBiome(
                imageFloutee,
                clusters,
                etiquettes,
                kmeans.getCentres(),
                "Desert"
        );

        ImageIO.write(imageDesert, "PNG", new File("biome_desert.png"));
        System.out.println("Image sauvegardee : biome_desert.png");

        BufferedImage imageForetTropicale = VisualisationBiomes.creerImageBiome(
                imageFloutee,
                clusters,
                etiquettes,
                kmeans.getCentres(),
                "Foret tropicale"
        );

        ImageIO.write(imageForetTropicale, "PNG", new File("biome_foret_tropicale.png"));
        System.out.println("Image sauvegardee : biome_foret_tropicale.png");

        BufferedImage imageGlacier = VisualisationBiomes.creerImageBiome(
                imageFloutee,
                clusters,
                etiquettes,
                kmeans.getCentres(),
                "Glacier"
        );

        ImageIO.write(imageGlacier, "PNG", new File("biome_glacier.png"));
        System.out.println("Image sauvegardee : biome_glacier.png");

        // 8) Detection des ecosystemes : Eau profonde
        int[] ecosystemesEauProfonde = DetectionEcosystemes.detecterEcosystemes(
                imageFloutee,
                clusters,
                etiquettes,
                "Eau profonde"
        );

        int nbEcosystemesEauProfonde = DetectionEcosystemes.compterEcosystemes(ecosystemesEauProfonde);
        System.out.println("Nombre d'ecosystemes Eau profonde : " + nbEcosystemesEauProfonde);

        BufferedImage imageEcosystemesEauProfonde = VisualisationEcosystemes.creerImageEcosystemes(
                imageFloutee,
                ecosystemesEauProfonde
        );

        ImageIO.write(
                imageEcosystemesEauProfonde,
                "PNG",
                new File("ecosystemes_eau_profonde.png")
        );

        System.out.println("Image sauvegardee : ecosystemes_eau_profonde.png");

        // 9) Detection des ecosystemes : Desert
        int[] ecosystemesDesert = DetectionEcosystemes.detecterEcosystemes(
                imageFloutee,
                clusters,
                etiquettes,
                "Desert"
        );

        int nbEcosystemesDesert = DetectionEcosystemes.compterEcosystemes(ecosystemesDesert);
        System.out.println("Nombre d'ecosystemes Desert : " + nbEcosystemesDesert);

        BufferedImage imageEcosystemesDesert = VisualisationEcosystemes.creerImageEcosystemes(
                imageFloutee,
                ecosystemesDesert
        );

        ImageIO.write(
                imageEcosystemesDesert,
                "PNG",
                new File("ecosystemes_desert.png")
        );

        System.out.println("Image sauvegardee : ecosystemes_desert.png");

        // 10) Detection des ecosystemes : Foret tropicale
        int[] ecosystemesForetTropicale = DetectionEcosystemes.detecterEcosystemes(
                imageFloutee,
                clusters,
                etiquettes,
                "Foret tropicale"
        );

        int nbEcosystemesForetTropicale = DetectionEcosystemes.compterEcosystemes(ecosystemesForetTropicale);
        System.out.println("Nombre d'ecosystemes Foret tropicale : " + nbEcosystemesForetTropicale);

        BufferedImage imageEcosystemesForetTropicale = VisualisationEcosystemes.creerImageEcosystemes(
                imageFloutee,
                ecosystemesForetTropicale
        );

        ImageIO.write(
                imageEcosystemesForetTropicale,
                "PNG",
                new File("ecosystemes_foret_tropicale.png")
        );

        System.out.println("Image sauvegardee : ecosystemes_foret_tropicale.png");
    }
}