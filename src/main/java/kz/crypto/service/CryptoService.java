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
public class CryptoService {
    private final CryptoConfigService cryptoConfigService;
    public Map<String, List<CryptoDto>> getCryptoValues() {
        List<CryptoConfig> cryptoConfigs = cryptoConfigService.getAll();
        List<Path> filePathList;
        String path = "src/main/resources/storage";
        String absolutePath = new File(path).getAbsolutePath();
        try (Stream<Path> paths = Files.walk(Paths.get(absolutePath))) {
            filePathList = paths
                    .filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, List<CryptoDto>> records = new HashMap<>();
        filePathList.forEach(res -> {
            if ( getStatusCrypto(cryptoConfigs, res.toString())) {
                try (BufferedReader br = new BufferedReader(new FileReader(res.toString()))) {
                    String line;
                    String symbol = null;
                    CryptoDto crypto;
                    List<CryptoDto> cryptoList = new ArrayList<>();
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        if (values[0] == null || values[0].equals("timestamp")) {
                            continue;
                        }
                        symbol = values[1];
                        crypto = new CryptoDto();
                        crypto.setDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(values[0])),
                                TimeZone.getDefault().toZoneId()));
                        crypto.setSymbol(values[1]);
                        crypto.setPrice(Double.valueOf(values[2]));
                        cryptoList.add(crypto);
                    }
                    records.put(symbol, cryptoList);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return records;
    }

    private Boolean getStatusCrypto(List<CryptoConfig> cryptoConfigs, String cryptoFolder) {
        return !cryptoConfigs.stream()
                .anyMatch(entry -> cryptoFolder.contains(entry.getSymbol()));
    }
}
