package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Scope("singleton")
public class RegisterController {

    @Autowired
    private KundeServiceIF kundeService;

    @Autowired
    private BankingServiceIF bankingService;

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("kunde", new Kunde());
        System.out.println("GET /register");
        return "register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String postRegister(
            @Valid @ModelAttribute("kunde") Kunde kunde,
            BindingResult result,
            @ModelAttribute("passwordWiederholen") String confirmationPasswort,
            HttpServletRequest request,
            Model model) {
        try {
            System.out.println("POST /register");
            if (!confirmationPasswort.equals(kunde.getPasswort())) {
                result.addError(new ObjectError("globalError", "Die beiden eingegebenen Passwörter stimmen nicht überein."));
            }

            if (result.hasErrors()) {
                return "register";
            }

            // Kunden speichern
            kundeService.kundeRegistrieren(kunde);

            // Standard-Konto für Kunden anlegen
            bankingService.kontoAnlegen(kunde);
            model.addAttribute("alertSuccessTitle", "Registrierung erfolgreich.");
            model.addAttribute("alertSuccessMessage", "Sie wurden erfolgreich als Kunde registriert und ein Konto wurde automatisch für Sie angelegt. Sie werden nun automatisch angemeldet.");

            //Auto-Login nach Registrierung
            request.login(kunde.getUsername(), confirmationPasswort);

            return "redirect:/";
        } catch (TRBankException exception) {
            model.addAttribute("trException", exception);
            return "register";
        } catch (ServletException e) {
            model.addAttribute("trException", new TRBankException("Fehler bei der Anmeldung des neuen Benutzers.", e.getMessage()));
            return "redirect:/login";
        }
    }
}
