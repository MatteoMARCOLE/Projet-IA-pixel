package biome;

/**
 * Représente un biome avec son nom et sa couleur RGB de référence.
 */
public class Biome {
    public final String nom;
    public final int rouge;
    public final int vert;
    public final int bleu;

    public Biome(String nom, int rouge, int vert, int bleu) {
        this.nom = nom;
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }
}
