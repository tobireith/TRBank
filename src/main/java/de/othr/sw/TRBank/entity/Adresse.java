package de.othr.sw.TRBank.entity;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;

@Embeddable
public class Adresse {
    @NotBlank
    private String strasse;
    @NotBlank
    @Length (min = 1, max = 20)
    private String hausnummer;
    @NotBlank
    private String stadt;
    @NotBlank
    @Length (min = 5, max = 5)
    private String plz;
    @NotBlank
    @Length(min = 2, max = 50)
    private String land;

    public Adresse() {

    }

    public Adresse(String strasse, String hausnummer, String stadt, String plz, String land) {
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

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
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
