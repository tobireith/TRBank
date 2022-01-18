package de.othr.sw.TRBank.entity;

import de.othr.sw.TRBank.entity.util.SingleIdEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Kontoauszug extends SingleIdEntity<Long> {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long kontoauszugId;
    @ManyToOne
    private Konto konto;
    @ManyToMany
    private List<Transaktion> transaktionen = new ArrayList<>();
    private long versandId;
    @Digits(integer = 16, fraction = 2)
    private BigDecimal kontostandAnfang;
    @Digits(integer = 16, fraction = 2)
    private BigDecimal kontostandEnde;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVon;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumBis;

    public Kontoauszug() {

    }

    public Kontoauszug(Konto konto, List<Transaktion> transaktionen, long versandId, BigDecimal kontostandAnfang, BigDecimal kontostandEnde, Date datumVon, Date datumBis) {
        this.konto = konto;
        this.transaktionen = transaktionen;
        this.versandId = versandId;
        setKontostandAnfang(kontostandAnfang);
        setKontostandEnde(kontostandEnde);
        this.datumVon = datumVon;
        this.datumBis = datumBis;
    }

    public Kontoauszug(Konto konto, long versandId, BigDecimal kontostandAnfang, BigDecimal kontostandEnde, Date datumVon, Date datumBis) {
        this.konto = konto;
        this.versandId = versandId;
        setKontostandAnfang(kontostandAnfang);
        setKontostandEnde(kontostandEnde);
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
        return transaktionen;
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

    public BigDecimal getKontostandAnfang() {
        return kontostandAnfang;
    }

    public void setKontostandAnfang(BigDecimal kontostandAnfang) {
        this.kontostandAnfang = kontostandAnfang.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getKontostandEnde() {
        return kontostandEnde;
    }

    public void setKontostandEnde(BigDecimal kontostandEnde) {
        this.kontostandEnde = kontostandEnde.setScale(2, RoundingMode.HALF_UP);
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
                ", kontoID=" + konto.getID() +
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
