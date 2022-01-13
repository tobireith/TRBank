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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@Scope("singleton")
@RequestMapping("/konto")
public class KontoController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping(value = "/{kontoId}")
    public String konto(
            @PathVariable long kontoId,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            Model model,
            Principal principal) {
        try {
            // TODO: Kundenobjekt an diese Seite übergeben anstatt es zu laden!

            if (pageNumber == null) {
                return "redirect:/konto/{kontoId}/?pageNumber=1";
            }
            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            Konto konto = bankingService.getKontoFromKundeById(aktuellerKunde, kontoId);
            model.addAttribute("konto", konto);

            List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
            int pageSize = 10;
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("pages", transaktionen.size() / pageSize + 1);

            List<Transaktion> currentTransaktionen = transaktionen.subList((pageNumber - 1) * 10, Math.min(transaktionen.size(), (pageNumber - 1) * 10 + 9));
            model.addAttribute("currentTransaktionen", currentTransaktionen);
            model.addAttribute("pageNumber", pageNumber);

            model.addAttribute("disableDelete", (konto.getKontostand() != 0));
            return "konto";
        } catch (TRBankException exception) {
            model.addAttribute("trException", exception);
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/{kontoId}/delete")
    public String deleteKonto(
            @PathVariable long kontoId,
            Model model) {
        try {
            bankingService.kontoLoeschen(kontoId);
            return "redirect:/";
        } catch (TRBankException e) {
            model.addAttribute("trException", e);
            return "redirect:/konto/{kontoId}/?pageNumber=1";
        }
    }
}
