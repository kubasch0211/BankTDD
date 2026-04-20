package polsl.wtto.banktdd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.service.validation.AccountNumberValidator;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final AccountNumberValidator validator;

    @Transactional
    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        if (!validator.isValid(toNumber)) {
            throw new IllegalArgumentException("Nieprawidłowy numer konta docelowego");
        }

        Account source = accountRepository.findByAccountNumber(fromNumber)
                .orElseThrow(() -> new IllegalArgumentException("Konto nadawcy nie istnieje"));

        Account target = accountRepository.findByAccountNumber(toNumber)
                .orElseThrow(() -> new IllegalArgumentException("Konto odbiorcy nie istnieje"));

        source.withdraw(amount);
        target.deposit(amount);

        accountRepository.save(source);
        accountRepository.save(target);
    }
}