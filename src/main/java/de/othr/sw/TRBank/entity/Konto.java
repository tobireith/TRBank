package de.othr.sw.TRBank.entity;

import de.othr.sw.TRBank.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
public class Konto extends SingleIdEntity<String> {

    @Id
    private String iban;

    //TODO: Cascade Type?
    @ManyToOne
    private Kunde besitzer;
    @OneToMany(mappedBy = "quellkonto")
    private Collection<Transaktion> transaktionenRaus;
    @OneToMany(mappedBy = "zielkonto")
    private Collection<Transaktion> transaktionenRein;
    private double kontostand;


    public Konto() {
    }

    public Konto(String iban, Kunde besitzer, double kontostand) {
        this.iban = iban;
        this.besitzer = besitzer;
        this.kontostand = kontostand;
    }

    public Kunde getBesitzer() {
        return besitzer;
    }

    public void setBesitzer(Kunde besitzer) {
        this.besitzer = besitzer;
    }

    public Collection<Transaktion> getTransaktionenRaus() {
        return Collections.unmodifiableCollection(transaktionenRaus);
    }

    public Collection<Transaktion> getTransaktionenRein() {
        return Collections.unmodifiableCollection(transaktionenRein);
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
                ", besitzer=" + besitzer +
                ", ausgehend=" + transaktionenRaus +
                ", einkommend=" + transaktionenRein +
                ", kontostand=" + kontostand +
                ", iban='" + iban + '\'' +
                '}';
    }

    @Override
    public String getID() {
        return this.iban;
    }
}
