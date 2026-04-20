package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.OperationLog;
import polsl.wtto.banktdd.domain.OperationType;
import polsl.wtto.banktdd.repository.AccountRepository;
import polsl.wtto.banktdd.repository.OperationRepository;
import polsl.wtto.banktdd.service.validation.AccountNumberValidator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtmServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountNumberValidator accountNumberValidator;
    @InjectMocks
    private AtmService atmService;

    @Test
    void shouldDepositMoneyAndSaveOperationLog() {
        // given
        String accountNumber = "1111111119";
        BigDecimal amount = new BigDecimal("200.00");
        Account account = new Account(accountNumber, new BigDecimal("100.00"));

        // Uczymy walidatora, że ten numer jest poprawny
        when(accountNumberValidator.isValid(accountNumber)).thenReturn(true);
        // Uczymy bazę, że ma nam zwrócić nasze konto
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        // when: klient wpłaca pieniądze w bankomacie
        atmService.deposit(accountNumber, amount);

        // then:
        // 1. Sprawdzamy czy na koncie jest odpowiednia kwota i czy zaktualizowano konto w bazie
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("300.00"));
        verify(accountRepository).save(account);

        // 2. Przechwytujemy "paragon" operacji i sprawdzamy, czy zapisano w nim poprawne dane
        ArgumentCaptor<OperationLog> logCaptor = ArgumentCaptor.forClass(OperationLog.class);
        verify(operationRepository).save(logCaptor.capture());

        OperationLog savedLog = logCaptor.getValue();
        assertThat(savedLog.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(savedLog.getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(savedLog.getAmount()).isEqualTo(amount);
    }
}