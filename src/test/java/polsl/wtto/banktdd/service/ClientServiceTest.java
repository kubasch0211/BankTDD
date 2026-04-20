package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.Client;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.repository.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldAddClientSuccessfully() {
        // given
        String firstName = "Jan";
        String lastName = "Kowalski";
        String pesel = "12345678901";
        String accountNumber = "1111111119"; // Wymagany nowy parametr

        // Baza mówi: "Nie ma jeszcze takiego PESELu"
        when(clientRepository.existsByPesel(pesel)).thenReturn(false);

        // Baza mówi: "Nie ma jeszcze takiego numeru konta"
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        // when (przekazujemy 4 argumenty)
        clientService.addClient(firstName, lastName, pesel, accountNumber);

        // then
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());

        Client savedClient = clientCaptor.getValue();
        assertThat(savedClient.getFirstName()).isEqualTo("Jan");
        assertThat(savedClient.getLastName()).isEqualTo("Kowalski");
        assertThat(savedClient.getPesel()).isEqualTo("12345678901");
        // Sprawdzamy, czy numer konta też poprawnie ustawił się w obiekcie
        assertThat(savedClient.getAccountNumber()).isEqualTo("1111111119");
    }

    @Test
    void shouldThrowExceptionWhenClientAlreadyExists() {
        // given
        String pesel = "12345678901";

        when(clientRepository.existsByPesel(pesel)).thenReturn(true);

        // when & then (dodany 4. argument z fikcyjnym numerem konta do testu)
        assertThatThrownBy(() -> clientService.addClient("Anna", "Nowak", pesel, "2222222229"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Klient z podanym numerem PESEL już istnieje");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPeselIsInvalid() {
        // given
        String invalidPesel = "123";

        // when & then
        assertThatThrownBy(() -> clientService.addClient("Anna", "Nowak", invalidPesel, "2222222229"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PESEL musi mieć dokładnie 11 cyfr");
    }

    @Test
    void shouldDeleteClientSuccessfully() {
        // given
        String pesel = "12345678901";
        // Używamy nowego konstruktora (4 parametry)
        Client existingClient = new Client("Jan", "Kowalski", pesel, "1111111119");

        when(clientRepository.findByPesel(pesel)).thenReturn(Optional.of(existingClient));

        // when
        clientService.deleteClient(pesel);

        // then
        verify(clientRepository).delete(existingClient);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentClient() {
        // given
        String pesel = "99999999999";

        when(clientRepository.findByPesel(pesel)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> clientService.deleteClient(pesel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Klient z podanym numerem PESEL nie istnieje");

        verify(clientRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateClientLastNameSuccessfully() {
        // given
        String pesel = "12345678901";
        String newLastName = "Nowak-Kowalska";

        // Używamy nowego konstruktora (4 parametry)
        Client existingClient = new Client("Anna", "Kowalska", pesel, "1111111119");

        when(clientRepository.findByPesel(pesel)).thenReturn(Optional.of(existingClient));

        // when
        clientService.updateClientLastName(pesel, newLastName);

        // then
        assertThat(existingClient.getFirstName()).isEqualTo("Anna");
        assertThat(existingClient.getPesel()).isEqualTo(pesel);
        assertThat(existingClient.getLastName()).isEqualTo(newLastName);

        verify(clientRepository).save(existingClient);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentClient() {
        // given
        String pesel = "99999999999";
        String newLastName = "Nowak";

        when(clientRepository.findByPesel(pesel)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> clientService.updateClientLastName(pesel, newLastName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Klient z podanym numerem PESEL nie istnieje");

        verify(clientRepository, never()).save(any());
    }
}