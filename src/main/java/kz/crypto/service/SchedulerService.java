package kz.crypto.service;

import kz.crypto.model.Crypto;
import kz.crypto.model.CryptoType;
import kz.crypto.repository.CryptoRepository;
import kz.crypto.repository.CryptoTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class SchedulerService {

    @Autowired
    private CryptoRepository cryptoRepository;
    @Autowired
    private CryptoTypeRepository cryptoTypeRepository;

    @Scheduled(fixedRate = 60000)
    public void updateCrypto() {
        List<Path> filePathList;
        String path = "src/main/resources/storage";
        String absolutePath = new File(path).getAbsolutePath();
        try (Stream<Path> paths = Files.walk(Paths.get(absolutePath))) {
            filePathList = paths
                    .filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        filePathList.forEach(res -> {
            File file = new File(res.toString());
            try (BufferedReader br = new BufferedReader(new FileReader(res.toString()))) {
                String line;
                String symbol = null;

                CryptoType cryptoType = cryptoTypeRepository.findByFilePathAndLastUpdated(file.getName(), file.lastModified());
                if(cryptoType == null) {
                    CryptoType cryptoTypeNew = new CryptoType();
                    cryptoTypeNew.setFilePath(file.getName());
                    cryptoTypeNew.setActive(true);
                    cryptoTypeNew.setLastUpdated(file.lastModified());

                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        symbol = values[1];

                        Crypto crypto = cryptoRepository.find(Long.parseLong(values[0]), values[1], Double.valueOf(values[2]));
                        if(crypto == null) {
                            Crypto cryptoNew = new Crypto();
                            cryptoNew.setDate(Long.parseLong(values[0]));
                            cryptoNew.setSymbol(values[1]);
                            cryptoNew.setPrice(Double.valueOf(values[2]));
                            cryptoRepository.saveAndFlush(cryptoNew);
                        }
                    }

                    cryptoTypeNew.setType(symbol);
                    cryptoTypeRepository.saveAndFlush(cryptoTypeNew);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
