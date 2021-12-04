package de.othr.sw.TRBank.entity;

import javax.persistence.*;

@Embeddable
public class Adresse {
    private String strasse;
    private String hausnummer;
    private String stadt;
    private int plz;
    private String land;

    public Adresse() {

    }

    public Adresse(String strasse, String hausnummer, String stadt, int plz, String land) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.stadt = stadt;
        this.plz = plz;
        this.land = land;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "strasse='" + strasse + '\'' +
                ", hausnummer='" + hausnummer + '\'' +
                ", stadt='" + stadt + '\'' +
                ", plz=" + plz +
                ", land='" + land + '\'' +
                '}';
    }
}
