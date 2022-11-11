package kz.crypto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.crypto.dto.Calc1Dto;
import kz.crypto.dto.Calc2Dto;
import kz.crypto.model.CryptoConfig;
import kz.crypto.service.CryptoConfigService;
import kz.crypto.service.RecommendationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/crypto/config")
@AllArgsConstructor
@Tag(name = "Crypto Config", description = "For managing from not currently supported cryptos")
public class CryptoConfigController {

    private final CryptoConfigService cryptoConfigService;

    @GetMapping()
    @Operation(summary = "Get all crypto configs")
    public ResponseEntity<List<CryptoConfig>> getAll() {
        return ResponseEntity.ok(cryptoConfigService.getAll());
    }

    @PostMapping()
    @Operation(summary = "Create crypto config")
    public ResponseEntity<CryptoConfig> create(@RequestBody CryptoConfig cryptoConfig) {
        return ResponseEntity.ok(cryptoConfigService.create(cryptoConfig));
    }

    @DeleteMapping("/{cryptoName}")
    @Operation(summary = "Delete crypto config by crypto name")
    public void delete(@PathVariable(name = "cryptoName") String cryptoName) {
        cryptoConfigService.delete(cryptoName);
    }
}
