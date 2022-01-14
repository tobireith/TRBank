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

    Transaktion transaktionTaetigen(Transaktion transaktion, Kunde kunde) throws TRBankException;

    Kontoauszug kontoauszugErstellen(Konto konto) throws TRBankException;

    Konto getKontoByIban(String Iban) throws TRBankException;

    boolean kontoWithIbanExists(String Iban);

    Konto getKontoById (long kontoId)  throws TRBankException;

    Konto getKontoFromKundeById(Kunde kunde, long kontoId) throws TRBankException;

    Konto kontoAnlegen(Kunde kunde);
    Konto kontoAnlegen(Konto konto);

    Konto kontoUpdaten(Konto konto);

    void kontoLoeschen(long kontoId) throws TRBankException;

    List<Transaktion> getTransaktionenForKonten(List<Konto> konten);

    List<Transaktion> getAllTransaktionen();

    String generateRandomIban(String prefix);
}
