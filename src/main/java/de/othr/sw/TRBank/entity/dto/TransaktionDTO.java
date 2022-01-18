package de.othr.sw.TRBank.entity.dto;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

public class TransaktionDTO {
    @NotBlank
    @Size(min = 22, max = 22)
    private String quellIban;
    @NotBlank
    @Size(min = 22, max = 22)
    private String zielIban;

    @NotNull
    @Positive
    private BigDecimal betrag;

    @Length(max = 256)
    private String verwendungszweck;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;

    public TransaktionDTO() {

    }

    public TransaktionDTO(String quellIban, String zielIban, BigDecimal betrag, String verwendungszweck) {
        this.quellIban = quellIban;
        this.zielIban = zielIban;
        this.betrag = betrag;
        this.verwendungszweck = verwendungszweck;
    }

    public String getQuellIban() {
        return quellIban;
    }

    public void setQuellIban(String quellIban) {
        this.quellIban = quellIban;
    }

    public String getZielIban() {
        return zielIban;
    }

    public void setZielIban(String zielIban) {
        this.zielIban = zielIban;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    public void setVerwendungszweck(String verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "TransaktionDTO{" +
                "quellIban='" + quellIban + '\'' +
                ", zielIban='" + zielIban + '\'' +
                ", betrag=" + betrag +
                ", verwendungszweck='" + verwendungszweck + '\'' +
                ", datum=" + datum +
                '}';
    }
}
