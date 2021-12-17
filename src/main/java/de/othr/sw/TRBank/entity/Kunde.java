package de.othr.sw.TRBank.entity;

import de.othr.sw.TRBank.entity.util.SingleIdEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class Kunde extends SingleIdEntity<Long> implements UserDetails {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long kundeId;
    @Embedded
    private Adresse adresse;
    @OneToMany(mappedBy = "besitzer")
    private List<Konto> konten = new ArrayList<>();
    @Column(unique = true)
    private String username;
    private String passwort;
    private String vorname;
    private String nachname;
    private boolean firmenkunde;

    public Kunde() {

    }

    public Kunde(String username, String passwort) {
        this.username = username;
        this.passwort = passwort;
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

    public List<Konto> getKonten() {
        return Collections.unmodifiableList(konten);
    }

    public void setKonten(List<Konto> konten) {
        this.konten = konten;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //TODO! (Firmenkunde & normaler Benutzer)
        return List.of((GrantedAuthority) () -> {
            if(isFirmenkunde()) {
                return "BUSINESS";
            }
            else {
                return "STANDARD";
            }
        });
    }

    @Override
    public String getPassword() {
        return passwort;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
                "kundeId=" + kundeId +
                ", adresse=" + adresse +
                ", konten=" + konten +
                ", username='" + username + '\'' +
                ", passwort='" + passwort + '\'' +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", firmenkunde=" + firmenkunde +
                '}';
    }

    @Override
    public Long getID() {
        return this.kundeId;
    }
}
