package de.othr.sw.TRBank.controller.web;

import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Scope("singleton")
@Controller
public class LoginController {
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(
            @RequestParam(value = "error", required = false) Exception exception,
            Model model) {
        if(exception != null) {
            model.addAttribute("trException", new TRBankException("Falscher Username oder Passwort."));
        }
        return "login";
    }
}
