package kz.crypto.dto;

import lombok.Data;
import java.util.List;

@Data
public class Calc2Dto {
    private String crypto;
    private Double normalizedValue;
    private List<CryptoDto> cryptoDtos;
}
