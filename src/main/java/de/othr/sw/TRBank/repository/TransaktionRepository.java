package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Transaktion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransaktionRepository extends PagingAndSortingRepository<Transaktion, Long> {
    Optional<List<Transaktion>> findAllByQuellkontoAndDatumBeforeOrderByDatum(Konto quellkonto, Date datum);
    Optional<List<Transaktion>> findAllByZielkontoAndDatumBeforeOrderByDatum(Konto zielkonto, Date datum);
    Optional<List<Transaktion>> findAllByQuellkontoOrZielkontoOrderByDatum(Konto quellkonto, Konto zielkonto, Pageable pageable);
    Optional<List<Transaktion>> findAllByQuellkontoOrZielkontoAndDatumAfterOrderByDatum(Konto quellkonto, Konto zielkonto, Date datum);
}
