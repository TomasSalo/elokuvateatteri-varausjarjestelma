/**
 * Edustaa elokuvateatterin fyysistä salia.
 * Sali pitää kirjaa nimestään sekä istumapaikkojen määrästä rivien ja sarakkeiden avulla.
 */
public class Sali {
    private String nimi;
    private int rivit;
    private int paikatPerRivi;

    /**
     * Luo uuden salin annetuilla tiedoilla.
     * * @param nimi Salin yksilöllinen nimi (esim. "Sali 1" tai "Isense").
     * @param rivit Rivien lukumäärä salissa.
     * @param paikatPerRivi Istumapaikkojen määrä yhdellä rivillä.
     */
    public Sali(String nimi, int rivit, int paikatPerRivi) {
        this.nimi = nimi;
        this.rivit = rivit;
        this.paikatPerRivi = paikatPerRivi;
    }

    /**
     * Laskee ja palauttaa salin kokonaispaikkamäärän rivien ja paikkojen perusteella.
     * * @return Salin kokonaiskapasiteetti kokonaislukuna.
     */
    public int getKokonaiskapasiteetti() {
        return this.rivit * this.paikatPerRivi;
    }

    /**
     * Palauttaa salin nimen.
     * * @return Salin nimi merkkijonona.
     */
    public String getNimi() {
        return nimi;
    }

    /**
     * Palauttaa rivien määrän salissa.
     * * @return Rivien määrä.
     */
    public int getRivit() {
        return rivit;
    }

    /**
     * Palauttaa paikkojen määrän per rivi.
     * * @return Paikkojen määrä rivillä.
     */
    public int getPaikatPerRivi() {
        return paikatPerRivi;
    }

    /**
     * Palauttaa salin paikkamäärän. Metodi on hyödyllinen Naytos-luokan täyttöasteen tarkistamiseen.
     * * @return Sama kuin getKokonaiskapasiteetti.
     */
    public int getPaikat() {
        return getKokonaiskapasiteetti();
    }

    /**
     * Palauttaa salin tiedot merkkijonona.
     * * @return Salin nimi ja kapasiteetti muotoiltuna tekstinä.
     */
    @Override
    public String toString() {
        return "Sali: " + nimi + " (Kapasiteetti: " + getKokonaiskapasiteetti() + " paikkaa)";
    }
}
