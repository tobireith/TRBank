package de.othr.sw.TRBank.entity;

import de.othr.sw.TRBank.entity.util.SingleIdEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @Valid
    private Adresse adresse;
    @OneToMany(mappedBy = "besitzer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Konto> konten = new ArrayList<>();
    @Column(unique = true)
    @NotBlank
    @Length(min = 6, max = 20)
    private String username;
    @NotBlank
    @Length(min = 6, max = 20)
    private String passwort;
    @NotBlank
    private String vorname;
    @NotBlank
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

    public Kunde addKonto(Konto konto) {
        konten.add(konto);
        konto.setBesitzer(this);
        return this;
    }

    public Kunde removeKonto(Konto konto) {
        konten.remove(konto);
        konto.setBesitzer(null);
        return this;
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
        return List.of(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                if(isFirmenkunde()) {
                    return "FIRMENKUNDE";
                } else {
                    return "STANDARD";
                }
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
