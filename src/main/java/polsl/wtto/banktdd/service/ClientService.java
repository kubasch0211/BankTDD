package polsl.wtto.banktdd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.Client;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.repository.ClientRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void addClient(String firstName, String lastName, String pesel, String accountNumber) {

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Imię nie może być puste");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwisko nie może być puste");
        }

        if (pesel == null || !pesel.matches("\\d{11}")) {
            throw new IllegalArgumentException("PESEL musi mieć dokładnie 11 cyfr");
        }
        if (clientRepository.existsByPesel(pesel)) {
            throw new IllegalArgumentException("Klient z podanym numerem PESEL już istnieje");
        }

        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new IllegalArgumentException("Konto z podanym numerem już istnieje");
        }

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        Client client = new Client(firstName, lastName, pesel, accountNumber);
        client.setAccountNumber(accountNumber);
        clientRepository.save(client);
    }

    public void deleteClient(String pesel) {
        Client client = clientRepository.findByPesel(pesel)
                .orElseThrow(() -> new IllegalArgumentException("Klient z podanym numerem PESEL nie istnieje"));

        clientRepository.delete(client);

    }

    public void updateClientLastName(String pesel, String newLastName) {
        Client client = clientRepository.findByPesel(pesel)
                .orElseThrow(() -> new IllegalArgumentException("Klient z podanym numerem PESEL nie istnieje"));
        client.changeLastName(newLastName);
        clientRepository.save(client);
    }

}