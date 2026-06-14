public class Biome {

    private String nom;
    private int rouge;
    private int vert;
    private int bleu;

    public Biome(String nom, int rouge, int vert, int bleu) {
        this.nom = nom;
        this.rouge = rouge;
        this.vert = vert;
        this.bleu = bleu;
    }

    public String getNom() {
        return nom;
    }

    public int getRouge() {
        return rouge;
    }

    public int getVert() {
        return vert;
    }

    public int getBleu() {
        return bleu;
    }

    public double[] getCouleur() {
        return new double[] { rouge, vert, bleu };
    }
}