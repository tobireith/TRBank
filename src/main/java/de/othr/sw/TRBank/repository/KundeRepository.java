package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Kunde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface KundeRepository extends CrudRepository<Kunde, String> {
    List<Kunde> getByVornameAndNachname(String vorname, String nachname);
    Kunde findByUsername(String username);
    Kunde findByUsernameAndPasswort(String username, String passwort);
}
