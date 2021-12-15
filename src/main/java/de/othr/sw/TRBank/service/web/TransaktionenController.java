package de.othr.sw.TRBank.service.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TransaktionenController {

    @Autowired
    private BankingServiceIF bankingService;

    @RequestMapping("/transactions")
    public String transactions(Model model) {
        //TODO
        String iban = "DE12345678900822331220";
        // TODO: Render Error-Page!
        Konto konto = null;
        try {
            konto = bankingService.getKontoByIban(iban);
        } catch (TRBankException e) {
            e.printStackTrace();
        }
        model.addAttribute("konto", konto);
        //TODO: Make Buttons for the next 10 Transactions! (Next Page...)
        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto), PageRequest.of(0, 10));
        model.addAttribute("transaktionen", transaktionen);
        return "transaktionen";
    }
}
