package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exceptions.KundeException;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.transaction.Transactional;
import java.util.List;

public interface KundeServiceIF extends UserDetailsService {
    Kunde kundeRegistrieren(Kunde kunde) throws KundeException;

    @Transactional
    Kunde kundeSpeichern(Kunde k);

    Kunde kundeAnmelden(Kunde kunde) throws KundeException;
    List<Kunde> getAllKunden();
}
