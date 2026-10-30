import java.util.*;

/**
 * Luokka edustaa yksittäisen asiakkaan tekemää varausta tiettyyn näytökseen.
 * Luokka toteuttaa Tallennettava-rajapinnan tietokantatoiminnallisuutta varten.
 */
public class Varaus implements Tallennettava {
    private Naytos naytos;
    private String asiakkaanNimi;
    private int asiakkaanIka;
    private Map<Herkku, Integer> tilatutHerkut;
    private List<Istumapaikka> varatutPaikat;

    /**
     * Luo uuden varaus-olion annetuilla tiedoilla.
     * * @param naytos Näytös, johon varaus kohdistuu.
     * @param asiakkaanNimi Varauksen tekevän asiakkaan nimi.
     * @param asiakkaanIka Asiakkaan ikä varaushetkellä ikärajatarkistusta varten.
     * @param tilatutHerkut Lista varaukseen liitetyistä oheistuotteista.
     * @param varatutPaikat Lista varatuista istumapaikoista.
     */
    public Varaus(Naytos naytos, String asiakkaanNimi, int asiakkaanIka,
                  Map<Herkku, Integer> tilatutHerkut, List<Istumapaikka> varatutPaikat) {
        this.naytos = naytos;
        this.asiakkaanNimi = asiakkaanNimi;
        this.asiakkaanIka = asiakkaanIka;
        this.tilatutHerkut = tilatutHerkut != null ? tilatutHerkut : new HashMap<Herkku, Integer>();
        this.varatutPaikat = varatutPaikat != null ? varatutPaikat : new ArrayList<>();
    }

    /**
     * Tarkistaa, täyttääkö asiakkaan ikä elokuvan ikärajavaatimuksen.
     * Hyödyntää Ikaraja-enumin sisältämää numeerista ikäarvoa.
     * * @return true, jos asiakkaan ikä on vähintään elokuvan ikärajan suuruinen, muuten false.
     */
    public boolean tarkistaIkaraja() {
        if (this.naytos == null || this.naytos.getElokuva() == null) {
            return false;
        }
        int vaadittuIka = this.naytos.getElokuva().getIkaraja().getVuodet();
        return this.asiakkaanIka >= vaadittuIka;
    }

    /**
     * Laskee varauksen kokonaishinnan sisältäen kaikki tilatut herkut.
     * * @return Varauksen kokonaissumma euroina.
     */
    public static double laskeYhteishinta(Map<Herkku, Integer> valitutHerkut) {
        double summa = 0;

        for (Map.Entry<Herkku, Integer> merkinta : valitutHerkut.entrySet()) {
            Herkku h = merkinta.getKey();
            int maara = merkinta.getValue();
            summa += h.getHinta() * maara;
        }
        return summa;
    }
    /**
     * Toteuttaa Tallennettava-rajapinnan metodin.
     * Tulostaa tällä hetkellä lokitiedon tallennuksesta (toteutus siirrettävissä SQL-luokkaan).
     */
    @Override
    public void tallennaTietokantaan() {
        System.out.println("Tallennetaan varaus tietokantaan asiakkaalle: " + asiakkaanNimi);
    }

    /**
     * Palauttaa varaukseen liittyvän näytöksen.
     * @return Naytos-olio.
     */
    public Naytos getNaytos() {
        return naytos;
    }

    /**
     * Palauttaa asiakkaan nimen.
     * @return Asiakkaan nimi merkkijonona.
     */
    public String getAsiakkaanNimi() {
        return asiakkaanNimi;
    }

    /**
     * Palauttaa listan varatuista istumapaikoista.
     * @return Lista Istumapaikka-olioita.
     */
    public List<Istumapaikka> getVaratutPaikat() {
        return varatutPaikat;
    }

    /**
     * Palauttaa listan tilatuista herkuista.
     * @return Lista Herkku-olioita.
     */
    public Map<Herkku, Integer> getTilatutHerkut() {
        return tilatutHerkut;
    }

    /**
     * Palauttaa varauksen tiedot selkeässä tekstimuodossa.
     * @return Varauksen tiedot merkkijonona.
     */
    @Override
    public String toString() {
        return "Varaus: " + asiakkaanNimi + ", Näytös: " + naytos.getElokuva().getNimi() +
                ", Paikkoja: " + varatutPaikat.size() + " kpl.";
    }
}