package de.othr.sw.TRBank.controller.rest;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.entity.dto.RestDTO;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Scope("singleton")
@RestController
@Validated
@RequestMapping(value = "/api/rest")
public class TransaktionRestController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping(value = "/transaktion", method = RequestMethod.POST)
    public Transaktion transaktionTaetigen(
            @Valid @RequestBody RestDTO restDTO
    ) throws TRBankException {
        Kunde kunde = kundeService.anmeldedatenVerifizieren(restDTO.getUsername(), restDTO.getPasswort());
        Transaktion transaktion = bankingService.transaktionTaetigen(restDTO.getTransaktionDTO(), kunde);

        System.out.println("EXTERNE TRANSAKTION WURDE DURCHGEFÜHRT: " + transaktion);

        return transaktion;
    }
}
