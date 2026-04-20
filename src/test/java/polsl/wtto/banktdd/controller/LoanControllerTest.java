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
import polsl.wtto.banktdd.service.LoanService;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    void shouldTakeLoanSuccessfully() throws Exception {
        // given: JSON z numerem konta i kwotą kredytu
        String json = """
                {
                    "accountNumber": "1111111119",
                    "amount": 1000.00
                }
                """;

        // when & then: Uderzamy pod adres kredytów i oczekujemy statusu 201 Created (bo tworzymy nowy dług)
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Sprawdzamy, czy kontroler wywołał serwis z poprawnymi danymi
        verify(loanService).takeLoan("1111111119", new BigDecimal("1000.00"));
    }
}