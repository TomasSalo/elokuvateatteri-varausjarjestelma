import java.util.Objects;

/**
 * Edustaa myytävää tuotetta (esim. popcorn, limu).
 */
public class Herkku {
    private int id;
    private String nimi;
    private double hinta;

    /**
     * Luo uuden herkun.
     *
     * @param nimi  Tuotteen nimi.
     * @param hinta Tuotteen hinta.
     */

    public Herkku(int id, String nimi, double hinta) {
        this.id = id;
        this.nimi = nimi;
        this.hinta = hinta;
    }

    /**
     * @return Herkun nimi.
     */
    public String getNimi() {
        return nimi;
    }

    /**
     * @return Herkun hinta.
     */
    public double getHinta() {
        return hinta;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Herkku{" +
                "id=" + id +
                ", nimi='" + nimi + '\'' +
                ", hinta=" + hinta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        // Jos kyseessä on täsmälleen sama olio, ne ovat samat
        if (this == o) return true;
        // Jos verrattava olio on tyhjä tai eri luokkaa, ne eivät ole samat
        if (o == null || getClass() != o.getClass()) return false;

        // Muutetaan verrattava olio Herkku-tyyppiseksi ja verrataan ID
        Herkku herkku = (Herkku) o;
        return id == herkku.getId();
    }

    @Override
    public int hashCode() {
        // Luodaan tiiviste ID:n perusteella
        return Objects.hash(id);
    }
}
