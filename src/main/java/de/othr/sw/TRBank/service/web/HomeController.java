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
import org.springframework.web.bind.annotation.RequestMapping;

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
        List<Konto> konten = bankingService.getKontenByKunde(aktuellerKunde);
        model.addAttribute("konten", konten);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(konten, PageRequest.of(0, 5));
        model.addAttribute("transaktionen", transaktionen);
        return "index";
    }
}
