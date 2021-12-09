package de.othr.sw.TRBank.service.impl;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.repository.TransaktionRepository;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.TransaktionServiceIF;
import de.othr.sw.TRBank.service.exceptions.KundeException;
import de.othr.sw.TRBank.service.exceptions.TransaktionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class TransaktionServiceImpl /*implements TransaktionServiceIF */ {
    /*


    @Autowired
    private TransaktionRepository transaktionRepository;
    @Autowired
    private KundeServiceIF kundeService;
    /*
    @Autowired
    private KontoServiceIF kontoService;


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


    @Transactional
    @Override
    public Transaktion transaktionTätigen(Kunde kunde, Transaktion t) throws TransaktionException, KundeException {
        kundeService.kundeAnmelden(kunde);

        //TODO
        // Für Quell- & Zielkonto sind ggf. nur die IBANs eingetragen -> Lookup durch Service nach diesen IBANs
        t.setQuellkonto(kontoService.getKontoByIban(t.getQuellkonto().getIban()));
        t.setZielkonto(kontoService.getKontoByIban(t.getZielkonto().getIban()));

        if(!kunde.isFirmenkunde() && !kunde.getKonten().contains(t.getQuellkonto())) {
            throw new TransaktionException(t, 2, "ERROR: Kunde ist kein Firmenkunde. Quellkonto muss das Konto des Kunden sein.");
        } else if(!kunde.getKonten().contains(t.getQuellkonto()) || !kunde.getKonten().contains(t.getZielkonto())) {
            throw new TransaktionException(t, 3, "ERROR: Quell- und Zielkonto gehören nicht dem Kunden!");
        }

        Konto von = t.getQuellkonto();
        Konto zu = t.getZielkonto();
        // Prüfen, ob genug Geld auf dem Quellkonto ist
        if (von.getKontostand() < t.getBetrag()) {
            throw (new TransaktionException(t, 1, "ERROR: Kontostand zu niedrig"));
        }

        // Kontostände anpassen
        von.setKontostand(von.getKontostand() - t.getBetrag());
        kontoService.saveKonto(von);
        zu.setKontostand(zu.getKontostand() + t.getBetrag());
        kontoService.saveKonto(zu);
        return transaktionRepository.save(t);
    }

    @Transactional
    @Override
    public List<Transaktion> getAllTransaktionenForKonto(Konto konto) {
        return transaktionRepository.getAllByQuellkontoOrZielkontoOrderByDatum(konto, konto);
    }

    */
}
