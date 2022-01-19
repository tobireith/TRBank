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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class KundeServiceImpl implements KundeServiceIF {
    @Autowired
    private KundeRepository kundeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public Kunde kundeRegistrieren(@Valid Kunde k) throws TRBankException {
        if(kundeRepository.findByUsername(k.getUsername()).isPresent()) {
            throw new TRBankException("Dieser Username existiert bereits.");
        }
        k.setPasswort(passwordEncoder.encode(k.getPassword()));
        return kundeSpeichern(k);
    }

    @Transactional (Transactional.TxType.REQUIRED)
    @Override
    public Kunde kundeSpeichern(@Valid Kunde k){
        return kundeRepository.save(k);
    }

    @Transactional (Transactional.TxType.SUPPORTS)
    @Override
    public Kunde anmeldedatenVerifizieren(String username, String passwort) throws TRBankException{
        Kunde kunde = kundeRepository.findByUsername(username).orElseThrow(() -> new TRBankException("Unbekannter Username"));
        if(!passwordEncoder.matches(passwort, kunde.getPassword())) {
            throw new TRBankException("Falscher Username oder Passwort");
        }
        return kunde;
    }

    @Transactional (Transactional.TxType.SUPPORTS)
    @Override
    public Kunde getKundeByUsername(String username) throws TRBankException{
        return kundeRepository.findByUsername(username).orElseThrow(() -> new TRBankException("Unbekannter Username: " + username));
    }

    @Transactional (Transactional.TxType.SUPPORTS)
    @Override
    public boolean userWithUsernameExists(String username){
        return kundeRepository.findByUsername(username).isPresent();
    }

    @Transactional (Transactional.TxType.SUPPORTS)
    @Override
    public List<Kunde> getAllKunden() {
        List<Kunde> kunden = new ArrayList<>();
        kundeRepository.findAll().forEach(kunden::add);
        return kunden;
    }

    @Transactional (Transactional.TxType.SUPPORTS)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return kundeRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Falscher Username"));
    }


}
