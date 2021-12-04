package de.othr.sw.TRBank.service.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {

    @RequestMapping(value = "/")
    public String start() {
        return "index";
    }
}
