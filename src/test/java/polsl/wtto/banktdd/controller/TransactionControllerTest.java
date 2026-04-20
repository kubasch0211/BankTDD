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
import polsl.wtto.banktdd.service.TransactionService;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void shouldExecuteTransferSuccessfully() throws Exception {
        // given: Przygotowujemy dane do przelewu (kto, do kogo, ile)
        String json = """
                {
                    "fromAccountNumber": "1111111119",
                    "toAccountNumber": "2222222229",
                    "amount": 150.00
                }
                """;

        // when & then: Uderzamy pod adres przelewu
        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // Sprawdzamy, czy kontroler przekazał dobre dane do serwisu
        verify(transactionService).transfer("1111111119", "2222222229", new BigDecimal("150.00"));
    }
}