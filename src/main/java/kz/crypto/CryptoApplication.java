package kz.crypto;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@Slf4j
//@EnableScheduling
@SpringBootApplication
public class CryptoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoApplication.class, args);
    }

}
