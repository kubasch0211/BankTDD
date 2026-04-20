package polsl.wtto.banktdd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.OperationLog;
import polsl.wtto.banktdd.domain.OperationType;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.repository.OperationRepository;
import polsl.wtto.banktdd.service.validation.AccountNumberValidator;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AtmService {

    private final AccountRepository accountRepository; // TEGO BRAKOWAŁO!
    private final OperationRepository operationRepository;
    private final AccountNumberValidator accountNumberValidator;

    public void deposit(String accountNumber, BigDecimal amount) {
        if (accountNumber == null || !accountNumberValidator.isValid(accountNumber)) {
            throw new IllegalArgumentException("Nieprawidłowy numer konta");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Konto nie istnieje"));

        account.deposit(amount);

        accountRepository.save(account);

        OperationLog log = new OperationLog(accountNumber, OperationType.DEPOSIT, amount);
        operationRepository.save(log);
    }
}