package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.entity.Transaktion;
import de.othr.sw.TRBank.service.exceptions.KundeException;
import de.othr.sw.TRBank.service.exceptions.TransaktionException;

import java.util.Date;
import java.util.List;

public interface TransaktionServiceIF {
    Transaktion transaktionTätigen(Kunde kunde, Transaktion transaktion) throws TransaktionException, KundeException;
    List<Transaktion> getAllTransaktionenForKonto(Konto konto);
    List<Transaktion> getAusgehendeTransaktionenAbDatum(Konto konto, Date datum);
    List<Transaktion> getEinkommendeTransaktionenAbDatum(Konto konto, Date datum);
}
