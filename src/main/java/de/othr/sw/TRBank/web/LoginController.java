package de.othr.sw.TRBank.web;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;

import java.security.Principal;

@Controller
@SessionScope
public class LoginController {

    @Autowired
    private KundeServiceIF kundeService;

    private Kunde kunde;

    private String username;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        System.out.println("GET /login");
        return "login";
    }
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String postLogin(Model model) {
        System.out.println("POST /login");
        return "redirect:/login/success";
    }

    @RequestMapping(value="login/success")
    public String doLogin(
            Principal principal
    ) throws TRBankException {
        System.out.println("SETTING Kunde for this session...");
        this.kunde = kundeService.getKundeByUsername(principal.getName());
        this.username = principal.getName();
        System.out.println("SET Kunde for this session: " + this.kunde);
        return "redirect:/";
    }

    public Kunde getKunde() {
        try {
            return kundeService.getKundeByUsername(username);
        } catch (TRBankException e) {
            e.printStackTrace();
            return kunde;
        }
    }
}
