package de.othr.sw.TRBank.controller.rest;

import de.othr.DaumDelivery.entity.dto.DeliveryDTO;
import de.othr.DaumDelivery.entity.dto.RestDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Scope("singleton")
@Service("ownProxy")
public class SendDeliveryProxy implements SendDeliveryIF{
    @Override
    public DeliveryDTO sendDelivery(RestDTO tempDeliveryDTO) {
        return new DeliveryDTO(new Random().nextInt(999999), new Date(), 1.00);
    }
}
