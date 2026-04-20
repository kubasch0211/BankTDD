package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.Client;
import polsl.wtto.banktdd.repository.ClientRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldAddClientSuccessfully() {
        // given
        String firstName = "Jan";
        String lastName = "Kowalski";
        String pesel = "12345678901";

        // Baza mówi: "Nie ma jeszcze takiego PESELu"
        when(clientRepository.existsByPesel(pesel)).thenReturn(false);

        // when
        clientService.addClient(firstName, lastName, pesel);

        // then
        // Przechwytujemy obiekt Client, który serwis wysłał do zapisu w bazie
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());

        Client savedClient = clientCaptor.getValue();
        assertThat(savedClient.getFirstName()).isEqualTo("Jan");
        assertThat(savedClient.getLastName()).isEqualTo("Kowalski");
        assertThat(savedClient.getPesel()).isEqualTo("12345678901");
    }

    @Test
    void shouldThrowExceptionWhenClientAlreadyExists() {
        // given
        String pesel = "12345678901";

        // Baza mówi: "Taki PESEL już jest w systemie"
        when(clientRepository.existsByPesel(pesel)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> clientService.addClient("Anna", "Nowak", pesel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Klient z podanym numerem PESEL już istnieje");

        // Upewniamy się, że serwis w ogóle nie próbował niczego zapisać
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPeselIsInvalid() {
        // given
        String invalidPesel = "123"; // za krótki

        // when & then
        assertThatThrownBy(() -> clientService.addClient("Anna", "Nowak", invalidPesel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PESEL musi mieć dokładnie 11 cyfr");
    }
}