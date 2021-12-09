package de.othr.sw.TRBank.entity;

import de.othr.sw.TRBank.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
public class Kontoauszug extends SingleIdEntity<Long> {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long kontoauszugId;
    //TODO: Cascade Type?
    @ManyToOne
    private Konto konto;
    //TODO: Cascade Type?
    @ManyToMany
    private List<Transaktion> transaktionen;
    private long versandId;
    private double kontostandAnfang;
    private double kontostandEnde;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVon;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumBis;

    public Kontoauszug() {

    }

    public Kontoauszug(Konto konto, long versandId, double kontostandAnfang, double kontostandEnde, Date datumVon, Date datumBis) {
        this.konto = konto;
        this.versandId = versandId;
        this.kontostandAnfang = kontostandAnfang;
        this.kontostandEnde = kontostandEnde;
        this.datumVon = datumVon;
        this.datumBis = datumBis;
    }

    public long getKontoauszugId() {
        return kontoauszugId;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(Konto konto) {
        this.konto = konto;
    }

    public List<Transaktion> getTransaktionen() {
        return Collections.unmodifiableList(transaktionen);
    }

    public void setTransaktionen(List<Transaktion> transaktionen) {
        this.transaktionen = transaktionen;
    }

    public long getVersandId() {
        return versandId;
    }

    public void setVersandId(long versandId) {
        this.versandId = versandId;
    }

    public double getKontostandAnfang() {
        return kontostandAnfang;
    }

    public void setKontostandAnfang(double kontostandAnfang) {
        this.kontostandAnfang = kontostandAnfang;
    }

    public double getKontostandEnde() {
        return kontostandEnde;
    }

    public void setKontostandEnde(double kontostandEnde) {
        this.kontostandEnde = kontostandEnde;
    }

    public Date getDatumVon() {
        return datumVon;
    }

    public void setDatumVon(Date datumVon) {
        this.datumVon = datumVon;
    }

    public Date getDatumBis() {
        return datumBis;
    }

    public void setDatumBis(Date datumBis) {
        this.datumBis = datumBis;
    }

    @Override
    public String toString() {
        return "Kontoauszug{" +
                "kontoauszugId=" + kontoauszugId +
                ", konto=" + konto +
                ", transaktionen=" + transaktionen +
                ", versandId=" + versandId +
                ", kontostandAnfang=" + kontostandAnfang +
                ", kontostandEnde=" + kontostandEnde +
                ", datumVon=" + datumVon +
                ", datumBis=" + datumBis +
                '}';
    }

    @Override
    public Long getID() {
        return this.kontoauszugId;
    }
}
