package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.entity.Kunde;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Scope("singleton")
public class RegisterController {

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    private KundeServiceIF kundeService;
    @Autowired
    private BankingServiceIF bankingService;

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register(Model model,
                           RedirectAttributes attributes
    ) {
        TRBankException trBankException = (TRBankException) model.asMap().get("trBankException");
        if (trBankException != null) {
            model.addAttribute("trBankException", trBankException);
        }
        model.addAttribute("kunde", new Kunde());
        return "register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String postRegister(
            @Valid @ModelAttribute("kunde") Kunde kunde,
            BindingResult result,
            @ModelAttribute("passwordWiederholen") String confirmationPasswort,
            HttpServletRequest request,
            Model model,
            RedirectAttributes attributes
    ) {
        TRBankException trBankException = (TRBankException) model.asMap().get("trBankException");
        if (trBankException != null) {
            model.addAttribute("trBankException", trBankException);
        }
        try {
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

            //Auto-Login nach Registrierung
            request.login(kunde.getUsername(), confirmationPasswort);

            return "redirect:/";
        } catch (TRBankException exception) {
            model.addAttribute("trBankException", exception);
            logger.error("An TRBank-Error Occurred: " + exception);
            return "register";
        } catch (ServletException e) {
            attributes.addFlashAttribute("trBankException", new TRBankException("Fehler bei der Anmeldung des neuen Benutzers.", e.getMessage()));
            logger.error("An Servlet-Error Occurred while performing register and login: " + e);
            return "redirect:/login";
        }
    }
}
