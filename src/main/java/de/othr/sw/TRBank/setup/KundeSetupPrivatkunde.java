package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Adresse;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exception.TRBankException;

public class KundeSetupPrivatkunde extends KundeSetupAbstract{
    @Override
    boolean setup() throws TRBankException {
        if(!kundeService.userWithUsernameExists("customer1")) {
            kundeService.kundeRegistrieren(new Kunde("customer1", "passwort", new Adresse("Am Fischmarkt", "15a, Whg. 13, EG", "Regensburg", 93053, "Deutschland"), "Renate", "Renner", false));
        }
        if(!kundeService.userWithUsernameExists("customer2")) {
            kundeService.kundeRegistrieren(new Kunde("customer2", "passwort", new Adresse("Donauweg", "33c", "Regensburg", 93049, "Deutschland"), "Thorsten", "Maier", false));
        }
        if(!kundeService.userWithUsernameExists("customer3")) {
            kundeService.kundeRegistrieren(new Kunde("customer3", "passwort", new Adresse("Landshuter Strasse", "4", "Regensburg", 93049, "Deutschland"), "Angela", "Hofmeister", false));
        }
        if(!kundeService.userWithUsernameExists("customer4")) {
            kundeService.kundeRegistrieren(new Kunde("customer4", "passwort", new Adresse("Peter-Müllritter-Strasse", "12a, Whg. 3", "Sinzing", 93161, "Deutschland"), "Peter", "Bauer", false));
        }
        return true;
    }
}
