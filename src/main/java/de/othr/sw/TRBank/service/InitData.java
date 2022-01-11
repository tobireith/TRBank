package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.*;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class InitData {
    @Autowired
    private KundeServiceIF kundeService;
    @Autowired
    private BankingServiceIF bankingService;

    @Transactional
    public void initAllData() throws TRBankException {
        Map<String, Kunde> kundeMap = new HashMap<>();

        // Firmenkunden anlegen
        kundeMap.put("TRBank", new Kunde("TRBank", "passwort", new Adresse("Gewerbepark", "1a", "Regensburg", 93049, "Deutschland"), "Tobias", "Reithmaier", true));
        kundeMap.put("haberlRepairs", new Kunde("haberlRepairs", "simon123", new Adresse("Seybothstrasse", "2b", "Regensburg", 93048, "Deutschland"), "Simon", "Haberl", true));
        kundeMap.put("erbenAirports", new Kunde("erbenAirports", "emanuel123", new Adresse("Am Fischmarkt", "13", "Regensburg", 93053, "Deutschland"), "Emanuel", "Erben", true));
        kundeMap.put("gebauerSecurity", new Kunde("gebauerSecurity", "miriam123", new Adresse("Galgenbergstrasse", "35c", "Regensburg", 93053, "Deutschland"), "Miriam", "Gebauer", true));
        kundeMap.put("huberCompany", new Kunde("huberCompany", "passwort", new Adresse("Gewerbepark", "21c", "Regensburg", 93049, "Deutschland"), "Georg", "Huber", true));
        kundeMap.put("maierCompany", new Kunde("maierCompany", "passwort", new Adresse("Gewerbepark", "33c", "Regensburg", 93049, "Deutschland"), "Thorsten", "Maier", true));

        // Privatkunden anlegen
        kundeMap.put("customer1", new Kunde("customer1", "passwort", new Adresse("Am Fischmarkt", "15a, Whg. 13, EG", "Regensburg", 93053, "Deutschland"), "Renate", "Renner", false));
        kundeMap.put("customer2", new Kunde("customer2", "passwort", new Adresse("Donauweg", "33c", "Regensburg", 93049, "Deutschland"), "Thorsten", "Maier", false));
        kundeMap.put("customer3", new Kunde("customer3", "passwort", new Adresse("Landshuter Strasse", "4", "Regensburg", 93049, "Deutschland"), "Angela", "Hofmeister", false));
        kundeMap.put("customer4", new Kunde("customer4", "passwort", new Adresse("Peter-Müllritter-Strasse", "12a, Whg. 3", "Sinzing", 93161, "Deutschland"), "Peter", "Bauer", false));

        // Alle Kunden speichern / registrieren
        for (Kunde kunde : kundeMap.values()) {
            kundeService.kundeRegistrieren(kunde);
        }

        double kontostandFirmenkunde = 1000000 + Math.round(new Random().nextDouble() * 800000);
        double kontostandPrivatkunde = 10000 + Math.round(new Random().nextDouble() * 200000);

        // Konten zu Firmenkunden hinzufügen
        bankingService.kontoAnlegen(new Konto("DE12345678901234500001", kundeMap.get("TRBank"), kontostandFirmenkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500002", kundeMap.get("haberlRepairs"), kontostandFirmenkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500003", kundeMap.get("erbenAirports"), kontostandFirmenkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500004", kundeMap.get("gebauerSecurity"), kontostandFirmenkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500005", kundeMap.get("huberCompany"), kontostandFirmenkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500006", kundeMap.get("maierCompany"), kontostandFirmenkunde));

        // Konten zu Privatkunden hinzufügen
        bankingService.kontoAnlegen(new Konto("DE12345678901234500901", kundeMap.get("customer1"), kontostandPrivatkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500902", kundeMap.get("customer2"), kontostandPrivatkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500903", kundeMap.get("customer3"), kontostandPrivatkunde));
        bankingService.kontoAnlegen(new Konto("DE12345678901234500904", kundeMap.get("customer4"), kontostandPrivatkunde));

        // Zusätzliche Konten erzeugen (0-2 pro Kunde)
        for (Kunde kunde : kundeMap.values()) {
            int anzahlKontos = new Random().nextInt(3);
            for (int j = 1; j <= anzahlKontos; j++) {
                double kontostand = 50000 + Math.round(new Random().nextDouble() * 80000);
                String iban = bankingService.generateRandomIban("DE");
                bankingService.kontoAnlegen(new Konto(iban, kunde, kontostand));
            }
        }

        List<String> keys = new ArrayList<>(kundeMap.keySet());
        //Transaktionen erzeugen, beim Letzten kunden nicht
        for(int i = 0; i < kundeMap.size(); i++) {
            int anzahlTransaktionen = new Random().nextInt(30);
            for (int k = 0; k < anzahlTransaktionen; k++) {
                Kunde vonKunde = kundeService.getAllKunden().get(i);
                Konto vonKonto = vonKunde.getKonten().get(new Random().nextInt(vonKunde.getKonten().size()));
                Konto zuKonto;
                do {
                    String randomKundeKeyZu = keys.get(new Random().nextInt(kundeMap.size()));
                    Kunde randomKundeZu = kundeMap.get(randomKundeKeyZu);
                    zuKonto = randomKundeZu.getKonten().get(new Random().nextInt(randomKundeZu.getKonten().size()));
                } while(zuKonto == vonKonto);
                double betrag = Math.round(new Random().nextDouble() * 1000);
                Date datum = new Date(ThreadLocalRandom
                        .current()
                        .nextLong(new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1) * 365).getTime(), new Date().getTime()));
                String verwendungszweck = "Initiale Überweisung " + k + " von Konto " + vonKonto.getID() + " zu Konto " + zuKonto.getID();
                Transaktion transaktion = new Transaktion(vonKonto, zuKonto, betrag, datum, verwendungszweck);
                bankingService.transaktionTaetigen(transaktion, vonKunde);
            }
        }

        System.out.println("Initialisierung abgeschlossen.");
    }
}
