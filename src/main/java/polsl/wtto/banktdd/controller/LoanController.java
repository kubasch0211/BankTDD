package polsl.wtto.banktdd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import polsl.wtto.banktdd.dto.LoanRequest;
import polsl.wtto.banktdd.service.LoanService;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void takeLoan(@RequestBody LoanRequest request) {
        loanService.takeLoan(request.getAccountNumber(), request.getAmount());
    }
}