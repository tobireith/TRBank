package de.othr.sw.TRBank.service;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface KundeServiceIF extends UserDetailsService {
    Kunde kundeRegistrieren(Kunde kunde) throws TRBankException;

    Kunde anmeldedatenVerifizieren(String username, String passwort) throws TRBankException;

    Kunde getKundeByUsername(String username) throws TRBankException;

    boolean userWithUsernameExists(String username);

    List<Kunde> getAllKunden();
}
