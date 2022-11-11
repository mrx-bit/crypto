package kz.crypto.service;

import kz.crypto.dto.CryptoDto;
import kz.crypto.model.CryptoConfig;
import kz.crypto.repository.CryptoConfigRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class CryptoConfigService {
    private final CryptoConfigRepository cryptoConfigRepository;

    public CryptoConfig create(CryptoConfig cryptoConfig) {
        CryptoConfig cryptoConfigOld = cryptoConfigRepository.findBySymbol(cryptoConfig.getSymbol());
        return cryptoConfigOld == null ? cryptoConfigRepository.save(cryptoConfig) : cryptoConfigOld;
    }

    public List<CryptoConfig> getAll() {
        return cryptoConfigRepository.findAll();
    }

    public void delete(String symbol) {
        CryptoConfig cryptoConfig = cryptoConfigRepository.findBySymbol(symbol);
        if (cryptoConfig != null)
            cryptoConfigRepository.delete(cryptoConfig);
    }

}
