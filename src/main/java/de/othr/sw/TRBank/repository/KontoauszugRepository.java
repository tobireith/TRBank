package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KontoauszugRepository extends JpaRepository<Kontoauszug, Long> {
    Optional<List<Kontoauszug>> findAllByKontoOrderByDatumBis(Konto konto);
}
