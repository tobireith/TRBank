package de.othr.sw.TRBank.repository;

import de.othr.sw.TRBank.entity.Transaktion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaktionRepository extends PagingAndSortingRepository<Transaktion, Long> {
}
