package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Kunde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KundeRepository extends CrudRepository<Kunde, String> {
    Optional<Kunde> findByUsername(String username);
}
