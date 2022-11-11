package kz.crypto.service;

import kz.crypto.dto.Calc2Dto;
import kz.crypto.dto.CryptoDto;
import kz.crypto.dto.Calc1Dto;
import kz.crypto.util.CalculateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecommendationService {
    @Autowired
    private CryptoService cryptoService;

    public List<Calc1Dto> getCalc() {
        Map<String, List<CryptoDto>> cryptoValues = cryptoService.getCryptoValues();

        List<Calc1Dto> calc1Dtos = new ArrayList<>();
        for (Map.Entry<String, List<CryptoDto>> entry : cryptoValues.entrySet()) {
            Map<CalculateEnum, CryptoDto> map = new HashMap<>();
            // oldest/newest/min/max
            map.put(CalculateEnum.OLDEST,
                    Collections.min(entry.getValue(), Comparator.comparing(CryptoDto::getDate))
            );
            map.put(CalculateEnum.NEWEST,
                    Collections.max(entry.getValue(), Comparator.comparing(CryptoDto::getDate))
            );

            map.put(CalculateEnum.MIN,
                    Collections.min(entry.getValue(), Comparator.comparing(CryptoDto::getPrice))
            );
            map.put(CalculateEnum.MAX,
                    Collections.max(entry.getValue(), Comparator.comparing(CryptoDto::getPrice))
            );
            Calc1Dto recommendationDto = new Calc1Dto();
            recommendationDto.setCrypto(entry.getKey());
            recommendationDto.setCalc(map);

            calc1Dtos.add(recommendationDto);
        }

        return calc1Dtos;
    }

    public List<Calc1Dto> getCalc1() {
        Map<String, List<CryptoDto>> cryptoValues = cryptoService.getCryptoValues();

        List<Calc1Dto> calc1Dtos = new ArrayList<>();
        for (Map.Entry<String, List<CryptoDto>> entry : cryptoValues.entrySet()) {
            Map<YearMonth, List<CryptoDto>> cryptoByMonth = entry.getValue().stream()
                .collect(Collectors
                    .groupingBy(m-> YearMonth.from(m.getDate()), LinkedHashMap::new, Collectors.toList()));

            for (Map.Entry<YearMonth, List<CryptoDto>> entry2 : cryptoByMonth.entrySet()) {
                Map<CalculateEnum, CryptoDto> map = new HashMap<>();
                // oldest/newest/min/max
                map.put(CalculateEnum.OLDEST,
                        Collections.min(entry2.getValue(), Comparator.comparing(CryptoDto::getDate))
                );
                map.put(CalculateEnum.NEWEST,
                        Collections.max(entry2.getValue(), Comparator.comparing(CryptoDto::getDate))
                );

                map.put(CalculateEnum.MIN,
                        Collections.min(entry2.getValue(), Comparator.comparing(CryptoDto::getPrice))
                );
                map.put(CalculateEnum.MAX,
                        Collections.max(entry2.getValue(), Comparator.comparing(CryptoDto::getPrice))
                );
                Calc1Dto calc1Dto = new Calc1Dto();
                calc1Dto.setCrypto(entry.getKey());
                calc1Dto.setMonth(entry2.getKey());
                calc1Dto.setCalc(map);
                calc1Dtos.add(calc1Dto);
            }
        }
        return calc1Dtos;
    }

    public List<Calc2Dto> getCalc2() {
        Map<String, List<CryptoDto>> cryptoValues = cryptoService.getCryptoValues();
        List<Calc2Dto> calc2DtoList = new ArrayList<>();
        for (Map.Entry<String, List<CryptoDto>> entry : cryptoValues.entrySet()) {
            var min =  Collections.min(entry.getValue(), Comparator.comparing(CryptoDto::getPrice)).getPrice();
            var max =  Collections.max(entry.getValue(), Comparator.comparing(CryptoDto::getPrice)).getPrice();
            Calc2Dto calc2Dto = new Calc2Dto();
            calc2Dto.setCrypto(entry.getKey());
            calc2Dto.setCryptoDtos(entry.getValue());
            calc2Dto.setNormalizedValue((max - min) / min);
            calc2DtoList.add(calc2Dto);
        }
        calc2DtoList.sort(Comparator.comparing(Calc2Dto::getNormalizedValue).reversed());
        return calc2DtoList;
    }

    public Calc1Dto getCalc3(String key) {
        List<Calc1Dto> values = getCalc();
        return values.stream().filter(x -> x.getCrypto().equals(key)).findFirst().orElseThrow(RuntimeException::new);
    }

    public Calc2Dto getCalc4(LocalDate date) {
        Map<String, List<CryptoDto>> cryptoValues = cryptoService.getCryptoValues();
        List<Calc2Dto> calc2DtoList = new ArrayList<>();
        for (Map.Entry<String, List<CryptoDto>> entry : cryptoValues.entrySet()) {
            var list = entry.getValue().stream().filter(x -> x.getDate().toLocalDate().equals(date)).collect(Collectors.toList());

            var min =  Collections.min(list, Comparator.comparing(CryptoDto::getPrice)).getPrice();
            var max =  Collections.max(list, Comparator.comparing(CryptoDto::getPrice)).getPrice();
            Calc2Dto calc2Dto = new Calc2Dto();
            calc2Dto.setCrypto(entry.getKey());
            calc2Dto.setCryptoDtos(list);
            calc2Dto.setNormalizedValue((max - min) / min);
            calc2DtoList.add(calc2Dto);
        }
        calc2DtoList.sort(Comparator.comparing(Calc2Dto::getNormalizedValue).reversed());
        return calc2DtoList.stream().findFirst().orElseThrow(RuntimeException::new);
    }
}
