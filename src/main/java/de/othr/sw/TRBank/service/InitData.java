package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.*;
import de.othr.sw.TRBank.repository.KontoRepository;
import de.othr.sw.TRBank.repository.KundeRepository;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Random;

@Component
public class InitData {
    @Autowired
    private KundeServiceIF kundeService;
    @Autowired
    private BankingServiceIF bankingService;
    @Autowired
    private KontoRepository kontoRepository;
    @Autowired
    private KundeRepository kundeRepository;

    // TODO: Initialize with real data!

    @Transactional
    public void initData() throws TRBankException {
        for (int i = 1; i <= 5; i++) {
            //Adressen erzeugen
            Adresse adr = new Adresse("Seybothstrasse ", String.valueOf(i), "Regensburg", 93053, "Deutschland");
            System.out.println("Adresse erstellt:" + adr);

            //Kunde erzeugen
            Kunde testKunde = new Kunde("Huber" + i, "passwort" + i, adr, "Hans " + i, "Huber " + i, true);
            testKunde = kundeService.kundeRegistrieren(testKunde);
            System.out.println("Kunde erstellt:" + testKunde);

            //Konten erzeugen (1-3 pro Kunde)
            int anzahlKontos = new Random().nextInt(3) + 1;
            for (int j = 1; j <= anzahlKontos; j++) {
                double kontostand = 1000 + Math.round(new Random().nextDouble() * 10000);
                String iban = "DE" + "1234567890" + String.format("%010d", new Random().nextInt(1000000000));
                Konto testKonto = new Konto(iban, testKunde, kontostand);
                Konto konto = bankingService.kontoSpeichern(testKonto);
                System.out.println("Konto erstellt:" + konto);
            }
        }
        //Transaktionen erzeugen (1-5 pro Kunde), beim Letzten kunden nicht
        for(int i = 1; i <= 4; i++) {
            int anzahlTransaktionen = new Random().nextInt(5) + 1;
            for (int k = 1; k <= anzahlTransaktionen; k++) {
                System.out.println("1");
                Kunde vonKunde = kundeRepository.getAllByVornameAndNachname("Hans " + i, "Huber " + i).get(0);
                Konto vonKonto = kontoRepository.getKontosByBesitzerOrderByKontoId(vonKunde).get(0);
                System.out.println("2");
                Kunde zuKunde = kundeRepository.getAllByVornameAndNachname("Hans " + (i+1), "Huber " + (i+1)).get(0);
                System.out.println("KONTEN VON " + zuKunde.getVorname() + " | " + zuKunde.getKonten());
                Konto zuKonto = kontoRepository.getKontosByBesitzerOrderByKontoId(zuKunde).get(0);
                System.out.println("3");
                double betrag = Math.round(new Random().nextDouble() * 1000);
                Date datum = new Date();
                String verwendungszweck = "Testüberweisung " + k + " von Konto " + vonKonto.getID();
                Transaktion testTransaktion = new Transaktion(vonKonto, zuKonto, betrag, datum, verwendungszweck);
                Kunde tempKunde = new Kunde("Huber" + i, "passwort" + i);
                System.out.println("TRANSAKTION:" + testTransaktion + " KUNDE: " + tempKunde);
                Transaktion t = bankingService.transaktionTaetigen(tempKunde, testTransaktion);
                System.out.println("Transaktion erstellt:" + t);
            }
        }
        //TODO: Kontoauszüge erstellen
    }
}
