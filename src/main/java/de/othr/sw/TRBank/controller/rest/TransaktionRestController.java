package de.othr.sw.TRBank.controller.rest;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.entity.dto.RestDTO;
import de.othr.sw.TRBank.entity.dto.TransaktionDTO;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import de.othr.sw.TRBank.service.impl.BankingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Scope("singleton")
@RestController
@Validated
@RequestMapping(value = "/api/rest")
public class TransaktionRestController {

    private final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);
    @Autowired
    private BankingServiceIF bankingService;
    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping(value = "/transaktion", method = RequestMethod.POST)
    public TransaktionDTO transaktionTaetigen(
            @Valid @RequestBody RestDTO restDTO
    ) throws TRBankException {
        Kunde kunde = kundeService.anmeldedatenVerifizieren(restDTO.getUsername(), restDTO.getPasswort());
        Transaktion transaktion = bankingService.transaktionTaetigen(restDTO.getTransaktionDTO(), kunde);

        TransaktionDTO responseTransaktion = new TransaktionDTO(
                transaktion.getQuellkonto().getIban(),
                transaktion.getZielkonto().getIban(),
                transaktion.getBetrag(),
                transaktion.getVerwendungszweck()
        );
        responseTransaktion.setDatum(transaktion.getDatum());

        logger.info("Transaktion von externem Aufruf wurde durchgeführt.");
        return responseTransaktion;
    }
}
