package de.othr.sw.TRBank.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@SessionScope
public class RegisterController {

    @Autowired
    private KundeServiceIF kundeService;

    @Autowired
    private BankingServiceIF bankingService;


    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("kunde", new Kunde());
        System.out.println("GET /register");
        return "register";
    }

    @Transactional
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String postLogin(
            @Valid @ModelAttribute("kunde") Kunde kunde,
            BindingResult result,
            @ModelAttribute("passwordWiederholen") String confirmationPasswort,
            Model model) throws TRBankException {
        //TODO: Error-Handling
        System.out.println("POST /register");
        if (result.hasErrors()) {
            return "register";
        }
        if(!confirmationPasswort.equals(kunde.getPasswort())) {
            throw new TRBankException("Die beiden eingegebenen Passwörter sind nicht gleich!");
        }

        // Kunden speichern
        kunde = kundeService.kundeRegistrieren(kunde);

        // Standard-Konto für Kunden anlegen
        String iban = bankingService.generateRandomIban(kunde.getAdresse().getLand().substring(0, 2).toUpperCase());
        Konto konto = new Konto(iban, kunde, 0);
        bankingService.kontoSpeichern(konto);

        System.out.println("New customer registered: " + kunde);

        return "redirect:/login";
    }
}
