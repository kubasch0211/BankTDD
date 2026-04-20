package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.OperationType;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.service.validation.AccountNumberValidator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberValidator validator;

    @Mock
    private HistoryService historyService; // To naprawia NullPointerException

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldTransferMoneySuccessfully() {
        // given
        String fromAccNumber = "1111111119";
        String toAccNumber = "2222222228"; // Suma = 18, modulo = 8.
        BigDecimal amount = new BigDecimal("100.00");

        Account sourceAccount = new Account(fromAccNumber, new BigDecimal("500.00"));
        Account targetAccount = new Account(toAccNumber, new BigDecimal("100.00"));

        when(validator.isValid(toAccNumber)).thenReturn(true);
        when(accountRepository.findByAccountNumber(fromAccNumber)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(toAccNumber)).thenReturn(Optional.of(targetAccount));

        // when
        transactionService.transfer(fromAccNumber, toAccNumber, amount);

        // then
        assertThat(sourceAccount.getBalance()).isEqualByComparingTo(new BigDecimal("400.00"));
        assertThat(targetAccount.getBalance()).isEqualByComparingTo(new BigDecimal("200.00"));

        verify(accountRepository).save(sourceAccount);
        verify(accountRepository).save(targetAccount);

        // Weryfikacja logowania historii dla obu stron
        verify(historyService).logOperation(eq(fromAccNumber), eq(amount.negate()), eq(OperationType.TRANSFER));
        verify(historyService).logOperation(eq(toAccNumber), eq(amount), eq(OperationType.TRANSFER));
    }

    @Test
    void shouldThrowExceptionWhenTargetAccountIsInvalid() {
        // given
        String fromAccNumber = "1111111119";
        String invalidTargetNumber = "ZLY_NUMER";
        BigDecimal amount = new BigDecimal("100.00");

        when(validator.isValid(invalidTargetNumber)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> transactionService.transfer(fromAccNumber, invalidTargetNumber, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nieprawidłowy numer konta docelowego");

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(historyService);
    }
}