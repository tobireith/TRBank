package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Random;

public abstract class KontoSetupAbstract extends SetupComponentAbstract{
    @Autowired
    BankingServiceIF bankingService;
    @Autowired
    KundeServiceIF kundeService;

    BigDecimal generateKontostandFirmenkunde() {
        return BigDecimal.valueOf(new Random().nextDouble() * 8000000 + 25000000.0);
    }
    BigDecimal generateKontostandPrivatkunde() {
        return BigDecimal.valueOf(new Random().nextDouble() * 250000 + 50000.0);
    }

    void generateRandomKonten(Kunde kunde) {
        // Zusätzliche Konten erzeugen (1-4 pro Kunde)
        int anzahlKontos = new Random().nextInt(3) + 1;
        for (int j = 1; j <= anzahlKontos; j++) {
            BigDecimal kontostand;
            if(kunde.isFirmenkunde()) {
                kontostand = generateKontostandFirmenkunde();
            } else {
                kontostand = generateKontostandPrivatkunde();
            }
            String iban = bankingService.generateRandomIban("DE");
            bankingService.kontoAnlegen(new Konto(iban, kunde, kontostand));
        }
    }
}
