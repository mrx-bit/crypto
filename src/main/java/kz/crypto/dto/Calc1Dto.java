package kz.crypto.dto;

import kz.crypto.model.Crypto;
import kz.crypto.util.CalculateEnum;
import lombok.Data;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Data
public class Calc1Dto {
    private String crypto;
    private YearMonth month;
    private Map<CalculateEnum, CryptoDto> calc;
}
