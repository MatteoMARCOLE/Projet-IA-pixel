/**
 * Classe représentant un biome, avec son nom et sa couleur (r, g, b).
 */
public class Biome {
    public String nom;
    public int r, g, b;

    /**
     * Constructeur de la classe Biome
     * @param nom le nom du biome
     * @param r la composante rouge de la couleur du biome (entre 0 et 255)
     * @param g la composante verte de la couleur du biome (entre 0 et 255)
     * @param b la composante bleue de la couleur du biome (entre 0 et 255)
     */
    public Biome(String nom, int r, int g, int b) {
        this.nom = nom;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
