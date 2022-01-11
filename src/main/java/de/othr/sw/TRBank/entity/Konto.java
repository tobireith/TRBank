package de.othr.sw.TRBank.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.othr.sw.TRBank.entity.util.SingleIdEntity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Konto extends SingleIdEntity<Long> {

    //TODO: Add this to UML Schema!
    public final int SCHULDENLIMIT = -25000;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long kontoId;
    //TODO: Cascade Type?
    @ManyToOne
    private Kunde besitzer;
    @JsonManagedReference(value = "quelle")
    @OneToMany(mappedBy = "quellkonto")
    private List<Transaktion> transaktionenRaus = new ArrayList<>();
    @JsonManagedReference(value = "ziel")
    @OneToMany(mappedBy = "zielkonto")
    private List<Transaktion> transaktionenRein = new ArrayList<>();
    @NotNull
    @Size(min = 22, max = 22)
    @Column(unique = true)
    private String iban;
    @Min(value = SCHULDENLIMIT)
    private double kontostand = 0.0;

    public Konto() {
    }

    public Konto(String iban, Kunde besitzer, double kontostand) {
        this.iban = iban;
        this.besitzer = besitzer;
        this.kontostand = kontostand;
    }

    public long getKontoId() {
        return kontoId;
    }

    public Kunde getBesitzer() {
        return besitzer;
    }

    public void setBesitzer(Kunde besitzer) {
        this.besitzer = besitzer;
    }

    public List<Transaktion> getTransaktionenRaus() {
        return Collections.unmodifiableList(transaktionenRaus);
    }

    public List<Transaktion> getTransaktionenRein() {
        return Collections.unmodifiableList(transaktionenRein);
    }

    public void setTransaktionenRaus(List<Transaktion> transaktionenRaus) {
        this.transaktionenRaus = transaktionenRaus;
    }

    public void setTransaktionenRein(List<Transaktion> transaktionenRein) {
        this.transaktionenRein = transaktionenRein;
    }

    public double getKontostand() {
        return kontostand;
    }

    public void setKontostand(double kontostand) {
        this.kontostand = kontostand;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public String toString() {
        return "Konto{" +
                "kontoId=" + kontoId +
                ", besitzerID=" + (besitzer != null ? besitzer.getID() : null) +
                ", transaktionenRaus=" + transaktionenRaus +
                ", transaktionenRein=" + transaktionenRein +
                ", iban='" + iban + '\'' +
                ", kontostand=" + kontostand +
                '}';
    }

    @Override
    public Long getID() {
        return this.kontoId;
    }
}
