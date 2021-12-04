package de.othr.sw.TRBank.service.impl;

import de.othr.sw.TRBank.entity.Kunde;
import de.othr.sw.TRBank.repository.KundeRepository;
import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.service.exceptions.KundeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class KundeServiceImpl implements KundeServiceIF {
    @Autowired
    private KundeRepository kundeRepository;

    @Transactional
    @Override
    public Kunde kundeRegistrieren(Kunde k) throws KundeException {
        if(kundeRepository.findByUsername(k.getUsername()) != null) {
            throw new KundeException(1, "ERROR: Dieser User existiert bereits.");
        }
        //TODO: k.setPassword noch mit Security Utils versehen
        return kundeRepository.save(k);
    }

    @Transactional
    @Override
    public Kunde kundeAnmelden(Kunde kunde) throws KundeException{
        kunde = kundeRepository.findByUsernameAndPasswort(kunde.getUsername(), kunde.getPasswort());
        if(kunde == null) {
            throw new KundeException(2, "ERROR: Falscher Username oder Passwort");
        }
        return kunde;
    }

    @Transactional
    @Override
    public Collection<Kunde> getAllKunden() {
        Collection<Kunde> kunden = new ArrayList<>();
        kundeRepository.findAll().forEach(kunden::add);
        return kunden;
    }
}
