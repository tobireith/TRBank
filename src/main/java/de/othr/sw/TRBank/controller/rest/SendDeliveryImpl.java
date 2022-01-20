package de.othr.sw.TRBank.controller.rest;

import de.othr.sw.TRBank.service.impl.BankingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/*
@Scope("singleton")
@Service
public class SendDeliveryImpl implements SendDeliveryIF {

    @Autowired
    private RestTemplate restServiceClient;

    private final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);

    @Override
    public TempDelivery sendDelivery(TempDeliveryDTO tempDeliveryDTO) {
        logger.info("Requesting new Delivery from DaumDeliveries!");
        // FIXME:   Adjust URL and Port
        //          Adjust Objects + Class
        return restServiceClient.postForObject("im-codd.oth-regensburg.de/8934" + "/api/deliveries", tempDeliveryDTO, TempDelivery.class);
        //TODO: Errorhandling
    }
}
 */
