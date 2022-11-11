package kz.crypto.repository;

import kz.crypto.model.CryptoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoTypeRepository extends JpaRepository<CryptoType, Long> {

    CryptoType findByType(String type);
    CryptoType findByFilePath(String filePath);
    CryptoType findByFilePathAndLastUpdated(String filePath, Long lastUpdated);
}
