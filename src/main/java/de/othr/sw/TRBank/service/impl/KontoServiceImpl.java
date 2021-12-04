package de.othr.sw.TRBank.service.impl;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.repository.KontoRepository;
import de.othr.sw.TRBank.repository.KontoauszugRepository;
import de.othr.sw.TRBank.service.KontoServiceIF;
import de.othr.sw.TRBank.service.TransaktionServiceIF;
import de.othr.sw.TRBank.service.exceptions.KontoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class KontoServiceImpl implements KontoServiceIF {
    @Autowired
    private KontoRepository kontoRepository;
    @Autowired
    private KontoauszugRepository kontoauszugRepository;
    @Autowired
    private TransaktionServiceIF transaktionService;

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
    public Kontoauszug kontoauszugErstellen(Konto konto) throws KontoException {
        Kontoauszug kontoauszug = new Kontoauszug();

        List<Kontoauszug> kontoauszuege = kontoauszugRepository.getAllByKontoOrderByDatumBis(konto);
        Kontoauszug letzterKontoauszug = kontoauszuege.get(kontoauszuege.size() - 1);
        kontoauszug.setKontostandAnfang(letzterKontoauszug.getKontostandEnde());

        List<Transaktion> transaktionen =
                kontoauszugRepository.transaktionenVonKontoauszugSortiertNachDatum(letzterKontoauszug.getKontoauszugId());

        Transaktion letzteTransaktion = transaktionen.get(transaktionen.size() - 1);

        List<Transaktion> neueAusgehendeTransaktionen =
                transaktionService.getAusgehendeTransaktionenAbDatum(konto, letzteTransaktion.getDatum());
        List<Transaktion> neueEingehendeTransaktionen =
                transaktionService.getEinkommendeTransaktionenAbDatum(konto, letzteTransaktion.getDatum());

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
    public Collection<Konto> kontenAuflisten(Kunde kunde) {
        return kontoRepository.getKontosByBesitzer(kunde);
    }
}
