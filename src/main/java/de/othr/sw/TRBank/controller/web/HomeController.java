package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@Scope("singleton")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private BankingServiceIF bankingService;
    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping(value = "/")
    public String home(
            Model model,
            Principal principal,
            RedirectAttributes attributes
    ) {
        TRBankException trBankException = (TRBankException) model.asMap().get("trBankException");
        if (trBankException != null) {
            model.addAttribute("trBankException", trBankException);
        }
        try {

            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            model.addAttribute("kunde", aktuellerKunde);
            model.addAttribute("konten", aktuellerKunde.getKonten());

            BigDecimal kontostandTotal = new BigDecimal("0.0");
            for (Konto konto : aktuellerKunde.getKonten()) {
                kontostandTotal = kontostandTotal.add(konto.getKontostand());
            }
            model.addAttribute("kontostandTotal", kontostandTotal);

            List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(aktuellerKunde.getKonten());
            if (transaktionen.size() > 5) {
                transaktionen = transaktionen.subList(0, 5);
            }
            model.addAttribute("transaktionen", transaktionen);
            return "index";
        } catch (TRBankException exception) {
            attributes.addFlashAttribute("trBankException", exception);
            logger.error("An Error occurred: " + exception);
            return "redirect:/error";
        }

    }

}
