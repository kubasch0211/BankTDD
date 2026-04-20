package polsl.wtto.banktdd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import polsl.wtto.banktdd.dto.ClientRequest;
import polsl.wtto.banktdd.service.ClientService;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addClient(@RequestBody ClientRequest request) {
        clientService.addClient(
                request.getFirstName(),
                request.getLastName(),
                request.getPesel(),
                request.getAccountNumber()
        );
    }
    @PatchMapping("/{pesel}/lastName")
    @ResponseStatus(HttpStatus.OK)
    public void updateLastName(
            @PathVariable String pesel,
            @RequestParam String newLastName) {

        clientService.updateClientLastName(pesel, newLastName);
    }

    // Endpoint do usuwania klienta: DELETE http://localhost:8080/api/clients/12345678901
    @DeleteMapping("/{pesel}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content to standard REST przy udanym usunięciu
    public void deleteClient(@PathVariable String pesel) {
        clientService.deleteClient(pesel);
    }
}