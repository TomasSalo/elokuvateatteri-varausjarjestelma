/**
 * Mallintaa yksittäistä fyysistä istumapaikkaa
 */

public class Istumapaikka {
    private int rivi;
    private int paikka;

    public Istumapaikka(int rivi, int paikka) {
        this.rivi = rivi;
        this.paikka = paikka;
    }

    public int getRivi() {
        return rivi;
    }

    public int getPaikka() {
        return paikka;
    }


    @Override
    public String toString() {
        return "Istumapaikka{" +
                "rivi=" + rivi +
                ", paikka=" + paikka +
                '}';
    }

}
