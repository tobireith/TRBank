package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kunde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KontoRepository extends CrudRepository<Konto, String> {
    List<Konto> getKontosByBesitzerOrderByKontoId(Kunde k);
    Konto getKontoByIban(String iban);
}
