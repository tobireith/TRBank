package de.othr.sw.TRBank.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

@Controller
@Scope("singleton")
@RequestMapping("/konto/{kontoId}")
public class TransaktionController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private LoginController loginController;

    @RequestMapping("/transaktion")
    public String getTransaktion(
            @PathVariable long kontoId,
            @ModelAttribute("isSender") boolean isSender,
            Model model) throws TRBankException {
        // TODO: Render Error-Page!
        // TODO: Kundenobjekt an diese Seite übergeben anstatt es zu laden!
        Konto konto = bankingService.getKontoFromKundeById(loginController.getKunde(), kontoId);
        model.addAttribute("konto", konto);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);
        return "transaktion";
    }

    @RequestMapping(value = "/transaktion", method = RequestMethod.POST)    // th:action="@{login}"
    public String postTransaktion(
            @PathVariable long kontoId,
            @ModelAttribute("ibanTo") String inputIbanTo,
            @ModelAttribute("betrag") String betrag,
            @ModelAttribute("ibanFrom") String inputIbanFrom,
            @ModelAttribute("verwendungszweck") String verwendungszweck,
            @ModelAttribute("submit") String submit,
            Model model
    ) throws TRBankException {
        if(submit.equals("Submit")) {
            System.out.println("SUBMITTED TRANSAKTION");
            // TODO: Render Error-Page!
            Konto quellkonto = bankingService.getKontoByIban(inputIbanFrom);
            Konto zielkonto = bankingService.getKontoByIban(inputIbanTo);

            Transaktion transaktion = new Transaktion(quellkonto, zielkonto, Double.parseDouble(betrag), new Date(), verwendungszweck);

            bankingService.transaktionTaetigen(transaktion);
        } else {
            System.out.println("CANCELED TRANSAKTION");
        }


        return "redirect:/konto/{kontoId}";
    }

}
