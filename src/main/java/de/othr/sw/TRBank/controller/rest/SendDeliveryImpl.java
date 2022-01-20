package de.othr.sw.TRBank.controller.rest;

import de.othr.DaumDelivery.entity.dto.DeliveryDTO;
import de.othr.DaumDelivery.entity.dto.RestDTO;
import de.othr.sw.TRBank.service.impl.BankingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Scope("singleton")
@Service
public class SendDeliveryImpl implements SendDeliveryIF {

    @Autowired
    private RestTemplate restServiceClient;

    private final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);

    @Value("$oth.url")
    private String baseUrl;
    @Value("$oth.daum.port")
    private String daumPort;
    @Value("$oth.daum.url.transaktion")
    private String daumTransaktionUrl;

    @Override
    public DeliveryDTO sendDelivery(RestDTO restDTO) {
        logger.info("Requesting new Delivery from DaumDeliveries!");
        return restServiceClient.postForObject(baseUrl + ":" + daumPort + daumTransaktionUrl, restDTO, DeliveryDTO.class);
    }
}
