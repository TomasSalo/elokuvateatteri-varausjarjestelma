import java.util.List;
import java.util.Map;

/**
 * Järjestelmän hallintaluokka, joka koordinoi kokonaisuutta.
 */
public class JarjestelmanHallinta {
    private TietokantaHallinta db = new TietokantaHallinta();

    /**
     * Suorittaa varauksen, jos asiakkaan ikä ja näytöksen tilanne sen sallivat.
     * Tulostaa virheilmoituksen, mikäli ehdot eivät täyty.
     * @param asiakas Varausta yrittävä asiakas-olio.
     * @param naytos Näytös-olio, johon asiakas haluaa varata paikan.
     * @return true, jos varaus onnistui ja tallennettiin tietokantaan, muuten false.
     */
    public boolean suoritaVaraus(Asiakas asiakas, Naytos naytos, Map<Herkku, Integer> valitutHerkut, List<Istumapaikka> valitutPaikat) {
        if (asiakas.getIka() < naytos.getElokuva().getIkaraja().getVuodet()) {
            System.out.println("VIRHE: Ikäsi ei riitä (Vaadittu: " + naytos.getElokuva().getIkaraja() + ").");
            return false;
        }
        if (valitutPaikat.isEmpty()) {
            System.out.println("VIRHE: Yhtään paikkaa ei ole valittu.");
            return false;
        }
        if (!naytos.onkoTilaa()) {
            System.out.println("VIRHE: Näytös on täynnä.");
            return false;
        }

        db.tallennaVaraus(naytos.getId(), asiakas.getTunnus(), valitutHerkut, valitutPaikat);
        return true;
    }


}
