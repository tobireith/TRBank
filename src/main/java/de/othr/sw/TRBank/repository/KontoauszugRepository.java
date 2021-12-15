package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;
import de.othr.sw.TRBank.entity.Transaktion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KontoauszugRepository extends JpaRepository<Kontoauszug, Long> {
    Optional<List<Kontoauszug>> findAllByKontoOrderByDatumBis(Konto konto);

    @Query("SELECT a.transaktionen " +
            "FROM Kontoauszug AS a " +
            "JOIN Transaktion AS t " +
            "JOIN Konto AS konto " +
            "WHERE a.kontoauszugId = ?1 " +
            "ORDER BY t.datum")
    Optional<List<Transaktion>> transaktionenVonKontoauszugSortiertNachDatum(Long konzoauszugId);
}
