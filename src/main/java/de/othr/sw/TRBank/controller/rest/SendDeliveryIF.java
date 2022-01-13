package de.othr.sw.TRBank.controller.rest;

import de.othr.sw.TRBank.entity.Kontoauszug;

public interface SendDeliveryIF {
    // FIXME:   Change this to return the real delivery!
    //          Wrap everything in the dto object from DaumDelivery!
    TempDelivery sendDelivery(TempDeliveryDTO tempDeliveryDTO);
}
