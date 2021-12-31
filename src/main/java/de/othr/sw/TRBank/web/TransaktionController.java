package de.othr.sw.TRBank.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
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
        System.out.println("GET on /transaktion");
        // TODO: Render Error-Page!
        // TODO: Kundenobjekt an diese Seite übergeben anstatt es zu laden!
        Konto konto = bankingService.getKontoFromKundeById(loginController.getKunde(), kontoId);
        model.addAttribute("konto", konto);

        Transaktion transaktion = new Transaktion();
        transaktion.setQuellkonto(new Konto());
        transaktion.setZielkonto(new Konto());
        if(isSender) {
            transaktion.setQuellkonto(konto);
        } else {
            transaktion.setZielkonto(konto);
        }
        model.addAttribute("transaktion", transaktion);

        System.out.println("GET on /transaktion, providing: " + transaktion);
        System.out.println("GET on /transaktion, providing Quellkonto: " + transaktion.getQuellkonto());

        List<Transaktion> transaktionen = bankingService.getTransaktionenForKonten(List.of(konto));
        model.addAttribute("transaktionen", transaktionen);
        return "transaktion";
    }

    @RequestMapping(value = "/transaktion", method = RequestMethod.POST)    // th:action="@{login}"
    public String postTransaktion(
            @PathVariable long kontoId,
            @Valid @ModelAttribute("transaktion") Transaktion transaktion,
            BindingResult result,
            @ModelAttribute("submit") String submit,
            Model model
    ) throws TRBankException {
        if(submit.equals("Submit")) {
            if (result.hasErrors()) {
                System.out.println("ERROR!");
                //FIXME: Correct display of transaktion with result errors!
                return "transaktion";
            } else {
                System.out.println("NO ERROR!");
            }
            System.out.println("SUBMITTED TRANSAKTION " + transaktion);
            System.out.println("SUBMITTED TRANSAKTION WITH QUELLKONTO " + transaktion.getQuellkonto());

            // TODO: Render Error-Page!
            Konto quellkonto = bankingService.getKontoByIban(transaktion.getQuellkonto().getIban());
            Konto zielkonto = bankingService.getKontoByIban(transaktion.getZielkonto().getIban());

            transaktion.setQuellkonto(quellkonto);
            transaktion.setZielkonto(zielkonto);
            transaktion.setDatum(new Date());

            bankingService.transaktionTaetigen(transaktion);
        } else {
            System.out.println("CANCELED TRANSAKTION");
        }


        return "redirect:/konto/{kontoId}";
    }

}
