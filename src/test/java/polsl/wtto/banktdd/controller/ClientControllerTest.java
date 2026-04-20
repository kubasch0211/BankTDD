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
import polsl.wtto.banktdd.service.ClientService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void shouldReturnCreatedStatusWhenClientIsAdded() throws Exception {
        // given: Wymagamy teraz podania "accountNumber" w JSONie!
        String json = """
                {
                    "firstName": "Jan",
                    "lastName": "Kowalski",
                    "pesel": "12345678901",
                    "accountNumber": "1111111119"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Sprawdzamy, czy kontroler przekazał WSZYSTKIE 4 parametry do serwisu
        verify(clientService).addClient("Jan", "Kowalski", "12345678901", "1111111119");
    }
    @Test
    void shouldUpdateClientLastNameSuccessfully() throws Exception {
        // given
        String pesel = "12345678901";
        String newLastName = "Nowak-Kowalska";

        // when & then: Uderzamy pod adres /api/clients/{pesel}/lastName
        mockMvc.perform(patch("/api/clients/" + pesel + "/lastName")
                        .param("newLastName", newLastName))
                .andExpect(status().isOk());

        // Sprawdzamy, czy kontroler przekazał to do naszego serwisu
        verify(clientService).updateClientLastName(pesel, newLastName);
    }
    @Test
    void shouldDeleteClientSuccessfully() throws Exception {
        // given
        String pesel = "12345678901";

        // when & then: Uderzamy metodą DELETE pod adres /api/clients/{pesel}
        // Spodziewamy się statusu 204 (No Content), co jest dobrą praktyką przy usuwaniu
        mockMvc.perform(delete("/api/clients/" + pesel))
                .andExpect(status().isNoContent());

        // Sprawdzamy, czy kontroler przekazał polecenie usunięcia do serwisu
        verify(clientService).deleteClient(pesel);
    }
}