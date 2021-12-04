package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exceptions.KontoException;

import java.util.Collection;

public interface KontoServiceIF {
    Collection<Konto> getKontenByKunde(Kunde kunde);
    Kontoauszug kontoauszugErstellen(Konto konto) throws KontoException;
    Konto getKontoByIban(String Iban);

}
