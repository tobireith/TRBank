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
import de.othr.sw.TRBank.service.TransaktionServiceIF;
import de.othr.sw.TRBank.service.exceptions.KontoException;
import de.othr.sw.TRBank.service.exceptions.KundeException;
import de.othr.sw.TRBank.service.exceptions.TransaktionException;
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

    @Override
    @Transactional
    public Konto kontoAnlegen(Konto k) {
        return kontoRepository.save(k);
    }

    public double transaktionenSummieren(List<Transaktion> transaktionen){
        double sum = 0;
        for(Transaktion element : transaktionen) {
            sum += element.getBetrag();
        }
        return sum;
    }

    @Override
    @Transactional
    public Konto getKontoByIban(String Iban) {
        return kontoRepository.getKontoByIban(Iban);
    }

    @Transactional
    @Override
    public Konto saveKonto(Konto konto) {
        return kontoRepository.save(konto);
    }

    @Override
    public Transaktion transaktionTaetigen(Kunde kunde, Transaktion transaktion) throws TransaktionException, KundeException {
        kundeService.kundeAnmelden(kunde);

        // Für Quell- & Zielkonto sind ggf. nur die IBANs eingetragen -> Lookup durch Service nach diesen IBANs
        transaktion.setQuellkonto(this.getKontoByIban(transaktion.getQuellkonto().getIban()));
        transaktion.setZielkonto(this.getKontoByIban(transaktion.getZielkonto().getIban()));

        if(!kunde.isFirmenkunde() && !kunde.getKonten().contains(transaktion.getQuellkonto())) {
            throw new TransaktionException(transaktion, 2, "ERROR: Kunde ist kein Firmenkunde. Quellkonto muss das Konto des Kunden sein.");
        } else if(!kunde.getKonten().contains(transaktion.getQuellkonto()) || !kunde.getKonten().contains(transaktion.getZielkonto())) {
            throw new TransaktionException(transaktion, 3, "ERROR: Quell- und Zielkonto gehören nicht dem Kunden!");
        }

        Konto von = transaktion.getQuellkonto();
        Konto zu = transaktion.getZielkonto();
        // Prüfen, ob genug Geld auf dem Quellkonto ist
        if (von.getKontostand() < transaktion.getBetrag()) {
            throw (new TransaktionException(transaktion, 1, "ERROR: Kontostand zu niedrig"));
        }

        // Transaktion durchführen
        Transaktion t = transaktionRepository.save(transaktion);

        // Kontostände anpassen
        von.setKontostand(von.getKontostand() - transaktion.getBetrag());
        this.saveKonto(von);
        zu.setKontostand(zu.getKontostand() + transaktion.getBetrag());
        this.saveKonto(zu);

        return t;
    }

    @Transactional
    @Override
    public Kontoauszug kontoauszugErstellen(Konto konto) throws KontoException {
        Kontoauszug kontoauszug = new Kontoauszug();

        List<Kontoauszug> kontoauszuege = kontoauszugRepository.getAllByKontoOrderByDatumBis(konto);
        Kontoauszug letzterKontoauszug = kontoauszuege.get(kontoauszuege.size() - 1);
        kontoauszug.setKontostandAnfang(letzterKontoauszug.getKontostandEnde());

        List<Transaktion> transaktionen =
                kontoauszugRepository.transaktionenVonKontoauszugSortiertNachDatum(letzterKontoauszug.getKontoauszugId());

        Transaktion letzteTransaktion = transaktionen.get(transaktionen.size() - 1);

        List<Transaktion> neueAusgehendeTransaktionen =
                this.getAusgehendeTransaktionenAbDatum(konto, letzteTransaktion.getDatum());
        List<Transaktion> neueEingehendeTransaktionen =
                this.getEinkommendeTransaktionenAbDatum(konto, letzteTransaktion.getDatum());

        List<Transaktion> neueTransaktionen = Stream.of(neueAusgehendeTransaktionen, neueEingehendeTransaktionen)
                .flatMap(Collection::stream)
                .sorted((Comparator.comparing(Transaktion::getDatum)))
                .collect(Collectors.toList());
        kontoauszug.setTransaktionen(neueTransaktionen);

        double summeEingehend = transaktionenSummieren(neueEingehendeTransaktionen);
        double summeAusgehend = transaktionenSummieren(neueAusgehendeTransaktionen);
        double kontostandEnde = letzterKontoauszug.getKontostandEnde() + summeEingehend - summeAusgehend;
        kontoauszug.setKontostandEnde(kontostandEnde);

        kontoauszug.setDatumVon(neueTransaktionen.get(0).getDatum());
        kontoauszug.setDatumBis(neueTransaktionen.get(neueTransaktionen.size()-1).getDatum());
        kontoauszug.setKonto(konto);

        // Versandunternehmen beauftragen
        //TODO: Aufruf zur externen Schnittstelle des Versandunternehmens
        kontoauszug.setVersandId(new Random().nextInt(10000));

        if(kontoauszug.getKontostandEnde() != konto.getKontostand()) {
            throw new KontoException(1, "ERROR: Kontostand stimmt nicht mit dem berechneten Wert überein", konto, kontoauszug);
        }

        return kontoauszugRepository.save(kontoauszug);
    }

    @Transactional
    @Override
    public List<Konto> getKontenByKunde(Kunde kunde) {
        return kontoRepository.getKontosByBesitzerOrderByKontoId(kunde);
    }

    @Override
    @Transactional
    public List<Transaktion> getTransaktionenForKonten(List<Konto> konten, Pageable pageable) {
        List<Transaktion> transaktionenTotal = new ArrayList<>();
        // Alle Konten durchiterieren
        for(Konto konto : konten) {
            // Alle Transaktionen zu dem aktuellen Konto finden
            List<Transaktion> transaktionenFuerKonto = transaktionRepository.getAllByQuellkontoOrZielkontoOrderByDatum(konto, konto, pageable);
            for(Transaktion transaktion : transaktionenFuerKonto) {
                // Nur Transaktionen hinzufügen, die noch nicht bereits in der Liste sind
                if(!transaktionenTotal.contains(transaktion)) {
                    transaktionenTotal.add(transaktion);
                }
            }
        }

        // Transaktionen standardmäßig nach Datum sortieren
        transaktionenTotal.sort(Comparator.comparing(Transaktion::getDatum));

        return transaktionenTotal;
    }

    @Override
    @Transactional
    public List<Transaktion> getEinkommendeTransaktionenAbDatum(Konto konto, Date datum) {
        return transaktionRepository.getAllByZielkontoAndDatumBeforeOrderByDatum(konto, datum);
    }
    @Override
    @Transactional
    public List<Transaktion> getAusgehendeTransaktionenAbDatum(Konto konto, Date datum) {
        return transaktionRepository.getAllByQuellkontoAndDatumBeforeOrderByDatum(konto, datum);
    }
}
