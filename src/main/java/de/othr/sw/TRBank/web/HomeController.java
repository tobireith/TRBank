package de.othr.sw.TRBank.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@Scope("singleton")
public class HomeController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private LoginController loginController;

    @RequestMapping(value = "/")
    public String home(Model model) {
        System.out.println("GET /");

        Kunde aktuellerKunde = loginController.getKunde();
        model.addAttribute("kunde", aktuellerKunde);
        model.addAttribute("konten", aktuellerKunde.getKonten());

        double kontostandTotal = aktuellerKunde.getKonten().stream().mapToDouble(Konto::getKontostand).sum();
        model.addAttribute("kontostandTotal", kontostandTotal);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(aktuellerKunde.getKonten());
        if (transaktionen.size() > 5) {
            transaktionen = transaktionen.subList(0, 5);
        }
        model.addAttribute("transaktionen", transaktionen);
        return "index";


    }

}
