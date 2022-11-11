package kz.crypto.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CryptoDto {
    private LocalDateTime date;
    private String symbol;
    private Double price;
}
