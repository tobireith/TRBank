package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

public abstract class KontoSetupAbstract extends SetupComponentAbstract{
    @Autowired
    BankingServiceIF bankingService;
    @Autowired
    KundeServiceIF kundeService;

    BigDecimal generateKontostandFirmenkunde() {
        return BigDecimal.valueOf(new Random().nextDouble() * 800000 + 1000000.0).round(new MathContext(3, RoundingMode.HALF_UP));
    }
    BigDecimal generateKontostandPrivatkunde() {
        return BigDecimal.valueOf(new Random().nextDouble() * 200000 + 10000.0).round(new MathContext(3, RoundingMode.HALF_UP));
    }

    void generateRandomKonten(Kunde kunde) {
        // Zusätzliche Konten erzeugen (0-3 pro Kunde)
        int anzahlKontos = new Random().nextInt(4);
        for (int j = 1; j <= anzahlKontos; j++) {
            BigDecimal kontostand = BigDecimal.valueOf(new Random().nextDouble() * 80000 + 5000.0).round(new MathContext(3, RoundingMode.HALF_UP));
            String iban = bankingService.generateRandomIban("DE");
            bankingService.kontoAnlegen(new Konto(iban, kunde, kontostand));
        }
    }
}
