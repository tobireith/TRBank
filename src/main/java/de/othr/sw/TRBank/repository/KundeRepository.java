package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Kunde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface KundeRepository extends CrudRepository<Kunde, String> {
    List<Kunde> getAllByVornameAndNachname(String vorname, String nachname);
    Kunde getByUsername(String username);
    Kunde getByUsernameAndPasswort(String username, String passwort);
}
