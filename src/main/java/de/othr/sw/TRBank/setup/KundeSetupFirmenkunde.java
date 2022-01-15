package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Adresse;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exception.TRBankException;

public class KundeSetupFirmenkunde extends KundeSetupAbstract{
    @Override
    boolean setup() throws TRBankException {
        if(!kundeService.userWithUsernameExists("TRBank")) {
            kundeService.kundeRegistrieren(new Kunde("TRBank", "passwort", new Adresse("Gewerbepark", "1a", "Regensburg", 93049, "Deutschland"), "Tobias", "Reithmaier", true));
        }
        if(!kundeService.userWithUsernameExists("haberlRepairs")) {
            kundeService.kundeRegistrieren(new Kunde("haberlRepairs", "simon123", new Adresse("Seybothstrasse", "2b", "Regensburg", 93048, "Deutschland"), "Simon", "Haberl", true));
        }
        if(!kundeService.userWithUsernameExists("erbenAirports")) {
            kundeService.kundeRegistrieren(new Kunde("erbenAirports", "emanuel123", new Adresse("Am Fischmarkt", "13", "Regensburg", 93053, "Deutschland"), "Emanuel", "Erben", true));
        }
        if(!kundeService.userWithUsernameExists("hoffmannAirways")) {
            kundeService.kundeRegistrieren(new Kunde("hoffmannAirways", "matteo123", new Adresse("Domstrasse", "35", "Regensburg", 93048, "Deutschland"), "Matteo", "Hoffmann", true));
        }
        if(!kundeService.userWithUsernameExists("gebauerSecurity")) {
            kundeService.kundeRegistrieren(new Kunde("gebauerSecurity", "miriam123", new Adresse("Galgenbergstrasse", "35c", "Regensburg", 93053, "Deutschland"), "Miriam", "Gebauer", true));
        }
        if(!kundeService.userWithUsernameExists("daumCompany")) {
            kundeService.kundeRegistrieren(new Kunde("daumCompany", "passwort", new Adresse("Gewerbepark", "21c", "Regensburg", 93049, "Deutschland"), "Georg", "Huber", true));
        }
        if(!kundeService.userWithUsernameExists("maierCompany")) {
            kundeService.kundeRegistrieren(new Kunde("maierCompany", "passwort", new Adresse("Gewerbepark", "33c", "Regensburg", 93049, "Deutschland"), "Thorsten", "Maier", true));
        }
        return true;
    }
}
