package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.exceptions.KontoException;
import de.othr.sw.TRBank.service.exceptions.KundeException;
import de.othr.sw.TRBank.service.exceptions.TransaktionException;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface BankingServiceIF {
    List<Konto> getKontenByKunde(Kunde kunde);
    Kontoauszug kontoauszugErstellen(Konto konto) throws KontoException;

    Konto getKontoByIban(String Iban);
    Konto kontoSpeichern(Konto konto);
    Transaktion transaktionTaetigen(Kunde kunde, Transaktion transaktion) throws TransaktionException, KundeException;

    @Transactional
    List<Transaktion> getTransaktionenForKonten(List<Konto> konten, Pageable pageable);
}
