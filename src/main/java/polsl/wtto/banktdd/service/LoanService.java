package polsl.wtto.banktdd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.Loan;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.repository.LoanRepository;
import polsl.wtto.banktdd.service.validation.AccountNumberValidator;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final AccountNumberValidator accountNumberValidator;

    @Transactional
    public void takeLoan(String accountNumber, BigDecimal loanAmount) {
        if (accountNumber == null || !accountNumberValidator.isValid(accountNumber)) {
            throw new IllegalArgumentException("Nieprawidłowy numer konta");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Konto o podanym numerze nie istnieje"));

        account.deposit(loanAmount);
        accountRepository.save(account);

        BigDecimal totalDebt = loanAmount.multiply(new BigDecimal("1.10"));

        Loan loan = new Loan(accountNumber, loanAmount, totalDebt);
        loanRepository.save(loan);
    }
}