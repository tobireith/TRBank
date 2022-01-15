package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TransaktionSetup extends SetupComponentAbstract{
    @Autowired
    KundeServiceIF kundeService;

    @Autowired
    BankingServiceIF bankingService;


    @Override
    boolean setup() throws TRBankException {
        //Nur wenn bisher keine Transaktionen vorhanden sind
        if (bankingService.getAllTransaktionen().size() <= 5) {
            List<Kunde> allKunden = kundeService.getAllKunden();
            //Transaktionen für jeden Kunden erstellen
            for (Kunde vonKunde : allKunden) {
                int anzahlTransaktionen = new Random().nextInt(25);
                for (int k = 0; k < anzahlTransaktionen; k++) {
                    List<Konto> vonKonten = bankingService.getKontenByKunde(vonKunde);
                    Konto vonKonto = vonKonten.get(new Random().nextInt(vonKonten.size()));

                    //Konto vonKonto = vonKunde.getKonten().get(new Random().nextInt(vonKunde.getKonten().size()));
                    Konto zuKonto;
                    do {
                        Kunde zuKunde = allKunden.get(new Random().nextInt(allKunden.size()));

                        List<Konto> zuKonten = bankingService.getKontenByKunde(zuKunde);
                        zuKonto = zuKonten.get(new Random().nextInt(zuKonten.size()));

                        //zuKonto = zuKunde.getKonten().get(new Random().nextInt(zuKunde.getKonten().size()));
                    } while (Objects.equals(zuKonto.getID(), vonKonto.getID()));
                    double betrag = Math.round(new Random().nextDouble() * 1000);
                    Date datum = new Date(ThreadLocalRandom
                            .current()
                            .nextLong(new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1) * 365).getTime(), new Date().getTime()));
                    String verwendungszweck = "Setup Überweisung " + " von Konto " + vonKonto.getID() + " zu Konto " + zuKonto.getID();
                    Transaktion transaktion = new Transaktion(vonKonto, zuKonto, betrag, datum, verwendungszweck);
                    bankingService.transaktionTaetigen(transaktion, vonKunde);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
