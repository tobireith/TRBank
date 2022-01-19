package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Date;

@Controller
@Scope("singleton")
public class KontoauszugController {
    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private KundeServiceIF kundeService;

    private final Logger logger = LoggerFactory.getLogger(KontoauszugController.class);

    @RequestMapping(value = "/konto/{kontoId}/kontoauszug")
    public String kontoauszug(@PathVariable long kontoId,
                              Model model,
                              Principal principal){
        try {
            System.out.println("GET /konto/id/kontoauszug");
            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            model.addAttribute("kunde", aktuellerKunde);

            model.addAttribute("datum", new Date());

            Konto konto = bankingService.getKontoFromKundeById(aktuellerKunde, kontoId);

            Kontoauszug kontoauszug = bankingService.kontoauszugErstellen(konto);
            model.addAttribute("kontoauszug", kontoauszug);

            model.addAttribute("transaktionen", kontoauszug.getTransaktionen());

            model.addAttribute("alertSuccessTitle", "Kontoauszug erstellt und versendet.");
            model.addAttribute("alertSuccessMessage", "Der Kontoauszug wurde erfolgreich erstellt " +
                    "und ein Versandauftrag wurde beim Versandunternehmen DaumDelivery erstellt. " +
                    "Die Sendungsnummer finden Sie in Ihrem Kontoauszug.");
            return "kontoauszug";
        } catch (TRBankException exception) {
            model.addAttribute("trException", exception);
            logger.error("An Error occurred: " + exception);
            return "redirect:/konto/{kontoId}";
        }
    }
}
