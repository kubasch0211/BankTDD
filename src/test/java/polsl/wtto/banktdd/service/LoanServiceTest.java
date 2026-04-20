package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.Loan;
import polsl.wtto.banktdd.domain.OperationType;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.repository.LoanRepository;
import polsl.wtto.banktdd.service.validation.AccountNumberValidator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private AccountNumberValidator accountNumberValidator;

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldTakeLoanSuccessfully() {
        String accountNumber = "1111111119";
        BigDecimal loanAmount = new BigDecimal("1000.00");
        Account account = new Account(accountNumber, new BigDecimal("500.00"));

        when(accountNumberValidator.isValid(accountNumber)).thenReturn(true);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        loanService.takeLoan(accountNumber, loanAmount);

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("1500.00"));

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());
        assertThat(loanCaptor.getValue().getRemainingDebt()).isEqualByComparingTo(new BigDecimal("1100.00"));

        verify(historyService).logOperation(eq(accountNumber), any(BigDecimal.class), eq(OperationType.LOAN));
    }

    @Test
    void shouldThrowExceptionWhenAccountNumberIsInvalid() {
        String invalidAccountNumber = "ZLY_NUMER";
        when(accountNumberValidator.isValid(invalidAccountNumber)).thenReturn(false);

        assertThatThrownBy(() -> loanService.takeLoan(invalidAccountNumber, new BigDecimal("1000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nieprawidłowy numer konta");

        verifyNoInteractions(loanRepository);
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(historyService);
    }

    @Test
    void shouldRepayLoanSuccessfully() {
        String accountNumber = "1111111119";
        BigDecimal repaymentAmount = new BigDecimal("200.00");

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(new BigDecimal("1000.00"));

        Loan loan = new Loan();
        loan.setAccountName(accountNumber);
        loan.setRemainingDebt(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(loanRepository.findByAccountName(accountNumber)).thenReturn(Optional.of(loan));

        loanService.repayLoan(accountNumber, repaymentAmount);

        assertEquals(new BigDecimal("800.00"), account.getBalance());
        assertEquals(new BigDecimal("300.00"), loan.getRemainingDebt());

        verify(accountRepository).save(account);
        verify(loanRepository).save(loan);

        verify(historyService).logOperation(eq(accountNumber), any(BigDecimal.class), eq(OperationType.LOAN));
    }
}