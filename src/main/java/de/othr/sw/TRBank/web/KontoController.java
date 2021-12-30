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
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(name = "pageNumber", required = false) String pageNumber,
            Model model) throws TRBankException {
        // TODO: Render Error-Page!
        // TODO: Kundenobjekt an diese Seite übergeben anstatt es zu laden!

        if(pageNumber == null) {
            pageNumber = "1";
        }

        Konto konto = bankingService.getKontoFromKundeById(loginController.getKunde() ,kontoId);
        model.addAttribute("konto", konto);

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);

        model.addAttribute("pageNumber", pageNumber);
        return "konto";
    }
}
