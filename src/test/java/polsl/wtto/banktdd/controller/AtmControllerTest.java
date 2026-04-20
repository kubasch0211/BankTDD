package polsl.wtto.banktdd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import polsl.wtto.banktdd.service.AtmService;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AtmControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AtmService atmService;

    @InjectMocks
    private AtmController atmController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(atmController).build();
    }

    @Test
    void shouldDepositMoneySuccessfully() throws Exception {
        // given: przygotowujemy JSON-a symulującego działanie bankomatu
        String json = """
                {
                    "accountNumber": "1111111119",
                    "amount": 250.50
                }
                """;

        // when & then: uderzamy pod adres wpłaty
        mockMvc.perform(post("/api/atm/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // Sprawdzamy, czy kontroler poprawnie odpalił serwis
        verify(atmService).deposit("1111111119", new BigDecimal("250.50"));
    }
}