package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exception.TRBankException;

public class KontoSetupPrivatkunde extends KontoSetupAbstract{
    @Override
    boolean setup() throws TRBankException {
        // Konten für Privatkunden hinzufügen
        if (!bankingService.kontoWithIbanExists("DE12345678901234500901")) {
            Kunde kunde = kundeService.getKundeByUsername("rennerRenate");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500901", kunde, generateKontostandPrivatkunde()));
            generateRandomKonten(kunde);
        }
        if (!bankingService.kontoWithIbanExists("DE12345678901234500902")) {
            Kunde kunde = kundeService.getKundeByUsername("fischerBruno");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500902", kunde, generateKontostandPrivatkunde()));
            generateRandomKonten(kunde);
        }
        if (!bankingService.kontoWithIbanExists("DE12345678901234500903")) {
            Kunde kunde = kundeService.getKundeByUsername("hofmeisterAngela");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500903", kunde, generateKontostandPrivatkunde()));
            generateRandomKonten(kunde);
        }
        if (!bankingService.kontoWithIbanExists("DE12345678901234500904")) {
            Kunde kunde = kundeService.getKundeByUsername("bauerPeter");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500904", kunde, generateKontostandPrivatkunde()));
            generateRandomKonten(kunde);
        }
        return true;
    }
}
