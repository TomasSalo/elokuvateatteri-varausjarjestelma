import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

/**
 * Ohjelman pääluokka, joka sisältää tekstipohjaisen käyttöliittymän
 * asiakkaille ja ylläpitäjille. Luokka koordinoi varaustoimintoja,
 * hallinnoi ylläpitäjän kirjautumista ja huolehtii virheettömästä syötteiden luvusta.
 */
public class Main {
    private static Scanner lukija = new Scanner(System.in);
    private static TietokantaHallinta db = new TietokantaHallinta();
    private static JarjestelmanHallinta hallinta = new JarjestelmanHallinta();

    // Suomalainen päivämäärä- ja aikamuotoilu (esim. 24.12.2025 klo 18:00)
    private static final DateTimeFormatter PVM_MUOTOILU = DateTimeFormatter.ofPattern("dd.MM.yyyy 'klo' HH:mm");

    /**
     * Ohjelman päämetodi. Alustaa tietokannan ja pyörittää päävalikkoa.
     *
     * @param args Komentoriviparametrit.
     */
    public static void main(String[] args) {
        TietokantaHallinta.alustaTietokanta();

        while (true) {
            System.out.println("\n========================================");
            System.out.println("   ELOKUVATEATTERI VARAUSJÄRJESTELMÄ");
            System.out.println("========================================");
            System.out.println("1. Asiakasvalikko (Varaa lippuja)");
            System.out.println("2. Ylläpitäjävalikko (Hallitse järjestelmää)");
            System.out.println("0. Lopeta ohjelma");

            int valinta = lueKokonaisluku("Valitse toiminto (0-2): ");

            if (valinta == 0) {
                System.out.println("Kiitos järjestelmän käytöstä. Hei hei!");
                break;
            } else if (valinta == 1) {
                asiakasValikko();
            } else if (valinta == 2) {
                if (kirjauduYllapitajana()) {
                    yllapitajaValikko();
                }
            } else {
                System.out.println("Virheellinen valinta. Syötä numero 0-2.");
            }
        }
    }

    /**
     * Kysyy ylläpitäjän tunnistetiedot ja vertaa niitä tietokannassa oleviin käyttäjiin.
     * Hyödyntää Kayttaja-luokan tarkistaTunnukset-metodia.
     *
     * @return true, jos kirjautuminen onnistui.
     */
    private static boolean kirjauduYllapitajana() {
        System.out.println("\n--- YLLÄPITÄJÄN KIRJAUTUMINEN ---");
        System.out.print("Käyttäjätunnus: ");
        String tunnus = lukija.nextLine().trim();
        System.out.print("Salasana: ");
        String salasana = lukija.nextLine().trim();

        // Haetaan lista ylläpitäjistä tietokannasta, ei koodista!
        List<Yllapitaja> tietokannanYllapitajat = db.haeYllapitajat();

        for (Yllapitaja y : tietokannanYllapitajat) {
            if (y.tarkistaTunnukset(tunnus, salasana)) {
                System.out.println("Kirjautuminen onnistui. Tervetuloa, " + y.getTunnus() + "!");
                return true;
            }
        }

        System.out.println("VIRHE: Väärä tunnus tai salasana. Pääsy evätty.");
        return false;
    }

    /**
     * Lukee käyttäjältä kokonaisluvun ja varmistaa try-catch -rakenteella,
     * ettei ohjelma kaadu virheelliseen syötteeseen.
     *
     * @param kehote Käyttäjälle näytettävä ohjeteksti.
     * @return Käyttäjän syöttämä numero.
     */
    private static int lueKokonaisluku(String kehote) {
        while (true) {
            try {
                System.out.print(kehote);
                return Integer.parseInt(lukija.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("HUOM: Syötä vain numeroita ilman välimerkkejä.");
            }
        }
    }
    /**
     * Lukee käyttäjältä ajan ja varmistaa, ettei ohjelma kaadu virheelliseen syötteeseen.
     * @param kehote Käyttäjälle näytettävä teksti.
     * @return Oikein muotoiltu päivämäärä merkkijonona.
     */
    private static String lueAika(String kehote) {
        while (true) {
            System.out.print(kehote);
            String syote = lukija.nextLine().trim();
            try {
                LocalDateTime.parse(syote);
                return syote; // Palautetaan kelvollinen syöte tietokantaa varten
            } catch (Exception e) {
                System.out.println("VIRHE: Väärä päivämäärämuoto. Käytä muotoa YYYY-MM-DDTHH:MM (esim. 2026-05-20T18:00)");
            }
        }
    }

    /**
     * Ylläpitäjän hallintavalikko. Mahdollistaa tietojen lisäämisen, selaamisen ja poistamisen.
     */
    private static void yllapitajaValikko() {
        while (true) {
            System.out.println("\n--- YLLÄPITÄJÄVALIKKO ---");
            System.out.println("1. Lisää elokuva");
            System.out.println("2. Lisää näytös");
            System.out.println("3. Selaa herkkuja");
            System.out.println("4. Selaa elokuvia");
            System.out.println("5. Selaa varauksia");
            System.out.println("6. Poista elokuva");
            System.out.println("7. Poista näytös");
            System.out.println("0. Kirjaudu ulos");

            int v = lueKokonaisluku("Valitse toiminto (0-6): ");

            if (v == 0) break;

            switch (v) {
                case 1 -> {
                    System.out.println("\n[LISÄÄ ELOKUVA]");
                    System.out.print("Nimi: ");
                    String n = lukija.nextLine();
                    int kesto = lueKokonaisluku("Kesto minuutteina (esim. 120): ");
                    System.out.print("Ikäraja (S, K7, K12, K16, K18): ");
                    String r = lukija.nextLine().toUpperCase().trim();
                    db.lisaaElokuva(n, kesto, r);
                    System.out.println("Elokuva lisätty.");
                }
                case 2 -> {
                    System.out.println("\n[LISÄÄ NÄYTÖS]");
                    List<Elokuva> elokuvat = db.haeElokuvat();
                    if (elokuvat.isEmpty()) {
                        System.out.println("Lisää ensin elokuva.");
                        break;
                    }
                    elokuvat.forEach(System.out::println);
                    int eid = lueKokonaisluku("Syötä elokuvan ID: ");
                    System.out.print("Sali (esim. LUKSE): ");
                    String sali = lukija.nextLine();
                    String aika = lueAika("Aika (esim. 2026-05-20T18:00): ");
                    db.lisaaNaytos(eid, sali, aika);
                    System.out.println("Näytös lisätty.");
                }
                case 3 -> db.haeHerkut().forEach(System.out::println);
                case 4 -> db.haeElokuvat().forEach(System.out::println);
                case 5 -> db.haeVaraukset().forEach(System.out::println);
                case 6 -> {
                    System.out.println("\n[POISTA ELOKUVA]");
                    db.haeElokuvat().forEach(System.out::println);
                    int eid = lueKokonaisluku("Syötä poistettavan elokuvan ID: ");
                    db.poistaElokuva(eid);
                    System.out.println("Poistettu.");
                }
                case 7 -> {
                    System.out.println("\n[POISTA NÄYTÖS]");
                    db.haeNaytokset().forEach(n -> System.out.println("ID: " + n.getId() + " | " + n.getElokuva().getNimi()));
                    int nid = lueKokonaisluku("Syötä poistettavan näytöksen ID: ");
                    db.poistaNaytos(nid);
                    System.out.println("Poistettu.");
                }
                default -> System.out.println("Virheellinen valinta.");
            }
        }
    }

    /**
     * Asiakkaan valikko. Käsittelee varausten teon ja ikärajatarkistukset.
     */
    private static void asiakasValikko() {
        System.out.println("\n--- ASIAKASVALIKKO ---");
        int ika = lueKokonaisluku("Syötä ikäsi numeroina: ");
        Asiakas asiakas = new Asiakas("Asiakas", "---", ika);

        List<Naytos> naytokset = db.haeNaytokset();
        if (naytokset.isEmpty()) {
            System.out.println("Ei tulevia näytöksiä.");
            return;
        }

        System.out.println("\nOHJELMISTO:");
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-25s | %-8s | %-15s | %-20s | %-10s\n", "NRO", "ELOKUVA", "IKÄRAJA", "SALI", "AIKA", "TILANNE");
        System.out.println("---------------------------------------------------------------------------------------------------");

        for (int i = 0; i < naytokset.size(); i++) {
            Naytos n = naytokset.get(i);
            String tila = (n.getSali().getPaikat() - n.getVaratutPaikat()) + " vapaana";

            System.out.printf("%-4d | %-25s | %-8s | %-15s | %-20s | %-10s\n",
                    i,
                    n.getElokuva().getNimi(),
                    n.getElokuva().getIkaraja(),
                    n.getSali().getNimi(),
                    n.getAika().format(PVM_MUOTOILU),
                    tila);
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        int valinta = lueKokonaisluku("Valitse näytöksen numero tai -1 peruaksesi: ");

        if (valinta >= 0 && valinta < naytokset.size()) {
            Naytos valittu = naytokset.get(valinta);
            List<Istumapaikka> valitutPaikat = new ArrayList<>();
            List<Istumapaikka> joVaratut = db.haeVaratutPaikat(valittu.getId());
            Map<Herkku, Integer> valitutHerkut = new HashMap<>();

            int tarvittavatPaikat = lueKokonaisluku("Kuinka monta lippua haluat varata?: ");

            for (int i = 0; i < tarvittavatPaikat; i++) {
                tulostaSali(valittu.getSali(), joVaratut);
                System.out.println("\nValitse paikka lipulle " + (i + 1) + "/" + tarvittavatPaikat);

                while (true) {
                    int rivi = lueKokonaisluku("Anna rivi: ");
                    int paikka = lueKokonaisluku("Anna paikka: ");

                    // Tarkistetaan onko paikka salin rajojen sisällä
                    if (rivi < 1 || rivi > valittu.getSali().getRivit() || paikka < 1 || paikka > valittu.getSali().getPaikatPerRivi()) {
                        System.out.println("Virhe: Paikkaa ei ole olemassa. Yritä uudelleen.");
                        continue;
                    }

                    // Tarkistetaan onko paikka jo varattu
                    boolean vapaa = true;
                    for (Istumapaikka ip : joVaratut) {
                        if (ip.getRivi() == rivi && ip.getPaikka() == paikka) { vapaa = false; break; }
                    }
                    for (Istumapaikka ip : valitutPaikat) {
                        if (ip.getRivi() == rivi && ip.getPaikka() == paikka) { vapaa = false; break; }
                    }

                    if (!vapaa) {
                        System.out.println("Virhe: Paikka on jo varattu. Valitse toinen.");
                    } else {
                        valitutPaikat.add(new Istumapaikka(rivi, paikka));
                        System.out.println("Paikka Rivi " + rivi + ", Paikka " + paikka + " valittu!");
                        break;
                    }
                }
            }
            System.out.println("\n========================================");
            System.out.println("  ELOKUVAAN HERKKUJA?");
            System.out.println("1. Selaa herkkuja" );
            System.out.println("2. Ei kiitos" );

            int v = lueKokonaisluku("Valitse toiminto: ");
            if (v == 1) {
                List<Herkku> herkut = db.haeHerkut();
                while (true) {
                    System.out.println("\n--- SAATAVILLA OLEVAT HERKUT ---");
                    for (int i = 0; i < herkut.size(); i++) {
                        System.out.println((i + 1) + ". " + herkut.get(i).getNimi() + " (" + herkut.get(i).getHinta() + "€)");
                    }
                    System.out.println("0. Olen valmis herkkujen osalta (Palaa tallentamaan varaus)");

                    int herkkuValinta = lueKokonaisluku("Minkä herkun haluat varata? (numero): ");

                    if (herkkuValinta > 0 && herkkuValinta <= herkut.size()) {
                        Herkku valittuHerkku = herkut.get(herkkuValinta - 1);
                        int maara = lueKokonaisluku("Kuinka monta kappaletta haluat?: ");

                        if (maara > 0) {
                            // (Jos samaa herkkua on jo lisätty, kasvatetaan sen määrää)
                            valitutHerkut.put(valittuHerkku, valitutHerkut.getOrDefault(valittuHerkku, 0) + maara);
                            System.out.println("=> Hienoa! " + maara + " kpl " + valittuHerkku.getNimi() + " lisättiin tilauslistalle.");
                        } else {
                            System.out.println("Määrän täytyy olla nollaa suurempi.");
                        }
                    } else if (herkkuValinta == 0) {
                        System.out.println("Herkkujen valinta päätetty.");
                        break;
                    } else {
                        System.out.println("Virheellinen valinta, yritä uudelleen.");
                    }
                }
            }


            if (hallinta.suoritaVaraus(asiakas, valittu, valitutHerkut, valitutPaikat)) {
                System.out.println("\n========================================");
                System.out.println("   VARAUS ONNISTUI!");
                System.out.println("========================================");
                System.out.println("Elokuva:    " + valittu.getElokuva().getNimi());
                System.out.println("Aika:       " + valittu.getAika().format(PVM_MUOTOILU));
                System.out.println("Sali:       " + valittu.getSali().getNimi());
                System.out.println("Paikkoja:   " + valitutPaikat.size() + " kpl");

                double yhteishinta = 0.0;
                yhteishinta += valitutPaikat.size() * 12.0;



                // Tulostetaan vielä kuitiksi valitut herkut
                if(!valitutHerkut.isEmpty()) {
                    System.out.println("\nHerkut:");
                    for(Map.Entry<Herkku, Integer> entry : valitutHerkut.entrySet()) {
                        Herkku h = entry.getKey();
                        int maara = entry.getValue();
                        double rivinHinta = h.getHinta() * maara;
                        yhteishinta += rivinHinta;

                        System.out.println("  - " + maara + " kpl " + h.getNimi() + " (" + rivinHinta + " €)");
                    }
                }
                System.out.println("----------------------------------------");
                System.out.println("YHTEENSÄ:   " + yhteishinta + " €");
                System.out.println("========================================\n");
            }
        }


    }

    /**
     * Tulosta salin kartan paikanvarauksen yhteydessä
     * @param sali
     * @param varatutPaikat
     */
    private static void tulostaSali(Sali sali, List<Istumapaikka> varatutPaikat) {
        System.out.println("\n[ VALKOKANGAS ]");
        for (int r = 1; r <= sali.getRivit(); r++) {
            System.out.printf("Rivi %2d: ", r);
            for (int p = 1; p <= sali.getPaikatPerRivi(); p++) {
                boolean onVarattu = false;
                // Tarkistetaan onko tämä kyseinen tuoli varattujen listalla
                for (Istumapaikka varattu : varatutPaikat) {
                    if (varattu.getRivi() == r && varattu.getPaikka() == p) {
                        onVarattu = true;
                        break;
                    }
                }
                if (onVarattu) {
                    System.out.print("[X] "); // Varattu
                } else {
                    System.out.print("[ ] "); // Vapaa
                }
            }
            System.out.println();
        }
    }
}