/**
 * Abstrakti yläluokka kaikille järjestelmän käyttäjille.
 */
public abstract class Kayttaja {
    protected String tunnus;
    protected String salasana;

    public Kayttaja(String tunnus, String salasana) {
        this.tunnus = tunnus;
        this.salasana = salasana;
    }

    /**
     * Tarkistaa, täsmäävätkö annetut tunnukset olion tietoihin.
     * @param tunnus Testattava käyttäjätunnus.
     * @param salasana Testattava salasana.
     * @return true, jos molemmat täsmäävät, muuten false.
     */
    public boolean tarkistaTunnukset(String tunnus, String salasana) {
        return this.tunnus.equals(tunnus) && this.salasana.equals(salasana);
    }

    public String getTunnus() {
        return tunnus;
    }

    public abstract String getRooli();
}