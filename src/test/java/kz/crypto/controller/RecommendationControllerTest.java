package kz.crypto.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import kz.crypto.CryptoApplication;
import kz.crypto.common.AbstractMockMvcController;
import kz.crypto.dto.Calc1Dto;
import kz.crypto.dto.Calc2Dto;
import kz.crypto.service.RecommendationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.extension.ExtendWith;
import static utils.functions.Functions.with;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {CryptoApplication.class}
)
@AutoConfigureMockMvc(addFilters = false)
class RecommendationControllerTest extends AbstractMockMvcController {
    @MockBean
    private RecommendationService recommendationService;

    public RecommendationControllerTest() {
        super("/recommendation");
    }

    @Test
    void calc1() {
        List<Calc1Dto> calc1Dtos = new ArrayList<>();
        lenient().when(recommendationService.getCalc1()).thenReturn(calc1Dtos);

        with(this.getResponse(this.getRequestNoBearer(this.buildUrl("/calc1")),
                new TypeReference<List<Calc1Dto>>() {}), Assertions::assertNotNull);
    }

    @Test
    void calc2() {
        List<Calc2Dto> calc2Dtos = new ArrayList<>();
        lenient().when(recommendationService.getCalc2()).thenReturn(calc2Dtos);

        with(this.getResponse(this.getRequestNoBearer(this.buildUrl("/calc2")),
                new TypeReference<List<Calc2Dto>>() {}), Assertions::assertNotNull);
    }

    @Test
    void calc3() {
        Calc1Dto calc1Dto = new Calc1Dto();
        lenient().when(recommendationService.getCalc3(anyString())).thenReturn(calc1Dto);

        with(this.getResponse(this.getRequestNoBearer(this.buildUrl("/calc3/BTC")), Calc1Dto.class), Assertions::assertNotNull);
    }

    @Test
    void calc4() {
        Calc2Dto calc2Dto = new Calc2Dto();
        lenient().when(recommendationService.getCalc4(any(LocalDate.class))).thenReturn(calc2Dto);

        with(this.getResponse(this.getRequestNoBearer(this.buildUrl("/calc4?day=01.01.2022")),
                Calc2Dto.class), Assertions::assertNotNull);
    }
}
