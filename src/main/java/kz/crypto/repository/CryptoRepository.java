package kz.crypto.repository;

import kz.crypto.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {

    @Query("select cr from Crypto cr where cr.date = :date and cr.symbol = :symbol and cr.price = :price")
    Crypto find(Long date, String symbol, Double price);
}
