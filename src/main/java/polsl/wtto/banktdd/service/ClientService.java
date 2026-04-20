package polsl.wtto.banktdd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import polsl.wtto.banktdd.domain.Client;
import polsl.wtto.banktdd.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public void addClient(String firstName, String lastName, String pesel) {


        if (pesel == null || !pesel.matches("\\d{11}")) {
            throw new IllegalArgumentException("PESEL musi mieć dokładnie 11 cyfr");
        }


        if (clientRepository.existsByPesel(pesel)) {
            throw new IllegalArgumentException("Klient z podanym numerem PESEL już istnieje");
        }


        clientRepository.save(new Client(firstName, lastName, pesel));
    }
    public void deleteClient(String pesel) {
        Client client = clientRepository.findByPesel(pesel)
                .orElseThrow(() -> new IllegalArgumentException("Klient z podanym numerem PESEL nie istnieje"));

        clientRepository.delete(client);
    }

}