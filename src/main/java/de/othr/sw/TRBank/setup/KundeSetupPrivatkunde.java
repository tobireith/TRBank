package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Adresse;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exception.TRBankException;

public class KundeSetupPrivatkunde extends KundeSetupAbstract{
    @Override
    boolean setup() throws TRBankException {
        if(!kundeService.userWithUsernameExists("rennerRenate")) {
            kundeService.kundeRegistrieren(new Kunde("rennerRenate", "renate123", new Adresse("Am Fischmarkt", "15a, Whg. 13, EG", "Regensburg", "93053", "Deutschland"), "Renate", "Renner", false));
        }
        if(!kundeService.userWithUsernameExists("fischerBruno")) {
            kundeService.kundeRegistrieren(new Kunde("fischerBruno", "bruno123", new Adresse("Donauweg", "33c", "Regensburg", "93049", "Deutschland"), "Bruno", "Fischer", false));
        }
        if(!kundeService.userWithUsernameExists("hofmeisterAngela")) {
            kundeService.kundeRegistrieren(new Kunde("hofmeisterAngela", "angela123", new Adresse("Landshuter Strasse", "4", "Regensburg", "93049", "Deutschland"), "Angela", "Hofmeister", false));
        }
        if(!kundeService.userWithUsernameExists("bauerPeter")) {
            kundeService.kundeRegistrieren(new Kunde("bauerPeter", "peter123", new Adresse("Peter-Müllritter-Strasse", "12a, Whg. 3", "Sinzing", "93161", "Deutschland"), "Peter", "Bauer", false));
        }
        return true;
    }
}
