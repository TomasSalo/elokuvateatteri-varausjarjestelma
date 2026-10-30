import java.time.LocalDateTime;

/**
 * Edustaa tiettyä elokuvaesitystä tietyssä salissa.
 */
public class Naytos {
    private int id;
    private Elokuva elokuva;
    private Sali sali;
    private LocalDateTime aika;
    private int varatutPaikat;
    /**
     * Luo uuden näytöksen.
     * @param id Näytöksen ID.
     * @param elokuva Esitettävä elokuva.
     * @param sali Sali, jossa elokuva esitetään.
     * @param aika Alkamisaika.
     * @param varatutPaikat Jo varattujen paikkojen määrä.
     */
    public Naytos(int id, Elokuva elokuva, Sali sali, LocalDateTime aika, int varatutPaikat) {
        this.id = id;
        this.elokuva = elokuva;
        this.sali = sali;
        this.aika = aika;
        this.varatutPaikat = varatutPaikat;
    }

    public int getId() { return id; }
    public Elokuva getElokuva() { return elokuva; }
    public Sali getSali() { return sali; }
    public LocalDateTime getAika() { return aika; }
    public int getVaratutPaikat() { return varatutPaikat; }

    /**
     * Tarkistaa, onko salissa tilaa.
     * @return true, jos vapaita paikkoja on.
     */
    public boolean onkoTilaa() { return varatutPaikat < sali.getPaikat(); }

    /**
     * Kasvattaa varattujen paikkojen määrää yhdellä.
     */
    public void varaaPaikka() { if (onkoTilaa()) varatutPaikat++; }

    @Override
    public String toString() {
        return "Naytos{" +
                "id=" + id +
                ", elokuva=" + elokuva +
                ", sali=" + sali +
                ", aika=" + aika +
                ", varatutPaikat=" + varatutPaikat +
                '}';
    }
}
