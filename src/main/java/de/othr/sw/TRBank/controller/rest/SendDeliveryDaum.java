package de.othr.sw.TRBank.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Scope("singleton")
@Service
public class SendDeliveryDaum implements SendDeliveryIF {

    @Autowired
    private RestTemplate restServiceClient;

    @Override
    public TempDelivery sendDelivery(TempDeliveryDTO tempDeliveryDTO) {
        System.out.println("Requesting new Delivery from DaumDeliveries!");
        // FIXME:   Adjust URL and Port
        //          Adjust Objects + Class
        return restServiceClient.postForObject("im-codd.oth-regensburg.de/8934" + "/api/deliveries", tempDeliveryDTO, TempDelivery.class);
        //TODO: Errorhandling
    }
}
