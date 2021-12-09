package de.othr.sw.TRBank.service.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private BankingServiceIF bankingService;

    @RequestMapping(value = "/")
    public String home(Model model) {
        //TODO
        String iban = "DE12345678900822331220";
        Konto konto = bankingService.getKontoByIban(iban);
        model.addAttribute("konto", konto);
        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto), PageRequest.of(0, 5));
        model.addAttribute("transaktionen", transaktionen);
        return "index";
    }
}
