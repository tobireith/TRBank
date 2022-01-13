package de.othr.sw.TRBank.controller.rest;

import de.othr.sw.TRBank.entity.Kunde;

public class TempDeliveryDTO {
    private Kunde kunde;
    private TempDelivery tempDelivery;

    public TempDeliveryDTO(Kunde kunde, TempDelivery tempDelivery) {
        this.kunde = kunde;
        this.tempDelivery = tempDelivery;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public TempDelivery getTempDelivery() {
        return tempDelivery;
    }

    public void setTempDelivery(TempDelivery tempDelivery) {
        this.tempDelivery = tempDelivery;
    }
}
