package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public abstract class KontoSetupAbstract extends SetupComponentAbstract{
    @Autowired
    BankingServiceIF bankingService;
    @Autowired
    KundeServiceIF kundeService;

    double generateKontostandFirmenkunde() {
        return 1000000.0 + Math.round(new Random().nextDouble() * 800000);
    }
    double generateKontostandPrivatkunde() {
        return 10000.0 + Math.round(new Random().nextDouble() * 200000);
    }

    void generateRandomKonten(Kunde kunde) {
        // Zusätzliche Konten erzeugen (0-3 pro Kunde)
        int anzahlKontos = new Random().nextInt(4);
        for (int j = 1; j <= anzahlKontos; j++) {
            double kontostand = 50000 + Math.round(new Random().nextDouble() * 80000);
            String iban = bankingService.generateRandomIban("DE");
            bankingService.kontoAnlegen(new Konto(iban, kunde, kontostand));
        }
    }
}
