package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.Loan;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.repository.LoanRepository;
import polsl.wtto.banktdd.service.validation.AccountNumberValidator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldTakeLoanSuccessfully() {
        // given
        String accountNumber = "1111111119";
        BigDecimal loanAmount = new BigDecimal("1000.00");
        Account account = new Account(accountNumber, new BigDecimal("500.00"));

        when(accountNumberValidator.isValid(accountNumber)).thenReturn(true);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        // when
        loanService.takeLoan(accountNumber, loanAmount);

        // then
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("1500.00"));

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());
        assertThat(loanCaptor.getValue().getRemainingDebt()).isEqualByComparingTo(new BigDecimal("1100.00"));
    }

    @Test
    void shouldThrowExceptionWhenAccountNumberIsInvalid() {
        // given
        String invalidAccountNumber = "ZLY_NUMER";
        when(accountNumberValidator.isValid(invalidAccountNumber)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> loanService.takeLoan(invalidAccountNumber, new BigDecimal("1000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nieprawidłowy numer konta");

        verifyNoInteractions(loanRepository);
        verifyNoInteractions(accountRepository);
    }
}