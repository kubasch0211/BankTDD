package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.Account;
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

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldTransferMoneySuccessfully() {
        // given
        String fromAccNumber = "1111111119";
        String toAccNumber = "2222222224"; // Suma = 14, modulo = 4.
        BigDecimal amount = new BigDecimal("100.00");

        Account sourceAccount = new Account(fromAccNumber, new BigDecimal("500.00"));
        Account targetAccount = new Account(toAccNumber, new BigDecimal("100.00"));

        // Uczymy nasze atrapy, jak mają się zachować w tym konkretnym teście
        when(validator.isValid(toAccNumber)).thenReturn(true);
        when(accountRepository.findByAccountNumber(fromAccNumber)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(toAccNumber)).thenReturn(Optional.of(targetAccount));

        // when
        transactionService.transfer(fromAccNumber, toAccNumber, amount);

        // then
        // 1. Sprawdzamy czy stan kont w pamięci się zmienił
        assertThat(sourceAccount.getBalance()).isEqualTo(new BigDecimal("400.00"));
        assertThat(targetAccount.getBalance()).isEqualTo(new BigDecimal("200.00"));

        // 2. Weryfikujemy, czy serwis kazał bazie danych zapisać nowe stany obu kont
        verify(accountRepository).save(sourceAccount);
        verify(accountRepository).save(targetAccount);
    }

    @Test
    void shouldThrowExceptionWhenTargetAccountIsInvalid() {
        // given
        String fromAccNumber = "1111111119";
        String invalidTargetNumber = "ZLY_NUMER";
        BigDecimal amount = new BigDecimal("100.00");

        // Walidator mówi: numer jest błędny
        when(validator.isValid(invalidTargetNumber)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> transactionService.transfer(fromAccNumber, invalidTargetNumber, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nieprawidłowy numer konta docelowego");

        // Upewniamy się, że przy błędzie nie dotknęliśmy bazy danych
        verifyNoInteractions(accountRepository);
    }
}