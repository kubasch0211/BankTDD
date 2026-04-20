package polsl.wtto.banktdd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import polsl.wtto.banktdd.dto.DepositRequest;
import polsl.wtto.banktdd.service.AtmService;

@RestController
@RequestMapping("/api/atm")
@RequiredArgsConstructor
public class AtmController {

    private final AtmService atmService;

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@RequestBody DepositRequest request) {
        atmService.deposit(request.getAccountNumber(), request.getAmount());
    }
}