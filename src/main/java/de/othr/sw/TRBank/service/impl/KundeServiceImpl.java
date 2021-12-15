package de.othr.sw.TRBank.service.impl;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.repository.KundeRepository;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exception.TRBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class KundeServiceImpl implements KundeServiceIF {
    @Autowired
    private KundeRepository kundeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Kunde kundeRegistrieren(Kunde k) throws TRBankException {
        if(kundeRepository.getByUsername(k.getUsername()) != null) {
            throw new TRBankException("ERROR: Dieser User existiert bereits.");
        }
        k.setPasswort(passwordEncoder.encode(k.getPassword()));
        return kundeSpeichern(k);
    }

    @Transactional
    @Override
    public Kunde kundeSpeichern(Kunde k){
        return kundeRepository.save(k);
    }

    @Transactional
    @Override
    public Kunde kundeAnmelden(Kunde anmeldedaten) throws TRBankException{
        Kunde kunde = kundeRepository.getByUsername(anmeldedaten.getUsername());
        if(kunde == null || !passwordEncoder.matches(anmeldedaten.getPassword(), kunde.getPassword())) {
            throw new TRBankException("ERROR: Falscher Username oder Passwort");
        }
        return kunde;
    }

    @Transactional
    @Override
    public List<Kunde> getAllKunden() {
        List<Kunde> kunden = new ArrayList<>();
        kundeRepository.findAll().forEach(kunden::add);
        return kunden;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO get oder find hier? -> TESTEN
        return kundeRepository.getByUsername(username);
    }
}
