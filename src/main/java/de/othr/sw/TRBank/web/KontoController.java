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

import java.util.List;

@Controller
@Scope("singleton")
@RequestMapping("/konto")
public class KontoController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private LoginController loginController;

    @RequestMapping(value = "/{kontoId}")
    public String konto(
            @PathVariable long kontoId,
            Model model) throws TRBankException {
        // TODO: Render Error-Page!
        // TODO: Kundenobjekt an diese Seite übergeben anstatt es zu laden!
        Konto konto = bankingService.getKontoFromKundeById(loginController.getKunde() ,kontoId);
        model.addAttribute("konto", konto);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);
        return "konto";
    }

    @RequestMapping(value = "/{kontoId}/close-account", method = RequestMethod.GET)
    public String kontoAufloesen(
            @PathVariable long kontoId,
            Model model) throws TRBankException {
        // TODO: Render Error-Page!
        // TODO: Kundenobjekt an diese Seite übergeben anstatt es zu laden!
        Konto konto = bankingService.getKontoFromKundeById(loginController.getKunde() ,kontoId);
        model.addAttribute("konto", konto);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);
        return "closeKonto";
    }

    @RequestMapping(value = "/{kontoId}/close-account", method = RequestMethod.POST)
    public String doKontoAufloesen(
            @PathVariable long kontoId,
            @ModelAttribute("submitCloseKonto") String submitCloseKonto,
            Model model) {
        if(submitCloseKonto.equals("Submit")) {
            System.out.println("DELETING KONTO");
            // TODO: Render Error-Page!
            // TODO: Delete Konto!
            return "redirect:/";
        } else {
            System.out.println("CANCELED DELETION OF KONTO");
            return "redirect:/konto/{kontoId}";
        }

    }
}
