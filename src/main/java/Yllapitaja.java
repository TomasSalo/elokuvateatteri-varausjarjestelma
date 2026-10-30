/**
 * Edustaa järjestelmän ylläpitäjää.
 */
public class Yllapitaja extends Kayttaja {
    public Yllapitaja(String tunnus, String salasana) {
        super(tunnus, salasana);
    }

    @Override
    public String getRooli() {
        return "YLLAPITAJA";
    }
}