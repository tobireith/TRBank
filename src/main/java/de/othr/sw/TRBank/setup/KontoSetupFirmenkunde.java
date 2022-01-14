package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exception.TRBankException;

import javax.transaction.Transactional;

public class KontoSetupFirmenkunde extends KontoSetupAbstract{
    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    boolean setup() throws TRBankException {
        // Konten für Firmenkunden hinzufügen
        if(!bankingService.kontoWithIbanExists("DE12345678901234500001")) {
            Kunde kunde = kundeService.getKundeByUsername("TRBank");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500001", kunde, generateKontostandFirmenkunde()));
            generateRandomKonten(kunde);
        }
        if(!bankingService.kontoWithIbanExists("DE12345678901234500002")) {
            Kunde kunde = kundeService.getKundeByUsername("haberlRepairs");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500002", kunde, generateKontostandFirmenkunde()));
            generateRandomKonten(kunde);
        }
        if(!bankingService.kontoWithIbanExists("DE12345678901234500003")) {
            Kunde kunde = kundeService.getKundeByUsername("erbenAirports");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500003", kunde, generateKontostandFirmenkunde()));
            generateRandomKonten(kunde);
        }
        if(!bankingService.kontoWithIbanExists("DE12345678901234500004")) {
            Kunde kunde = kundeService.getKundeByUsername("gebauerSecurity");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500004", kunde, generateKontostandFirmenkunde()));
            generateRandomKonten(kunde);
        }
        if(!bankingService.kontoWithIbanExists("DE12345678901234500005")) {
            Kunde kunde = kundeService.getKundeByUsername("huberCompany");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500005", kunde, generateKontostandFirmenkunde()));
            generateRandomKonten(kunde);
        }
        if(!bankingService.kontoWithIbanExists("DE12345678901234500006")) {
            Kunde kunde = kundeService.getKundeByUsername("maierCompany");
            bankingService.kontoAnlegen(new Konto("DE12345678901234500006", kunde, generateKontostandFirmenkunde()));
            generateRandomKonten(kunde);
        }
        return true;
    }
}
