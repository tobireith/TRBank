package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.*;
import de.othr.sw.TRBank.repository.KontoRepository;
import de.othr.sw.TRBank.repository.KundeRepository;
import de.othr.sw.TRBank.service.impl.KontoServiceImpl;
import de.othr.sw.TRBank.service.impl.KundeServiceImpl;
import de.othr.sw.TRBank.service.impl.TransaktionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Random;

@Service
public class InitService {
    @Autowired
    private KundeServiceImpl kundeService;
    @Autowired
    private KontoServiceImpl kontoService;
    @Autowired
    private TransaktionServiceImpl transaktionService;
    @Autowired
    private KontoRepository kontoRepository;
    @Autowired
    private KundeRepository kundeRepository;

    @Transactional
    public void initData() {
        try {
            for (int i = 1; i <= 5; i++) {
                //Adressen erzeugen
                Adresse adr = new Adresse("Seybothstraße ", String.valueOf(i), "Regensburg", 93053, "Deutschland");
                System.out.println("Adresse erstellt:" + adr);

                //Kunden erzeugen
                Kunde testKunde = new Kunde("Huber" + i, "passwort" + i, adr, "Hans " + i, "Huber " + i, true);
                Kunde kunde = kundeService.kundeRegistrieren(testKunde);
                System.out.println("Kunde erstellt:" + kunde);

                //Konten erzeugen (1-2 pro Kunde)
                int anzahlKontos = new Random().nextInt(2) + 1;
                for (int j = 1; j <= anzahlKontos; j++) {
                    double kontostand = 1000 + Math.round(new Random().nextDouble() * 10000);
                    String iban = "DE" + "1234567890" + String.format("%010d", new Random().nextInt(1000000000));
                    Konto testKonto = new Konto(iban, kunde, kontostand);
                    Konto konto = kontoService.kontoAnlegen(testKonto);
                    System.out.println("Konto erstellt:" + konto);
                }

                //Transaktionen erzeugen (1-5 pro Kunde), beim Letzten kunden nicht
                if (i <= 4) {
                    int anzahlTransaktionen = new Random().nextInt(5) + 1;
                    for (int k = 1; k <= anzahlTransaktionen; k++) {
                        Konto vonKonto = kontoRepository.getKontosByBesitzer(kunde).get(0);
                        Kunde zuKunde = kundeRepository.getAllByVornameAndNachname("Hans " + i, "Huber " + i).get(0);
                        Konto zuKonto = kontoRepository.getKontosByBesitzer(zuKunde).get(0);
                        double betrag = Math.round(new Random().nextDouble() * 1000);
                        Date datum = new Date();
                        String verwendungszweck = "Testüberweisung " + k + " von Konto " + vonKonto.getID();
                        try {
                            Transaktion testTransaktion = new Transaktion(vonKonto, zuKonto, betrag, datum, verwendungszweck);
                            Kunde tempKunde = kundeRepository.getByUsernameAndPasswort("Huber" + i, "passwort" + i);
                            Transaktion t = transaktionService.transaktionTätigen(tempKunde, testTransaktion);
                            System.out.println("Transaktion erstellt:" + t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //TODO: Kontoauszüge erstellen
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
