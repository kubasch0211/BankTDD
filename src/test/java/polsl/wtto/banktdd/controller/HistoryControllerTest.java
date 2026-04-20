package polsl.wtto.banktdd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import polsl.wtto.banktdd.service.HistoryService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private HistoryController historyController;

    @BeforeEach
    void setup() {
        // Ręczny setup, żeby IntelliJ nie marudził
        this.mockMvc = MockMvcBuilders.standaloneSetup(historyController).build();
    }

    @Test
    void shouldReturnAccountHistory() throws Exception {
        // given
        String accountNumber = "1111111119";

        // when & then: Uderzamy metodą GET pod adres z numerem konta na końcu
        mockMvc.perform(get("/api/history/" + accountNumber))
                .andExpect(status().isOk());

        // Sprawdzamy, czy kontroler poprosił serwis o wygenerowanie raportu dla tego konta
        verify(historyService).getAccountHistory(accountNumber);
    }
}