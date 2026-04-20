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
        String json = """
                {
                    "firstName": "Jan",
                    "lastName": "Kowalski",
                    "pesel": "12345678901"
                }
                """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(clientService).addClient("Jan", "Kowalski", "12345678901");
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
}