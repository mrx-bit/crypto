package kz.crypto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.crypto.dto.Calc2Dto;
import kz.crypto.dto.CryptoDto;
import kz.crypto.dto.Calc1Dto;
import kz.crypto.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/recommendation")
@Tag(name = "Recommendation service", description = "Crypto recommendation service")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping(value = "/calc1")
    @Operation(summary = "Calculates oldest/newest/min/max for each crypto for the whole month")
    public ResponseEntity<List<Calc1Dto>> getCalc1() {
        return ResponseEntity.ok(recommendationService.getCalc1());
    }


    @GetMapping(value = "/calc2")
    @Operation(summary = "Exposes an endpoint that will return a descending sorted list of all the cryptos, comparing the normalized range (i.e. (max-min)/min)")
    public ResponseEntity<List<Calc2Dto>> getCalc2() {
        return ResponseEntity.ok(recommendationService.getCalc2());
    }

    @GetMapping(value = "/calc3/{crypto}")
    @Operation(summary = "Exposes an endpoint that will return the oldest/newest/min/max values for a requested crypto")
    public ResponseEntity<Calc1Dto> getCalc3(@PathVariable("crypto") String crypto) {
        return ResponseEntity.ok(recommendationService.getCalc3(crypto));
    }

    @GetMapping(value = "/calc4")
    @Operation(summary = "Exposes an endpoint that will return the crypto with the highest normalized range for a specific day")
    public ResponseEntity<Calc2Dto> getCalc4(@RequestParam("day") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date) {
        return ResponseEntity.ok(recommendationService.getCalc4(date));
    }
}
