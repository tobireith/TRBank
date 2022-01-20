package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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

    private final Logger logger = LoggerFactory.getLogger(KontoController.class);

    @RequestMapping(value = "/{kontoId}")
    public String konto(
            @PathVariable long kontoId,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            Model model,
            Principal principal,
            RedirectAttributes attributes
    ) {
        TRBankException trBankException = (TRBankException) model.asMap().get("trBankException");
        if(trBankException != null) {
            model.addAttribute("trBankException", trBankException);
        }
        try {

            if (pageNumber == null) {
                attributes.addFlashAttribute("trBankException", trBankException);
                return "redirect:/konto/{kontoId}/?pageNumber=1";
            }
            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            Konto konto = bankingService.getKontoFromKundeById(aktuellerKunde, kontoId);
            model.addAttribute("konto", konto);

            List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
            int pageSize = 10;
            int pages = (transaktionen.size()-1) / pageSize + 1;

            if(pageNumber > pages) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST
                );
            }

            model.addAttribute("pageSize", pageSize);
            model.addAttribute("pages", pages);
            List<Transaktion> currentTransaktionen = transaktionen.subList((pageNumber - 1) * 10, Math.min(transaktionen.size(), (pageNumber - 1) * 10 + 9));
            model.addAttribute("currentTransaktionen", currentTransaktionen);
            model.addAttribute("pageNumber", pageNumber);

            model.addAttribute("disableDelete", (konto.getKontostand().compareTo(new BigDecimal("0.0")) != 0));
            return "konto";
        } catch (TRBankException exception) {
            logger.error("An Error occurred: " + exception);
            attributes.addFlashAttribute("trBankException", exception);
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/{kontoId}/delete")
    public String deleteKonto(
            @PathVariable long kontoId,
            Model model,
            RedirectAttributes attributes) {
        try {
            bankingService.kontoLoeschen(kontoId);
            return "redirect:/";
        } catch (TRBankException exception) {
            logger.error("An Error occurred while deleting the Konto: " + exception);
            attributes.addFlashAttribute("trBankException", exception);
            return "redirect:/konto/{kontoId}/?pageNumber=1";
        }
    }
}
