package ecosysteme;

/**
 * Contient le résultat de la détection des écosystèmes d'un biome.
 */
public class ResultatEcosystemes {
    public final int largeur;
    public final int hauteur;
    public final int idBiome;
    public final int[] affectationsEcosystemes;
    public final int nombreEcosystemes;

    public ResultatEcosystemes(int largeur, int hauteur, int idBiome, int[] affectationsEcosystemes, int nombreEcosystemes) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.idBiome = idBiome;
        this.affectationsEcosystemes = affectationsEcosystemes;
        this.nombreEcosystemes = nombreEcosystemes;
    }
}
