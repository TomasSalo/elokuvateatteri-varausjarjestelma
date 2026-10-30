import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Luokka vastaa SQLite-tietokantayhteyksistä ja järjestelmän SQL-kyselyistä.
 * Hoitaa tietojen tallentamisen, päivittämisen ja hakemisen.
 */
public class TietokantaHallinta {
    private static final String URL = "jdbc:sqlite:elokuvateatteri.db";

    /**
     * Alustaa tietokannan ja luo tarvittavat taulut, jos niitä ei ole olemassa.
     * Lisää myös oletussalit tietokantaan ohjelman ensimmäisellä suorituskerralla.
     */
    public static void alustaTietokanta() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS Elokuva (id INTEGER PRIMARY KEY, nimi TEXT, kesto INTEGER, ikaraja TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Sali (nimi TEXT PRIMARY KEY, rivit INTEGER, paikat_per_rivi INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Naytos (id INTEGER PRIMARY KEY, elokuva_id INTEGER, sali_nimi TEXT, aika TEXT, varatut INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Varaus (id INTEGER PRIMARY KEY, naytos_id INTEGER, asiakas TEXT )");
            stmt.execute("CREATE TABLE IF NOT EXISTS Herkku (id INTEGER PRIMARY KEY, nimi TEXT, hinta INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Varaus_Herkku (varaus_id INTEGER, herkku_id INTEGER, maara INTEGER NOT NULL, PRIMARY KEY (varaus_id, herkku_id), FOREIGN KEY (varaus_id) REFERENCES Varaus(id), FOREIGN KEY (herkku_id) REFERENCES Herkku(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS Varattu_Paikka (varaus_id INTEGER, naytos_id INTEGER, rivi INTEGER, paikka INTEGER, FOREIGN KEY (varaus_id) REFERENCES Varaus(id), FOREIGN KEY (naytos_id) REFERENCES Naytos(id))");


            stmt.execute("INSERT OR IGNORE INTO Sali VALUES('LUKSE', 5, 10)");
            stmt.execute("INSERT OR IGNORE INTO Sali VALUES('AI-SENSE', 10, 20)");

            stmt.execute("INSERT OR IGNORE INTO Herkku VALUES(1,'Popparit', 6.7 )");
            stmt.execute("INSERT OR IGNORE INTO Herkku VALUES(2,'Limu', 4 )");
            stmt.execute("INSERT OR IGNORE INTO Herkku VALUES(3,'Viinipullo', 10 )");
            stmt.execute("INSERT OR IGNORE INTO Herkku VALUES(4,'Viinapullo', 12 )");
            stmt.execute("INSERT OR IGNORE INTO Herkku VALUES(5,'Suolakurkut', 3.5 )");

            // Ylläpitäjätaulun luonti
            stmt.execute("CREATE TABLE IF NOT EXISTS Yllapitaja (tunnus TEXT PRIMARY KEY, salasana TEXT)");


            // Oletusylläpitäjän lisäys vain, jos taulu on tyhjä
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS lkm FROM Yllapitaja");
            if (rs.next() && rs.getInt("lkm") == 0) {
                stmt.execute("INSERT INTO Yllapitaja (tunnus, salasana) VALUES ('admin', 'salasana123')");
                System.out.println("Tietokanta alustettu: Luotu oletusylläpitäjä (admin/salasana123).");
            }

        } catch (SQLException e) {
            System.out.println("Tietokantavirhe alustuksessa: " + e.getMessage());
        }
    }

    /**
     * Hakee tietokannasta kaikki rekisteröidyt ylläpitäjät.
     * @return Lista tietokannasta löytyneistä Yllapitaja-olioista.
     */
    public List<Yllapitaja> haeYllapitajat() {
        List<Yllapitaja> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Yllapitaja")) {

            while (rs.next()) {
                lista.add(new Yllapitaja(rs.getString("tunnus"), rs.getString("salasana")));
            }
        } catch (SQLException e) {
            System.out.println("Virhe ylläpitäjien haussa: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lisää uuden elokuvan tietokantaan.
     * @param nimi Elokuvan nimi.
     * @param kesto Elokuvan kesto minuutteina.
     * @param raja Elokuvan ikäraja (esim. S, K7, K12, K16, K18).
     */
    public void lisaaElokuva(String nimi, int kesto, String raja) {
        String sql = "INSERT INTO Elokuva(nimi, kesto, ikaraja) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nimi);
            pstmt.setInt(2, kesto);
            pstmt.setString(3, raja);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Hakee kaikki tietokannassa olevat elokuvat.
     * @return Lista tietokannasta haetuista Elokuva-olioista.
     */
    public List<Elokuva> haeElokuvat() {
        List<Elokuva> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Elokuva")) {
            while (rs.next()) {
                lista.add(new Elokuva(rs.getInt("id"), rs.getString("nimi"), rs.getInt("kesto"), Ikaraja.valueOf(rs.getString("ikaraja"))));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /**
     * Hakee kaikki tietokannassa olevat näytökset yhdistäen elokuvan ja salin tiedot (SQL JOIN).
     * @return Lista tietokannasta haetuista Naytos-olioista.
     */
    public List<Naytos> haeNaytokset() {
        List<Naytos> lista = new ArrayList<>();
        String sql = "SELECT Naytos.*, Elokuva.nimi, Elokuva.kesto, Elokuva.ikaraja, Sali.rivit, Sali.paikat_per_rivi " +
                "FROM Naytos JOIN Elokuva ON Naytos.elokuva_id = Elokuva.id " +
                "JOIN Sali ON Naytos.sali_nimi = Sali.nimi";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Elokuva e = new Elokuva(rs.getInt("elokuva_id"), rs.getString("nimi"), rs.getInt("kesto"), Ikaraja.valueOf(rs.getString("ikaraja")));
                Sali s = new Sali(rs.getString("sali_nimi"), rs.getInt("rivit"), rs.getInt("paikat_per_rivi"));

                // LISÄÄ TÄMÄ LOHKO TÄHÄN:
                try {
                    String aikaString = rs.getString("aika");
                    LocalDateTime aika = LocalDateTime.parse(aikaString);
                    lista.add(new Naytos(rs.getInt("id"), e, s, aika, rs.getInt("varatut")));
                } catch (Exception e1) { // Käytetään yleistä Exceptionia varmuuden vuoksi
                    System.out.println("\n[TIETOKANTAVIRHE] Ohitettiin näytös ID " + rs.getInt("id") +
                            " viallisen päivämäärän takia: " + rs.getString("aika"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public List<Herkku> haeHerkut() {
        List<Herkku> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Herkku")) {
            while (rs.next()) {
                lista.add(new Herkku(rs.getInt("id"), rs.getString("nimi"), rs.getInt("hinta")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /**
     * Lisää uuden näytöksen tietokantaan.
     * @param elokuvaId Näytettävän elokuvan tietokanta-ID.
     * @param saliNimi Salin nimi, jossa elokuva esitetään.
     * @param aika Näytöksen alkamisaika muodossa YYYY-MM-DDTHH:MM.
     */
    public void lisaaNaytos(int elokuvaId, String saliNimi, String aika) {
        String sql = "INSERT INTO Naytos(elokuva_id, sali_nimi, aika, varatut) VALUES(?,?,?,0)";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, elokuvaId);
            pstmt.setString(2, saliNimi);
            pstmt.setString(3, aika);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Tallentaa uuden varauksen tietokantaan ja päivittää automaattisesti näytöksen varattujen paikkojen määrän.
     * @param naytosId Sen näytöksen tietokanta-ID, johon varaus tehdään.
     * @param asiakas Varauksen tekevän asiakkaan nimi tai tunnus.
     *
     */

   public void tallennaVaraus(int naytosId, String asiakas, Map<Herkku, Integer> valitutHerkut, List<Istumapaikka> valitutPaikat) {
       String varausSql = "INSERT INTO Varaus(naytos_id, asiakas) VALUES(?,?)";
       String herkkuSql = "INSERT INTO Varaus_Herkku(varaus_id, herkku_id, maara) VALUES(?,?,?)";
       String paikkaSql = "INSERT INTO Varattu_Paikka(varaus_id, naytos_id, rivi, paikka) VALUES(?,?,?,?)";
       String paivitysSql = "UPDATE Naytos SET varatut = varatut + ? WHERE id = ?";


       try (Connection conn = DriverManager.getConnection(URL)) {
           conn.setAutoCommit(false);

           try (PreparedStatement vStmt = conn.prepareStatement(varausSql, Statement.RETURN_GENERATED_KEYS)) {
               vStmt.setInt(1, naytosId);
               vStmt.setString(2, asiakas);
               vStmt.executeUpdate();

               // Haetaan juuri luodun varauksen ID
               try (ResultSet generatedKeys = vStmt.getGeneratedKeys()) {
                   if (generatedKeys.next()) {
                       int varausId = generatedKeys.getInt(1);

                       // Tallennetaan herkut uuteen liitostauluun erissä
                       try (PreparedStatement hStmt = conn.prepareStatement(herkkuSql)) {
                           for (Map.Entry<Herkku, Integer> entry : valitutHerkut.entrySet()) {
                               hStmt.setInt(1, varausId);
                               hStmt.setInt(2, entry.getKey().getId());
                               hStmt.setInt(3, entry.getValue());
                               hStmt.addBatch();
                           }
                           hStmt.executeBatch();
                       }
                       // Tallennetaan istumapaikat
                       try (PreparedStatement pStmt = conn.prepareStatement(paikkaSql)) {
                           for (Istumapaikka ip : valitutPaikat) {
                               pStmt.setInt(1, varausId);
                               pStmt.setInt(2, naytosId);
                               pStmt.setInt(3, ip.getRivi());
                               pStmt.setInt(4, ip.getPaikka());
                               pStmt.addBatch();
                           }
                           pStmt.executeBatch();
                       }
                   }
               }

               try (PreparedStatement nStmt = conn.prepareStatement(paivitysSql)) {
                   nStmt.setInt(1, valitutPaikat.size()); // Kasvatetaan varattujen määrää
                   nStmt.setInt(2, naytosId);
                   nStmt.executeUpdate();
               }

               // Jos kaikki onnistui ilman virheitä, vahvistetaan muutokset tietokantaan
               conn.commit();
               System.out.println("Varaus, herkut ja istumapaikat tallennettu onnistuneesti.");

           } catch (SQLException e) {
               // Jos jokin meni vikaan, perutaan kaikki tämän transaktion muutokset
               conn.rollback();
               System.err.println("Virhe varauksen tallennuksessa, muutokset peruttu: " + e.getMessage());
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

    /**
     * Hakee kaikki tehdyt varaukset tietokannasta ylläpitäjän tarkasteltavaksi.
     * @return Lista tehdyistä varauksista selkeänä merkkijonona.
     */
    public List<String> haeVaraukset() {
        List<String> varaukset = new ArrayList<>();
        String sql = "SELECT Varaus.id, Varaus.asiakas, Naytos.aika, Elokuva.nimi " +
                "FROM Varaus " +
                "JOIN Naytos ON Varaus.naytos_id = Naytos.id " +
                "JOIN Elokuva ON Naytos.elokuva_id = Elokuva.id";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String tieto = String.format("Varaus ID: %d | Asiakas: %s | Elokuva: %s | Aika: %s",
                        rs.getInt("id"), rs.getString("asiakas"), rs.getString("nimi"), rs.getString("aika"));
                varaukset.add(tieto);
            }
        } catch (SQLException e) {
            System.out.println("Virhe varausten haussa: " + e.getMessage());
        }
        return varaukset;
    }
    /**
     * Poistaa elokuvan tietokannasta ID:n perusteella.
     * Poistaa automaattisesti myös kaikki elokuvaan liittyvät näytökset.
     * @param id Poistettavan elokuvan ID.
     */
    public void poistaElokuva(int id) {
        String sqlNaytokset = "DELETE FROM Naytos WHERE elokuva_id = ?";
        String sqlElokuva = "DELETE FROM Elokuva WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL)) {
            // 1. Poistetaan ensin elokuvaan liittyvät näytökset
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlNaytokset)) {
                pstmt1.setInt(1, id);
                pstmt1.executeUpdate();
            }
            // 2. Poistetaan itse elokuva
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlElokuva)) {
                pstmt2.setInt(1, id);
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Virhe elokuvan poistossa: " + e.getMessage());
        }
    }

    /**
     * Poistaa näytöksen tietokannasta ID:n perusteella.
     * Poistaa samalla kaikki näytökseen tehdyt varaukset.
     * @param id Poistettavan näytöksen ID.
     */
    public void poistaNaytos(int id) {
        String sqlVaraukset = "DELETE FROM Varaus WHERE naytos_id = ?";
        String sqlNaytos = "DELETE FROM Naytos WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL)) {
            // 1. Poistetaan näytökseen liittyvät varaukset
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlVaraukset)) {
                pstmt1.setInt(1, id);
                pstmt1.executeUpdate();
            }
            // 2. Poistetaan itse näytös
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlNaytos)) {
                pstmt2.setInt(1, id);
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Virhe näytöksen poistossa: " + e.getMessage());
        }
    }

    public List<Istumapaikka> haeVaratutPaikat(int naytosId) {
        List<Istumapaikka> varatut = new ArrayList<>();
        String sql = "SELECT rivi, paikka FROM Varattu_Paikka WHERE naytos_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, naytosId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    varatut.add(new Istumapaikka(rs.getInt("rivi"), rs.getInt("paikka")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varatut;
    }
}
