package kz.crypto.repository;

import kz.crypto.model.CryptoConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoConfigRepository extends JpaRepository<CryptoConfig, Long> {

    CryptoConfig findBySymbol(String cryptoName);

}
