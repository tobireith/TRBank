package de.othr.sw.TRBank.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

@RequestMapping("/konto/{kontoId}")
@Controller
public class TransaktionController {

    @Autowired
    private BankingServiceIF bankingService;

    @RequestMapping("/zahlen")
    public String transaktionZahlen(
            @PathVariable long kontoId,
            Model model) throws TRBankException {
        // TODO: Render Error-Page!
        Konto konto = bankingService.getKontoById(kontoId);
        model.addAttribute("konto", konto);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);
        return "transaktionZahlen";
    }

    @RequestMapping(value = "/zahlen", method = RequestMethod.POST)    // th:action="@{login}"
    public String doLogin(
            @PathVariable long kontoId,
            @ModelAttribute("ibanTo") String inputIbanTo,
            @ModelAttribute("betrag") double betrag,
            @ModelAttribute("ibanFrom") String inputIbanFrom,
            @ModelAttribute("verwendungszweck") String verwendungszweck,
            Model model
    ) throws TRBankException {
        // TODO: Render Error-Page!
        Konto quellkonto = bankingService.getKontoByIban(inputIbanFrom);
        Konto zielkonto = bankingService.getKontoByIban(inputIbanTo);

        Transaktion transaktion = new Transaktion(quellkonto, zielkonto, betrag, new Date(), verwendungszweck);

        transaktion = bankingService.transaktionTaetigen(transaktion);
        System.out.println("TRANSAKTION POSTED: " + transaktion);
        return "redirect:/";
    }


    @RequestMapping("/anfordern")
    public String transaktionAnfordern(
            @PathVariable long kontoId,
            Model model) throws TRBankException {
        // TODO: Render Error-Page!
        Konto konto = bankingService.getKontoById(kontoId);
        model.addAttribute("konto", konto);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);
        return "transaktionAnfordern";
    }


}
