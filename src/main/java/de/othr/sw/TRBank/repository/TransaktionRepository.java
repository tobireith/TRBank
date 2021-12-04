package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Transaktion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransaktionRepository extends PagingAndSortingRepository<Transaktion, Long> {
    List<Transaktion> getAllByQuellkontoAndDatumBeforeOrderByDatum(Konto quellkonto, Date datum);
    List<Transaktion> getAllByZielkontoAndDatumBeforeOrderByDatum(Konto zielkonto, Date datum);
    List<Transaktion> getAllByQuellkontoOrZielkontoOrderByDatum(Konto quellkonto, Konto zielkonto);
    List<Transaktion> findAllByQuellkontoOrZielkontoAndDatumAfterOrderByDatum(Konto quellkonto, Konto zielkonto, Date datum);
}
