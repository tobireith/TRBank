package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.dto.TransaktionDTO;
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
            model.addAttribute("trException", exception);
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
            model.addAttribute("successTitle", "Transaktion erfolgreich durchgeführt.");
            model.addAttribute("successMessage", "Die Transaktion wurde erfolgreich durchgeführt.");
            return "success";
        } catch (TRBankException exception) {
            model.addAttribute("trException", exception);
            return "transaktion";
        }
    }

}
