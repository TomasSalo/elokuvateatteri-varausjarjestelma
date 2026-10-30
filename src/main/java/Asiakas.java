/**
 * Luokka mallintaa Asiakasta, joka perii Käyttäjä-luokan
 */
public class Asiakas extends Kayttaja {
    private int ika;

    public Asiakas(String tunnus, String salasana, int ika) {
        super(tunnus, salasana);
        this.ika = ika;
    }

    public int getIka() { return ika; }
    @Override
    public String getRooli() {
        return "ASIAKAS"; }
}
