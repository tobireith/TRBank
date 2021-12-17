package de.othr.sw.TRBank.service.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/konto")
public class KontoController {

    @Autowired
    private BankingServiceIF bankingService;

    @Autowired
    private KundeServiceIF kundeService;

    @RequestMapping(value = "/{kontoId}")
    public String konto(
            @PathVariable long kontoId,
            Model model) throws TRBankException {
        // TODO: Render Error-Page!
        Konto konto = bankingService.getKontoById(kontoId);
        model.addAttribute("konto", konto);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);
        return "konto";
    }
}
