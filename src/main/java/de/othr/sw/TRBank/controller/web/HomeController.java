package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@Scope("singleton")
public class HomeController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping(value = "/")
    public String home(Model model, Principal principal) {
        try {
            System.out.println("GET /");

            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            model.addAttribute("kunde", aktuellerKunde);
            model.addAttribute("konten", aktuellerKunde.getKonten());

            BigDecimal kontostandTotal = new BigDecimal("0.0");
            for(Konto konto : aktuellerKunde.getKonten()) {
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
            model.addAttribute("trException", exception);
            return "redirect:/error";
        }

    }

}
