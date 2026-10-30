/**
 * Enum-luokka joka mallintaa ikärajoja
 */
public enum Ikaraja {
    S(0), K7(7), K12(12), K16(16), K18(18);

    private final int vuodet;

    Ikaraja(int vuodet) {
        this.vuodet = vuodet;
    }

    public int getVuodet() {
        return vuodet;
    }
}
