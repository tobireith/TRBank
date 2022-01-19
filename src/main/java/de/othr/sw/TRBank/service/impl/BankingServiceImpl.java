package de.othr.sw.TRBank.service.impl;

import de.othr.sw.TRBank.controller.rest.SendDeliveryIF;
import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.entity.dto.TransaktionDTO;
import de.othr.sw.TRBank.repository.KontoRepository;
import de.othr.sw.TRBank.repository.KontoauszugRepository;
import de.othr.sw.TRBank.repository.TransaktionRepository;
import de.othr.sw.TRBank.service.BankingServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.othr.sw.TRBank.entity.Konto.SCHULDENLIMIT;

@Service
public class BankingServiceImpl implements BankingServiceIF {
    @Autowired
    private KontoRepository kontoRepository;
    @Autowired
    private KontoauszugRepository kontoauszugRepository;
    @Autowired
    private TransaktionRepository transaktionRepository;
    @Autowired
    private SendDeliveryIF sendDelivery;

    private final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);


    public BigDecimal transaktionenSummieren(List<Transaktion> transaktionen){
        BigDecimal sum = new BigDecimal("0.0");
        for(Transaktion element : transaktionen) {
            sum = sum.add(element.getBetrag());
        }
        return sum;
    }

    @Override
    @Transactional (Transactional.TxType.SUPPORTS)
    public Konto getKontoByIban(String Iban) throws TRBankException {
        return kontoRepository.findKontoByIban(Iban).orElseThrow(() -> new TRBankException("Fehler beim Laden des Kontos! IBAN nicht gefunden: " + Iban));
    }

    @Override
    @Transactional (Transactional.TxType.SUPPORTS)
    public boolean kontoWithIbanExists(String Iban) {
        return kontoRepository.findKontoByIban(Iban).isPresent();
    }

    @Override
    @Transactional (Transactional.TxType.SUPPORTS)
    public Konto getKontoById(long kontoId) throws TRBankException {
        return kontoRepository.findById(kontoId).orElseThrow(() -> new TRBankException("Fehler beim Laden des Kontos!"));
    }

    @Override
    public Konto getKontoFromKundeById(Kunde kunde, long kontoId) throws TRBankException {
        return kunde.getKonten().stream().filter(konto -> konto.getID() == kontoId).findFirst().orElseThrow(() -> new TRBankException("Fehler beim Laden des Kontos! Das Konto gehört ggf. nicht zum Kunden!"));
        //return kontoRepository.findById(kontoId).orElseThrow(() -> new TRBankException("ERROR: Fehler beim Laden des Kontos!"));
    }

    @Override
    public Konto kontoAnlegen(Kunde kunde) {
        Konto konto = new Konto(generateRandomIban(kunde.getAdresse().getLand().substring(0, 2).toUpperCase()), kunde, new BigDecimal("0.0"));
        return kontoAnlegen(konto);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public Konto kontoAnlegen(@Valid Konto konto) {
        // Konto speichern, das gespeicherte Konto wird in der Liste des Kunden automatisch hinzugefügt
        konto = kontoRepository.save(konto);
        return konto;
    }

    @Transactional (Transactional.TxType.REQUIRED)
    @Override
    public Konto kontoUpdaten(@Valid Konto konto) {
        konto = kontoRepository.save(konto);
        return konto;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public void kontoLoeschen(long kontoId) throws TRBankException {
        Konto konto = kontoRepository.findById(kontoId).orElseThrow(() -> new TRBankException("Konto zum löschen nicht gefunden."));
        if(konto.getKontostand().compareTo(new BigDecimal("0.0")) != 0) {
            throw new TRBankException("Kontostand muss gleich 0 sein.", "Kontostand muss gleich 0 sein. Aktueller Kontostand: " + konto.getKontostand(), "Überweisen sie Ihr restliches Geld auf ein anderes Konto, oder begleichen Sie Ihre Schulden.");
        }
        for(Transaktion t : konto.getTransaktionenRaus()) {
            t.setQuellkonto(null);
            transaktionRepository.save(t);
        }
        for(Transaktion t : konto.getTransaktionenRein()) {
            t.setZielkonto(null);
            transaktionRepository.save(t);
        }
        kontoRepository.deleteById(kontoId);
        logger.info("Konto wurde erfolgreich gelöscht.");
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public Transaktion transaktionSpeichern(@Valid Transaktion transaktion) {
        return transaktionRepository.save(transaktion);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public Transaktion transaktionTaetigen(@Valid TransaktionDTO transaktionDTO, @Valid Kunde kunde) throws TRBankException {
        Transaktion transaktion =  new Transaktion();

        transaktion.setBetrag(transaktionDTO.getBetrag());
        transaktion.setVerwendungszweck(transaktionDTO.getVerwendungszweck());
        transaktion.setDatum(new Date());

        // Für Quell- & Zielkonto sind nur die IBANs eingetragen → Lookup durch Service nach diesen IBANs
        transaktion.setQuellkonto(getKontoByIban(transaktionDTO.getQuellIban()));
        transaktion.setZielkonto(getKontoByIban(transaktionDTO.getZielIban()));

        Konto von = transaktion.getQuellkonto();
        Konto zu = transaktion.getZielkonto();

        List<Konto> konten = getKontenByKunde(kunde);
        if(!kunde.isFirmenkunde() && !konten.contains(von)) {
            throw new TRBankException("Kunde ist kein Firmenkunde. Quellkonto muss das Konto des Kunden sein.");
        }
        if(!konten.contains(von) && !konten.contains(zu)) {
            throw new TRBankException("Quell- und Zielkonto gehören nicht dem Kunden!");
        }
        if(von == zu) {
            throw new TRBankException("Quell- und Zielkonto müssen unterschiedlich sein.");
        }

        // Prüfen, ob genug Geld auf dem Quellkonto ist
        if (von.getKontostand().subtract(transaktion.getBetrag()).compareTo(new BigDecimal(SCHULDENLIMIT)) < 0) {
            throw (new TRBankException("Kontostand zu niedrig.", "Der Kontostand des Quellkontos ist zu niedrig um den Betrag zu decken und würde das Schuldenlimit des Kontos übersteigen."));
        }

        // Transaktion durchführen
        transaktion = transaktionRepository.save(transaktion);

        // Kontostände anpassen & Änderungen speichern
        von.setKontostand(von.getKontostand().subtract(transaktion.getBetrag()));
        kontoUpdaten(von);
        zu.setKontostand(zu.getKontostand().add(transaktion.getBetrag()));
        kontoUpdaten(zu);

        logger.info("Transaktion wurde erfolgreich erstellt.");
        return transaktion;
    }

    private List<Transaktion> transaktionenAbDatum(List<Transaktion> transaktionen, Date datum) {
        return transaktionen
                .stream()
                .filter(transaktion -> transaktion.getDatum().compareTo(datum) > 0)
                .collect(Collectors.toList());
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public Kontoauszug kontoauszugErstellen(@Valid Konto konto) throws TRBankException {
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
            kontoauszug.setKontostandAnfang(new BigDecimal("0.0"));
            neueAusgehendeTransaktionen = konto.getTransaktionenRaus();
            neueEingehendeTransaktionen = konto.getTransaktionenRein();
        }

        // Alle Transaktionen zusammen mischen & nach Datum sortieren
        List<Transaktion> neueTransaktionen = Stream.of(neueAusgehendeTransaktionen, neueEingehendeTransaktionen)
                .flatMap(Collection::stream)
                .sorted((Comparator.comparing(Transaktion::getDatum)))
                .collect(Collectors.toList());
        kontoauszug.setTransaktionen(neueTransaktionen);

        BigDecimal summeEingehend = transaktionenSummieren(neueEingehendeTransaktionen);
        BigDecimal summeAusgehend = transaktionenSummieren(neueAusgehendeTransaktionen);
        BigDecimal kontostandAnfang = konto.getKontostand().subtract( summeEingehend.subtract(summeAusgehend) );
        kontoauszug.setKontostandAnfang(kontostandAnfang);
        kontoauszug.setKontostandEnde(konto.getKontostand());

        if(neueTransaktionen.size() < 1) {
            throw new TRBankException("Keine neuen Transaktionen seit dem letzten Kontoauszug.");
        }

        kontoauszug.setDatumVon(neueTransaktionen.get(0).getDatum());
        kontoauszug.setDatumBis(neueTransaktionen.get(neueTransaktionen.size()-1).getDatum());
        kontoauszug.setKonto(konto);

        /*
        try {
            // Versandunternehmen beauftragen
            TempDelivery delivery = sendDelivery.sendDelivery(new TempDeliveryDTO(new Kunde("username", "password"), new TempDelivery()));
            kontoauszug.setVersandId(delivery.getDeliveryId());
        } catch (Exception e) {
            throw new TRBankException("Fehler bei der Erstellung des Sendungsauftrages.", e.getMessage());
            logger.error("An Error occurred while calling the external Delivery-System: " + e);
        }
         */
        kontoauszug.setVersandId(new Random().nextInt(99999999));

        kontoauszug = kontoauszugRepository.save(kontoauszug);
        logger.info("Kontoauszug wurde erfolgreich erstellt.");

        return kontoauszug;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<Konto> getKontenByKunde(Kunde kunde) throws TRBankException {
        return kontoRepository.findKontosByBesitzerOrderByKontoId(kunde).orElseThrow(() -> new TRBankException("Fehler beim Laden der Konten!"));
    }

    @Override
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

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<Transaktion> getAllTransaktionen() {
        List<Transaktion> transaktionen = new ArrayList<>();
        transaktionRepository.findAll().forEach(transaktionen::add);
        return transaktionen;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public String generateRandomIban(String prefix) {
        String iban;
        Konto konto;
        do {
            iban = prefix+"1234567890" + String.format("%010d", new Random().nextInt(1000000000));
            konto = kontoRepository.findKontoByIban(iban).orElse(null);
        } while (konto != null);
        return iban;
    }
}
