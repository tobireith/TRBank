package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

public interface BankingServiceIF {
    List<Konto> getKontenByKunde(Kunde kunde) throws TRBankException;
    Kontoauszug kontoauszugErstellen(Konto konto) throws TRBankException;

    Konto getKontoByIban(String Iban) throws TRBankException;
    Konto getKontoById (long kontoId)  throws TRBankException;

    Konto kontoSpeichern(Konto konto);
    Transaktion transaktionTaetigen(Kunde kunde, Transaktion transaktion) throws TRBankException;

    @Transactional
    List<Transaktion> getTransaktionenForKonten(List<Konto> konten);
}
