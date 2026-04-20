package polsl.wtto.banktdd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.Loan;
import polsl.wtto.banktdd.domain.OperationType;
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
    private final HistoryService historyService;

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

        // Logowanie otrzymania kredytu
        historyService.logOperation(accountNumber, loanAmount, OperationType.LOAN);
    }

    @Transactional
    public void repayLoan(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Konto nie istnieje"));

        Loan loan = loanRepository.findByAccountName(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("To konto nie ma aktywnego kredytu"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Niewystarczające środki na koncie do spłaty raty");
        }

        account.setBalance(account.getBalance().subtract(amount));

        BigDecimal newLoanAmount = loan.getRemainingDebt().subtract(amount);

        if (newLoanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            loanRepository.delete(loan);
        } else {
            loan.setRemainingDebt(newLoanAmount);
            loanRepository.save(loan);
        }

        accountRepository.save(account);

        historyService.logOperation(accountNumber, amount.negate(), OperationType.LOAN);
    }
}