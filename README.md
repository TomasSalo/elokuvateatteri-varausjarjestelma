# Elokuvateatterin varausjärjestelmä

Konsolipohjainen varausjärjestelmä elokuvateatterille. Tehty Turun yliopiston *Ohjelmoinnin harjoitustyö* -kurssilla keväällä 2026 parityönä.

## Mitä ohjelma tekee

**Asiakas**
- selaa tulevia näytöksiä taulukkona
- varaa paikan näytökseen ja saa varauskuitin
- järjestelmä tarkistaa automaattisesti ikärajan (S, K7, K12, K16, K18) ja salin vapaat paikat

**Ylläpitäjä** (kirjautuminen tunnuksella ja salasanalla)
- lisää ja poistaa elokuvia
- luo näytöksiä teatterin eri saleihin
- selaa ja poistaa varauksia

Kaikki tiedot tallentuvat paikalliseen SQLite-tietokantaan, joka luodaan automaattisesti ensimmäisellä käynnistyskerralla.

## Tekninen toteutus

| Kerros | Luokat | Vastuu |
|---|---|---|
| Käyttöliittymä | `Main` | Tekstikäyttöliittymä, syötteiden validointi ja virheenkäsittely |
| Logiikka | `JarjestelmanHallinta` | Liiketoimintasäännöt: ikärajat ja salin kapasiteetti |
| Tietokanta | `TietokantaHallinta` | SQLite-yhteys ja CRUD-operaatiot (JDBC, try-with-resources) |
| Tietomallit | `Elokuva`, `Sali`, `Naytos`, `Varaus`, `Istumapaikka`, `Herkku` | Teatterin resurssit |
| Käyttäjät | `Kayttaja` (abstrakti), `Asiakas`, `Yllapitaja` | Perintä ja tunnistautuminen |
| | `Ikaraja` (enum), `Tallennettava` (rajapinta) | |

**Teknologiat:** Java 17, Maven, SQLite JDBC, SLF4J, JUnit 5

## Käynnistys

```bash
git clone https://github.com/TomasSalo/elokuvateatteri-varausjarjestelma.git
cd elokuvateatteri-varausjarjestelma
mvn compile exec:java -Dexec.mainClass=Main
```

Tai avaa projekti IntelliJ IDEAssa, lataa Maven-riippuvuudet ja aja `Main.java`.

Ylläpitäjän oletustunnukset demokäyttöön: `admin` / `salasana123`.

Näytöksen aika syötetään muodossa `VVVV-KK-PPTHH:MM`, esim. `2026-05-17T18:30`.

## Dokumentaatio

- [Projektin dokumentaatio](docs/dokumentaatio.md)
- [Tekoälyn käyttö projektissa](docs/tekoalyn-kaytto.md)

## Tekijät

Toteutettu parityönä.

- Tomas Salo

