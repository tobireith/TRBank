package de.othr.sw.TRBank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.othr.sw.TRBank.entity.util.SingleIdEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
public class Transaktion extends SingleIdEntity<Long> {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long transaktionId;
    @Valid
    @JsonBackReference(value = "quelle")
    @ManyToOne
    private Konto quellkonto;
    @Valid
    @JsonBackReference(value = "ziel")
    @ManyToOne
    private Konto zielkonto;
    @NotNull
    @Positive
    @Digits(integer = 9, fraction = 2)
    private BigDecimal betrag;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    @Length(max = 256)
    private String verwendungszweck;

    public Transaktion() {
    }

    public Transaktion(Konto vonKonto, Konto zuKonto, BigDecimal betrag, String verwendgunszweck) {
        this.quellkonto = vonKonto;
        this.zielkonto = zuKonto;
        this.setBetrag(betrag);
        this.datum = null;
        this.verwendungszweck = verwendgunszweck;
    }

    public Transaktion(Konto vonKonto, Konto zuKonto, BigDecimal betrag, Date datum, String verwendgunszweck) {
        this.quellkonto = vonKonto;
        this.zielkonto = zuKonto;
        this.setBetrag(betrag);
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

    public BigDecimal getBetrag() {
        return betrag;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag.setScale(2, RoundingMode.HALF_UP);
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
                ", quellkontoId=" + (quellkonto != null ? quellkonto.getID() : null)  +
                ", zielkontoId=" + (zielkonto != null ? zielkonto.getID() : null) +
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
