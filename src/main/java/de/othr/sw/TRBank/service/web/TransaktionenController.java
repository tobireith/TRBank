package de.othr.sw.TRBank.service.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.KontoServiceIF;
import de.othr.sw.TRBank.service.TransaktionServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TransaktionenController {

    @Autowired
    private TransaktionServiceIF transaktionService;

    @Autowired
    private KontoServiceIF kontoService;

    @RequestMapping("/transactions")
    public String transactions(Model model) {
        //TODO
        String iban = "DE12345678900822331220";
        Konto konto = kontoService.getKontoByIban(iban);
        model.addAttribute("konto", konto);
        List<Transaktion> transaktionen = transaktionService.getAllTransaktionenForKonto(konto);
        model.addAttribute("transaktionen", transaktionen);
        return "transaktionen";
    }
}
