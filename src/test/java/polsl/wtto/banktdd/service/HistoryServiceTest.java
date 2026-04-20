package polsl.wtto.banktdd.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import polsl.wtto.banktdd.domain.OperationLog;
import polsl.wtto.banktdd.domain.OperationType;
import polsl.wtto.banktdd.repository.OperationRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private HistoryService historyService;

    @Test
    void shouldReturnOperationHistoryForGivenAccount() {
        // given
        String accountNumber = "1111111119";

        // Tworzymy fałszywą historię (dwa logi)
        OperationLog log1 = new OperationLog(accountNumber, OperationType.DEPOSIT, new BigDecimal("200.00"));
        OperationLog log2 = new OperationLog(accountNumber, OperationType.WITHDRAWAL, new BigDecimal("50.00"));

        // Uczymy bazę: "Jak zapytam o ten numer, zwróć listę tych dwóch logów"
        when(operationRepository.findByAccountNumber(accountNumber)).thenReturn(List.of(log1, log2));

        // when: klient prosi o wygenerowanie raportu
        List<OperationLog> history = historyService.getAccountHistory(accountNumber);

        // then: sprawdzamy czy raport zawiera poprawne dane
        assertThat(history).isNotNull();
        assertThat(history).hasSize(2);

        assertThat(history.get(0).getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(history.get(0).getAmount()).isEqualTo(new BigDecimal("200.00"));

        assertThat(history.get(1).getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(history.get(1).getAmount()).isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    void shouldLogOperationSuccessfully() {
        // given
        String accountNumber = "1111111119";
        BigDecimal amount = new BigDecimal("100.00");
        OperationType type = OperationType.DEPOSIT;

        // when
        historyService.logOperation(accountNumber, amount, type);

        // then
        // Przechwytujemy to, co serwis chce zapisać
        ArgumentCaptor<OperationLog> logCaptor = ArgumentCaptor.forClass(OperationLog.class);
        verify(operationRepository).save(logCaptor.capture());

        OperationLog savedLog = logCaptor.getValue();

        // Sprawdzamy, czy dane w obiekcie Log zgadzają się z tym, co wysłaliśmy
        assertThat(savedLog.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(savedLog.getAmount()).isEqualByComparingTo(amount);
        assertThat(savedLog.getType()).isEqualTo(type);
    }
}