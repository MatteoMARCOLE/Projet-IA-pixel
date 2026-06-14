import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedImage image = ImageIO.read(new File("Planete1.jpg"));

        BufferedImage imageFloutee = FlouGaussien.appliquerFlouGaussien5x5(image);

        double[][] descriptions = ImageVersDonnees.convertirImageEnDonneesRGB(imageFloutee);

        KMeans kmeans = new KMeans(8, 20);
        int[] clusters = kmeans.cluster(descriptions);

        NomDesBiomes etiqueteur = new NomDesBiomes();

        String[] etiquettes = etiqueteur.etiqueterClusters(kmeans.getCentres());

        for (int i = 0; i < etiquettes.length; i++) {
            System.out.println("Cluster " + i + " -> " + etiquettes[i]);
        }

        BufferedImage imageClusters = VisualisationClusters.creerImageClusters(
                imageFloutee,
                clusters,
                kmeans.getCentres()
        );

        ImageIO.write(imageClusters, "PNG", new File("planete_8clusters.png"));
    }
}