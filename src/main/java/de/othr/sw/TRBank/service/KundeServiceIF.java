package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exceptions.KundeException;

import java.util.List;

public interface KundeServiceIF {
    public Kunde kundeRegistrieren(Kunde kunde) throws KundeException;
    public Kunde kundeAnmelden(Kunde kunde) throws KundeException;
    public List<Kunde> getAllKunden();
}
