package de.othr.sw.TRBank.service.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping(value = "/")
    public String home(Model model) {
        //TODO: Change this to get the current logged in Kunde
        Kunde aktuellerKunde = kundeService.getAllKunden().get(0);
        model.addAttribute("konten", aktuellerKunde.getKonten());

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(aktuellerKunde.getKonten());
        if(transaktionen.size() > 5) {
            transaktionen = transaktionen.subList(0, 4);
        }
        model.addAttribute("transaktionen", transaktionen);
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)   // /login
    public String login(Model model) {
        model.addAttribute("kunde", new Kunde());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)    // th:action="@{login}"
    public String doLogin(
    ) {
        return "index";
    }
}
