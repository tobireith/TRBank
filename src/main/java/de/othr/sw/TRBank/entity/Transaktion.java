package de.othr.sw.TRBank.entity;

import de.othr.sw.TRBank.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaktion extends SingleIdEntity<Long> {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long transaktionId;
    //TODO: Cascade Type?
    @ManyToOne
    private Konto quellkonto;
    //TODO: Cascade Type?
    @ManyToOne
    private Konto zielkonto;
    private double betrag;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    private String verwendungszweck;

    public Transaktion() {
    }

    public Transaktion(Konto vonKonto, Konto zuKonto, double betrag, Date datum, String verwendgunszweck) {
        this.quellkonto = vonKonto;
        this.zielkonto = zuKonto;
        this.betrag = betrag;
        this.datum = datum;
        this.verwendungszweck = verwendgunszweck;
    }

    public long getTransaktionId() {
        return transaktionId;
    }

    public Konto getQuellkonto() {
        return quellkonto;
    }

    public void setQuellkonto(Konto vonKonto) {
        this.quellkonto = vonKonto;
    }

    public Konto getZielkonto() {
        return zielkonto;
    }

    public void setZielkonto(Konto zuKontoId) {
        this.zielkonto = zuKontoId;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    public void setVerwendungszweck(String verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
    }

    @Override
    public String toString() {
        return "Transaktion{" +
                "transaktionId=" + transaktionId +
                ", quellkonto=" + quellkonto +
                ", zielkonto=" + zielkonto +
                ", betrag=" + betrag +
                ", datum=" + datum +
                ", verwendungszweck='" + verwendungszweck + '\'' +
                '}';
    }

    @Override
    public Long getID() {
        return this.transaktionId;
    }
}
