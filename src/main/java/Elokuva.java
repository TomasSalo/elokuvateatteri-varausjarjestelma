/**
 * Edustaa elokuvateatterissa esitettävää elokuvaa.
 */
public class Elokuva {
    private int id;
    private String nimi;
    private int kesto;
    private Ikaraja ikaraja;

    /**
     * Luo uuden elokuvan.
     * @param id Elokuvan yksilöllinen tunniste.
     * @param nimi Elokuvan nimi.
     * @param kesto Elokuvan kesto minuutteina.
     * @param ikaraja Elokuvan ikäraja-luokitus.
     */

    public Elokuva(int id, String nimi, int kesto, Ikaraja ikaraja) {
        this.id = id;
        this.nimi = nimi;
        this.kesto = kesto;
        this.ikaraja = ikaraja;
    }

    public int getId() {
        return id;
    }
    public String getNimi() {
        return nimi;
    }
    public Ikaraja getIkaraja() {
        return ikaraja;
    }

    @Override
    public String toString() {
        return "Elokuva{" +
                "id=" + id +
                ", nimi='" + nimi + '\'' +
                ", kesto=" + kesto +
                ", ikaraja=" + ikaraja +
                '}';
    }
}
