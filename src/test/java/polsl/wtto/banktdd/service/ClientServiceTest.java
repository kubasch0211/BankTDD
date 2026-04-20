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

    @Test
    void shouldDeleteClientSuccessfully() {
        // given
        String pesel = "12345678901";
        // Tworzymy "istniejącego" klienta, którego chcemy usunąć
        Client existingClient = new Client("Jan", "Kowalski", pesel);

        // Uczymy atrapę bazy danych: jak zapytam o ten PESEL, zwróć mi tego klienta
        when(clientRepository.findByPesel(pesel)).thenReturn(java.util.Optional.of(existingClient));

        // when: Wywołujemy akcję usuwania
        clientService.deleteClient(pesel);

        // then: Sprawdzamy, czy serwis faktycznie zlecił bazie usunięcie tego konkretnego obiektu
        verify(clientRepository).delete(existingClient);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentClient() {
        // given
        String pesel = "99999999999";

        // Baza danych mówi: "Nie mam nikogo z takim PESEL-em"
        when(clientRepository.findByPesel(pesel)).thenReturn(java.util.Optional.empty());

        // when & then: Oczekujemy błędu
        assertThatThrownBy(() -> clientService.deleteClient(pesel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Klient z podanym numerem PESEL nie istnieje");

        // Upewniamy się, że serwis w ogóle nie próbował wywołać usuwania (bo nie miał kogo)
        verify(clientRepository, never()).delete(any());
    }
    @Test
    void shouldUpdateClientLastNameSuccessfully() {
        // given
        String pesel = "12345678901";
        String newLastName = "Nowak-Kowalska"; // Nowe nazwisko

        Client existingClient = new Client("Anna", "Kowalska", pesel);

        // Baza mówi: "Tak, mam taką klientkę"
        when(clientRepository.findByPesel(pesel)).thenReturn(java.util.Optional.of(existingClient));

        // when
        clientService.updateClientLastName(pesel, newLastName);

        // then
        // 1. Sprawdzamy, czy w obiekcie zmieniło się tylko nazwisko
        assertThat(existingClient.getFirstName()).isEqualTo("Anna");
        assertThat(existingClient.getPesel()).isEqualTo(pesel);
        assertThat(existingClient.getLastName()).isEqualTo(newLastName);

        // 2. Upewniamy się, że serwis zapisał zaktualizowany obiekt w bazie
        verify(clientRepository).save(existingClient);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentClient() {
        // given
        String pesel = "99999999999";
        String newLastName = "Nowak";

        // Baza mówi: "Nie ma u nas takiego PESELu"
        when(clientRepository.findByPesel(pesel)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> clientService.updateClientLastName(pesel, newLastName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Klient z podanym numerem PESEL nie istnieje");

        // Upewniamy się, że w razie błędu nic nie poszło do zapisu
        verify(clientRepository, never()).save(any());
    }
}

