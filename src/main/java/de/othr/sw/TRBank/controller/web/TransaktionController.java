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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Scope("singleton")
@RequestMapping("/konto/{kontoId}")
public class TransaktionController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping("/transaktion")
    public String getTransaktion(
            @PathVariable long kontoId,
            @ModelAttribute("isSender") boolean isSender,
            Model model,
            Principal principal) {
        try {
            System.out.println("GET on /transaktion");
            // TODO: Kundenobjekt an diese Seite übergeben anstatt es zu laden!
            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            Konto konto = bankingService.getKontoFromKundeById(aktuellerKunde, kontoId);
            model.addAttribute("konto", konto);

            Transaktion transaktion = new Transaktion();
            if (isSender) {
                transaktion.setQuellkonto(konto);
                transaktion.setZielkonto(new Konto());
            } else {
                transaktion.setZielkonto(konto);
                transaktion.setQuellkonto(new Konto());
            }
            model.addAttribute("transaktion", transaktion);

            return "transaktion";
        } catch (TRBankException exception) {
            model.addAttribute("trException", exception);
            return "redirect:/konto/{kontoId}";
        }
    }

    @RequestMapping(value = "/transaktion", method = RequestMethod.POST)
    public String postTransaktion(
            @PathVariable long kontoId,
            @ModelAttribute("isSender") boolean isSender,
            @Valid @ModelAttribute("transaktion") Transaktion transaktion,
            BindingResult result,
            @ModelAttribute("submit") String submit,
            Model model,
            Principal principal
    ) {
        try {
            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            Konto konto = bankingService.getKontoFromKundeById(aktuellerKunde, kontoId);
            model.addAttribute("konto", konto);
            if (submit.equals("Submit")) {
                if (result.hasErrors()) {
                    System.out.println("ERROR! " + result.getAllErrors());
                    return "transaktion";
                }

                bankingService.transaktionTaetigen(transaktion, aktuellerKunde);
            }
            return "redirect:/konto/{kontoId}";
        } catch (TRBankException exception) {
            model.addAttribute("trException", exception);
            return "transaktion";
        }
    }

}
