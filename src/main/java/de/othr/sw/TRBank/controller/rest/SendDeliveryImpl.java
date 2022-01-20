package de.othr.sw.TRBank.controller.rest;

import de.othr.DaumDelivery.entity.dto.DeliveryDTO;
import de.othr.DaumDelivery.entity.dto.RestDTO;
import de.othr.sw.TRBank.service.impl.BankingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Scope("singleton")
@Primary
@Service("default")
public class SendDeliveryImpl implements SendDeliveryIF {

    @Autowired
    private RestTemplate restServiceClient;

    @Autowired
    private SendDeliveryProxy sendDeliveryProxy;

    private final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);

    @Override
    public DeliveryDTO sendDelivery(RestDTO restDTO) {
        try {
            logger.info("Requesting new Delivery from DaumDeliveries.");
            DeliveryDTO deliveryDTO = restServiceClient.postForObject("http://im-codd.oth-regensburg.de:8970/api/rest/delivery", restDTO, DeliveryDTO.class);
            if(deliveryDTO == null) {
                throw new Exception();
            }
            return deliveryDTO;
        } catch (Exception exception) {
            logger.error("An Error occurred while calling the external Delivery-System: " + exception);
            logger.info("Using own proxy implementation for sending the delivery, since Daum Deliveries isn't available.");
            return sendDeliveryProxy.sendDelivery(restDTO);
        }
    }
}
