package de.othr.sw.TRBank.service.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KontoController {

    @RequestMapping
    public String konto(Model model) {
        //TODO?
        return "konto";
    }
}
