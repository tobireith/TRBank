package de.othr.sw.TRBank.controller.rest;

import de.othr.DaumDelivery.entity.dto.DeliveryDTO;
import de.othr.DaumDelivery.entity.dto.RestDTO;

public interface SendDeliveryIF {
    DeliveryDTO sendDelivery(RestDTO tempDeliveryDTO) throws Exception;
}
