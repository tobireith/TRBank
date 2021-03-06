package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.dto.TransaktionDTO;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Scope("singleton")
@RequestMapping("/konto/{kontoId}")
public class TransaktionController {

    private final Logger logger = LoggerFactory.getLogger(TransaktionController.class);
    @Autowired
    private BankingServiceIF bankingService;
    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping("/transaktion")
    public String getTransaktion(
            @PathVariable long kontoId,
            @ModelAttribute("isSender") boolean isSender,
            Model model,
            Principal principal,
            RedirectAttributes attributes
    ) {
        TRBankException trBankException = (TRBankException) model.asMap().get("trBankException");
        if (trBankException != null) {
            model.addAttribute("trBankException", trBankException);
        }
        try {
            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            Konto konto = bankingService.getKontoFromKundeById(aktuellerKunde, kontoId);
            model.addAttribute("konto", konto);

            TransaktionDTO transaktionDTO = new TransaktionDTO();
            if (isSender) {
                transaktionDTO.setQuellIban(konto.getIban());
            } else {
                transaktionDTO.setZielIban(konto.getIban());
            }
            model.addAttribute("transaktionDTO", transaktionDTO);

            return "transaktion";
        } catch (TRBankException exception) {
            attributes.addFlashAttribute("trBankException", exception);
            logger.error("An Error occurred: " + exception);
            return "redirect:/konto/{kontoId}";
        }
    }

    @RequestMapping(value = "/transaktion", method = RequestMethod.POST)
    public String postTransaktion(
            @PathVariable long kontoId,
            @ModelAttribute("isSender") boolean isSender,
            @Valid @ModelAttribute("transaktionDTO") TransaktionDTO transaktionDTO,
            BindingResult result,
            Model model,
            Principal principal
    ) {
        try {
            Kunde aktuellerKunde = kundeService.getKundeByUsername(principal.getName());
            Konto konto = bankingService.getKontoFromKundeById(aktuellerKunde, kontoId);
            model.addAttribute("konto", konto);
            if (result.hasErrors()) {
                return "transaktion";
            }
            bankingService.transaktionTaetigen(transaktionDTO, aktuellerKunde);
            model.addAttribute("successTitle", "Transaktion erfolgreich durchgef??hrt.");
            model.addAttribute("successMessage", "Die Transaktion wurde erfolgreich durchgef??hrt.");
            return "success";
        } catch (TRBankException exception) {
            model.addAttribute("trBankException", exception);
            logger.error("An TRBank-Error occurred: " + exception);
            return "transaktion";
        }
    }

}
