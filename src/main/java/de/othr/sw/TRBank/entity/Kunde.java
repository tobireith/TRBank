package de.othr.sw.TRBank.entity;

import de.othr.sw.TRBank.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
public class Kunde extends SingleIdEntity<Long> {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long kundeId;
    @Embedded
    private Adresse adresse;
    @OneToMany(mappedBy = "besitzer")
    private Collection<Konto> konten;
    @Column(unique = true)
    private String username;
    private String passwort;
    private String vorname;
    private String nachname;
    private boolean firmenkunde;

    public Kunde() {

    }

    public Kunde(String username, String passwort, Adresse adresse, String vorname, String nachname, boolean firmenkunde) {
        this.adresse = adresse;
        this.vorname = vorname;
        this.nachname = nachname;
        this.firmenkunde = firmenkunde;
        this.username = username;
        this.passwort = passwort;
    }

    public long getKundeId() {
        return kundeId;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Collection<Konto> getKonten() {
        return Collections.unmodifiableCollection(konten);
    }

    public boolean isFirmenkunde() {
        return firmenkunde;
    }

    public void setFirmenkunde(boolean firmenkunde) {
        this.firmenkunde = firmenkunde;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    @Override
    public String toString() {
        return "Kunde{" +
                ", adresse=" + adresse +
                ", konten=" + konten +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", firmenkunde=" + firmenkunde +
                ", username='" + username + '\'' +
                ", passwort='" + passwort + '\'' +
                '}';
    }

    @Override
    public Long getID() {
        return this.kundeId;
    }
}
