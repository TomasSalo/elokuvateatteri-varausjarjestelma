1. Ohjelman yleinen kuvaus
   Tämä sovellus on elokuvateatterin varausjärjestelmä, joka on jaettu asiakkaan ja ylläpitäjän toimintoihin. Asiakkaat voivat tarkastella elokuvateatterin ohjelmistoa ja varata paikkoja haluamiinsa näytöksiin selkeän tekstipohjaisen käyttöliittymän kautta. Järjestelmä valvoo automaattisesti, että salin istumapaikkakapasiteetti ei ylity ja että asiakkaan ikä riittää valitun elokuvan ikärajaan (esim. K16).

Ylläpitäjäpuoli on suojattu käyttäjätunnuksella ja salasanalla. Kirjautuneena ylläpitäjä tai teatterin työntekijä voi lisätä tietokantaan uusia elokuvia, luoda niille esitysaikoja teatterin eri saleihin sekä selailla tai poistaa tehtyjä varauksia, näytöksiä ja elokuvia.

2. Kuvaus ohjelman toteutuksesta
   Ohjelma on toteutettu olioparadigman mukaisesti Javalla, noudattaen virallista Maven-projektirakennetta (src/main/java ja src/test/java). Järjestelmä on jaettu seuraaviin loogisiin kokonaisuuksiin vastuualueiden erottamiseksi:

Käyttöliittymä (Main): Vastaa konsolipohjaisesta tekstikäyttöliittymästä, syötteiden lukemisesta ja ohjelman vikasietoisuudesta (esim. virheellisten kirjain- tai numerosyötteiden hallinta try-catch-rakenteilla).

Tietokantakerros (TietokantaHallinta): Vastaa kaikesta SQLite-tietokannan kanssa kommunikoinnista (CRUD-operaatiot: lisäys, haku, poisto, päivitys). Tietokantayhteydet on suojattu automaattisella resurssien vapautuksella (try-with-resources).

Logiikkakerros (JarjestelmanHallinta): Koordinoi varausprosessia ja tarkistaa liiketoimintasäännöt (ikäräjatarkistukset ja tilan riittävyys salissa) ennen kuin antaa luvan tietokantamuutoksille.

Tietomallit (Olioluokat): * Elokuva, Sali, Naytos ja Varaus mallintavat teatterin fyysisiä ja ajallisia resursseja.

Perintää hyödynnetään käyttäjien hallinnassa: Abstrakti yläluokka Kayttaja määrittää yhteiset ominaisuudet ja tunnistautumislogiikan. Sen perivät alaluokat Asiakas ja Yllapitaja.

Ikärajat on toteutettu Ikaraja Enum-luokalla, joka sitoo ikärajanumeron (esim. 16) suoraan S, K7, K12, K16 ja K18 -merkintöihin.

3. Ulkoisten kirjastojen käyttö
   Ohjelmassa on käytetty Javan vakiokirjastojen ja JUnit 5 -testaustyökalun lisäksi seuraavia Mavenilla (pom.xml) hallinnoitavia ulkoisia kirjastoja:

SQLite JDBC Driver: Mahdollistaa Javan yhdistämisen lokaaliin SQLite-tietokantaan. Järjestelmä käyttää tätä kaiken pysyvän datan tallentamiseen. Ohjelman käynnistyessä tietokanta-ajuri luo automaattisesti elokuvateatteri.db-tiedoston juurihakemistoon sekä alustaa tarvittavat taulut ja oletusylläpitäjän tunnukset.

SLF4J API & Simple: SQLite-ajuri käyttää taustallaan lokitusjärjestelmää, joka oletuksena tulostaa konsoliin virheilmoituksia lokittimen puuttumisesta. SLF4J on lisätty projektiin näiden tarpeettomien varoitusten vaimentamiseksi, jotta asiakkaalle näkyvä tekstikäyttöliittymä pysyy siistinä ja selkeänä.

4. Ohjeet ohjelman käynnistämiseen 
Kloonaa repositorio omalle koneellesi.

Avaa projekti valitsemassasi IDE:ssä (esim. IntelliJ IDEA).

Varmista, että ohjelmointiympäristö lataa Maven-riippuvuudet pom.xml-tiedostosta (IntelliJ:ssä "Load Maven Changes" tai oikean reunan valikosta Maven -> Reload).

Ohjelman käynnistyspiste on src/main/java/Main.java -luokassa.

5. Ohjeet ohjelman käyttämiseen
   Ohjelmaa käytetään syöttämällä numerovalintoja konsoliin. Käyttöliittymä ohjeistaa käyttäjää aktiivisesti sallituista syötemuodoista.

Asiakkaat (Valinta 1): * Ohjelma kysyy ensin asiakkaan iän ja listaa sen jälkeen kaikki tulevat näytökset siistinä taulukkona.

Varauksen tekeminen onnistuu syöttämällä halutun näytöksen numeron. Jos varaus onnistuu, ohjelma tulostaa asiakkaalle varauskuitin.

Ylläpitäjät (Valinta 2): * Kirjautuminen tapahtuu oletustunnuksilla:

Käyttäjätunnus: admin

Salasana: salasana123

Ylläpitäjävalikosta voi lisätä uusia elokuvia, luoda niille näytöksiä, selata tehtyjä varauksia sekä poistaa vanhoja elokuvia ja näytöksiä.

Huom: Näytöksiä ei voi luoda ennen kuin järjestelmään on lisätty vähintään yksi elokuva. Näytöksen aikamuotoilu on annettava tarkasti muodossa VVVV-KK-PPTHH:MM (esim. 2026-05-17T18:30).
