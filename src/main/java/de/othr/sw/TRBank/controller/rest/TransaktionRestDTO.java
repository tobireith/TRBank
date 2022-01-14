package de.othr.sw.TRBank.controller.rest;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;

import javax.validation.Valid;

public class TransaktionRestDTO {
    private Kunde kunde;
    @Valid
    private Transaktion transaktion;

    public TransaktionRestDTO(Kunde kunde, Transaktion transaktion) {
        this.kunde = kunde;
        this.transaktion = transaktion;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public Transaktion getTransaktion() {
        return transaktion;
    }

    public void setTransaktion(Transaktion transaktion) {
        this.transaktion = transaktion;
    }

    @Override
    public String toString() {
        return "TransaktionRestDTO{" +
                "kunde=" + kunde +
                ", transaktion=" + transaktion +
                '}';
    }
}
