package polsl.wtto.banktdd.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    @Test
    void shouldDepositMoneySuccessfully() {
        // given: Tworzymy konto z prawidłowym numerem i saldem początkowym 0.00
        Account account = new Account("1234567895", BigDecimal.ZERO);
        BigDecimal depositAmount = new BigDecimal("150.50");

        // when: Zasilamy konto
        account.deposit(depositAmount);

        // then: Saldo powinno wynosić 150.50
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("150.50"));
    }

    @Test
    void shouldAddDepositToExistingBalance() {
        // given: Konto ma już 100.00 zł
        Account account = new Account("1234567895", new BigDecimal("100.00"));
        BigDecimal depositAmount = new BigDecimal("50.00");

        // when
        account.deposit(depositAmount);

        // then: 100 + 50 = 150
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void shouldThrowExceptionWhenDepositAmountIsNegative() {
        // given
        Account account = new Account("1234567895", new BigDecimal("100.00"));
        BigDecimal negativeAmount = new BigDecimal("-50.00");

        // when & then: Wpłata ujemnej kwoty z bankomatu to oszustwo, rzucamy błąd!
        assertThatThrownBy(() -> account.deposit(negativeAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kwota wpłaty musi być większa od zera");
    }

    @Test
    void shouldThrowExceptionWhenDepositAmountIsNull() {
        // given
        Account account = new Account("1234567895", BigDecimal.ZERO);

        // when & then
        assertThatThrownBy(() -> account.deposit(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kwota wpłaty nie może być pusta");
    }

    @Test
    void shouldWithdrawMoneySuccessfully() {
        // given: Konto ma 200.00 zł
        Account account = new Account("1234567895", new BigDecimal("200.00"));
        BigDecimal withdrawAmount = new BigDecimal("50.00");

        // when: Wypłacamy 50.00 zł
        account.withdraw(withdrawAmount);

        // then: Powinno zostać 150.00 zł
        assertThat(account.getBalance()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void shouldThrowExceptionWhenFundsAreInsufficient() {
        // given: Konto ma tylko 100.00 zł
        Account account = new Account("1234567895", new BigDecimal("100.00"));
        BigDecimal tooLargeAmount = new BigDecimal("150.00");

        // when & then: Próba wypłaty 150.00 zł powinna rzucić błąd stanu (IllegalStateException)
        assertThatThrownBy(() -> account.withdraw(tooLargeAmount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Niewystarczające środki na koncie");
    }

    @Test
    void shouldThrowExceptionWhenWithdrawAmountIsNegativeOrZero() {
        // given
        Account account = new Account("1234567895", new BigDecimal("100.00"));
        BigDecimal negativeAmount = new BigDecimal("-10.00");

        // when & then: Podobnie jak przy wpłacie, nie można wypłacić kwoty ujemnej
        assertThatThrownBy(() -> account.withdraw(negativeAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Kwota wypłaty musi być większa od zera");
    }
}