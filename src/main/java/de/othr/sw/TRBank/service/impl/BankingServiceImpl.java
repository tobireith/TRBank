package de.othr.sw.TRBank.service.impl;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.repository.KontoRepository;
import de.othr.sw.TRBank.repository.KontoauszugRepository;
import de.othr.sw.TRBank.repository.TransaktionRepository;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BankingServiceImpl implements BankingServiceIF {
    @Autowired
    private KontoRepository kontoRepository;
    @Autowired
    private KontoauszugRepository kontoauszugRepository;
    @Autowired
    private TransaktionRepository transaktionRepository;
    @Autowired
    private KundeServiceIF kundeService;

    public static int SCHULDENLIMIT = 25000;

    public double transaktionenSummieren(List<Transaktion> transaktionen){
        double sum = 0;
        for(Transaktion element : transaktionen) {
            sum += element.getBetrag();
        }
        return sum;
    }

    @Override
    @Transactional
    public Konto getKontoByIban(String Iban) throws TRBankException {
        return kontoRepository.findKontoByIban(Iban).orElseThrow(() -> new TRBankException("ERROR: Fehler beim Laden des Kontos!"));
    }

    @Override
    @Transactional
    public Konto getKontoById(long kontoId) throws TRBankException {
        return kontoRepository.findById(kontoId).orElseThrow(() -> new TRBankException("ERROR: Fehler beim Laden des Kontos!"));
    }

    @Override
    @Transactional
    public Konto getKontoFromKundeById(Kunde kunde, long kontoId) throws TRBankException {
        return kunde.getKonten().stream().filter(konto -> konto.getID() == kontoId).findFirst().orElseThrow(() -> new TRBankException("ERROR: Fehler beim Laden des Kontos! Das Konto gehört ggf. nicht zum Kunden!"));
        //return kontoRepository.findById(kontoId).orElseThrow(() -> new TRBankException("ERROR: Fehler beim Laden des Kontos!"));
    }

    @Transactional
    @Override
    public Konto kontoSpeichern(Konto konto) {
        /*
        // Erst Konto Speichern, dann Referenz im Kunden updaten!
        Konto savedKonto = kontoRepository.save(konto);
        Kunde kunde = konto.getBesitzer();
        List<Konto> konten = new ArrayList<>(kunde.getKonten());
        if(konten.size() >= 1) {
            konten.remove(konto);
        }
        konten.add(konto);
        kunde.setKonten(konten);

        kundeService.kundeSpeichern(kunde);
        System.out.println("Kunde mit neuen Konten gespeichert: " + kunde.getKonten());
        return savedKonto;
         */

        //konto = kontoRepository.save(konto);
        Kunde kunde = konto.getBesitzer();
        kunde.addKonto(konto);
        kundeService.kundeSpeichern(kunde);
        System.out.println("Kunde mit Konten gespeichert: " + kunde.getKonten());
        return konto;
    }

    @Transactional
    @Override
    public Transaktion transaktionTaetigen(Kunde kunde, Transaktion transaktion) throws TRBankException {
        kunde = kundeService.kundeAnmelden(kunde);

        // TODO: Firmenkunde überprüfung durch Authorities
        if(!kunde.isFirmenkunde() && !kunde.getKonten().contains(transaktion.getQuellkonto())) {
            throw new TRBankException("ERROR: Kunde ist kein Firmenkunde. Quellkonto muss das Konto des Kunden sein.");
        } else if(!kunde.getKonten().contains(transaktion.getQuellkonto()) && !kunde.getKonten().contains(transaktion.getZielkonto())) {
            throw new TRBankException("ERROR: Quell- und Zielkonto gehören nicht dem Kunden!");
        }

        return transaktionTaetigen(transaktion);
    }

    @Transactional
    @Override
    public Transaktion transaktionTaetigen(Transaktion transaktion) throws TRBankException {

        // Für Quell- & Zielkonto sind ggf. nur die IBANs eingetragen -> Lookup durch Service nach diesen IBANs
        transaktion.setQuellkonto(getKontoByIban(transaktion.getQuellkonto().getIban()));
        transaktion.setZielkonto(getKontoByIban(transaktion.getZielkonto().getIban()));

        transaktion.setDatum(new Date());

        Konto von = transaktion.getQuellkonto();
        Konto zu = transaktion.getZielkonto();
        // Prüfen, ob genug Geld auf dem Quellkonto ist
        if (von.getKontostand() < transaktion.getBetrag()) {
            throw (new TRBankException("ERROR: Kontostand zu niedrig"));
        }

        // Transaktion durchführen
        Transaktion t = transaktionRepository.save(transaktion);

        // Transaktionen Liste im Konto anpassen und Kontostände anpassen
        List<Transaktion> transaktionenRaus = new ArrayList<>(von.getTransaktionenRaus());
        transaktionenRaus.add(t);
        von.setTransaktionenRaus(transaktionenRaus);
        von.setKontostand(von.getKontostand() - transaktion.getBetrag());
        this.kontoSpeichern(von);

        List<Transaktion> transaktionenRein = new ArrayList<>(von.getTransaktionenRein());
        transaktionenRein.add(t);
        zu.setTransaktionenRein(transaktionenRein);
        zu.setKontostand(zu.getKontostand() + transaktion.getBetrag());
        this.kontoSpeichern(zu);

        return t;
    }

    private List<Transaktion> transaktionenAbDatum(List<Transaktion> transaktionen, Date datum) {
        return transaktionen
                .stream()
                .filter(transaktion -> transaktion.getDatum().compareTo(datum) > 0)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Kontoauszug kontoauszugErstellen(Konto konto) throws TRBankException {
        Kontoauszug kontoauszug = new Kontoauszug();

        Kontoauszug letzterKontoauszug;
        Transaktion letzteTransaktion;
        List<Transaktion> neueAusgehendeTransaktionen;
        List<Transaktion> neueEingehendeTransaktionen;

        // Letzten Kontoauszug laden, um startKontostand & die letzte Transaktion zu erhalten
        Optional<List<Kontoauszug>> optionalKontoauszugList = kontoauszugRepository.findAllByKontoOrderByDatumBis(konto);

        List<Kontoauszug> kontoauszuege = optionalKontoauszugList.orElse(new ArrayList<>());
        if(kontoauszuege.size() >= 1) {
            letzterKontoauszug = kontoauszuege.get(kontoauszuege.size() - 1);
            kontoauszug.setKontostandAnfang(letzterKontoauszug.getKontostandEnde());
            letzteTransaktion = letzterKontoauszug.getTransaktionen().get(letzterKontoauszug.getTransaktionen().size() - 1);

            // Liste mit NUR neuen Einkommenden & Ausgehenden Transaktionen laden
            neueAusgehendeTransaktionen = transaktionenAbDatum(konto.getTransaktionenRaus(), letzteTransaktion.getDatum());
            neueEingehendeTransaktionen = transaktionenAbDatum(konto.getTransaktionenRein(), letzteTransaktion.getDatum());

        } else {
            kontoauszug.setKontostandAnfang(0);
            neueAusgehendeTransaktionen = konto.getTransaktionenRaus();
            neueEingehendeTransaktionen = konto.getTransaktionenRein();
        }

        // Alle Transaktionen zusammen mischen & nach Datum sortieren
        List<Transaktion> neueTransaktionen = Stream.of(neueAusgehendeTransaktionen, neueEingehendeTransaktionen)
                .flatMap(Collection::stream)
                .sorted((Comparator.comparing(Transaktion::getDatum)))
                .collect(Collectors.toList());
        kontoauszug.setTransaktionen(neueTransaktionen);

        double summeEingehend = transaktionenSummieren(neueEingehendeTransaktionen);
        double summeAusgehend = transaktionenSummieren(neueAusgehendeTransaktionen);
        double kontostandAnfang = konto.getKontostand() - (summeEingehend - summeAusgehend);
        kontoauszug.setKontostandAnfang(kontostandAnfang);
        kontoauszug.setKontostandEnde(konto.getKontostand());

        //TODO: Check this!
        /*
        if(kontoauszug.getKontostandEnde() != konto.getKontostand()) {
            throw new TRBankException("ERROR: Kontostand stimmt nicht mit dem berechneten Wert des aktuellen Kontoauszuges überein");
        }
         */

        System.out.println("KONTOSTAND WIRD ERSTELLT: " + neueTransaktionen);

        if(neueTransaktionen.size() < 1) {
            throw new TRBankException("Keine neuen Transaktionen seit dem letzten Kontoauszug");
        }

        kontoauszug.setDatumVon(neueTransaktionen.get(0).getDatum());
        kontoauszug.setDatumBis(neueTransaktionen.get(neueTransaktionen.size()-1).getDatum());
        kontoauszug.setKonto(konto);

        // Versandunternehmen beauftragen
        //TODO: Aufruf zur externen Schnittstelle des Versandunternehmens
        kontoauszug.setVersandId(new Random().nextInt(10000));

        return kontoauszugRepository.save(kontoauszug);
    }

    @Transactional
    @Override
    public List<Konto> getKontenByKunde(Kunde kunde) throws TRBankException {
        return kontoRepository.findKontosByBesitzerOrderByKontoId(kunde).orElseThrow(() -> new TRBankException("ERROR: Fehler beim Laden der Konten!"));
    }

    @Override
    @Transactional
    public List<Transaktion> getTransaktionenForKonten(List<Konto> konten) {
        List<Transaktion> transaktionenTotal = new ArrayList<>();
        // Alle Konten durchiterieren
        for(Konto konto : konten) {
            // Alle Transaktionen zu dem aktuellen Konto finden
            List<Transaktion> transaktionenFuerKonto = new ArrayList<>();
            Stream.of(konto.getTransaktionenRaus(), konto.getTransaktionenRein()).forEach(transaktionenFuerKonto::addAll);
            for(Transaktion transaktion : transaktionenFuerKonto) {
                // Nur Transaktionen hinzufügen, die noch nicht bereits in der Liste sind
                if(!transaktionenTotal.contains(transaktion)) {
                    transaktionenTotal.add(transaktion);
                }
            }
        }

        // Transaktionen standardmäßig nach Datum sortieren
        transaktionenTotal.sort(Comparator.comparing(Transaktion::getDatum).reversed());

        return transaktionenTotal;
    }

    @Override
    public String generateRandomIban(String prefix) {
        return prefix+"1234567890" + String.format("%010d", new Random().nextInt(1000000000));
    }
}
