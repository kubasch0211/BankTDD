package polsl.wtto.banktdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Account {

    @Getter
    @Setter
    private String accountNumber;

    @Getter
    @Setter
    private BigDecimal balance;

    public Account(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(BigDecimal depositAmount) {
        if (depositAmount == null) {
            throw new IllegalArgumentException("Kwota wpłaty nie może być pusta");
        }

        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Kwota wpłaty musi być większa od zera");
        }

        this.balance = this.balance.add(depositAmount);
    }

    public void withdraw(BigDecimal depositAmount) {
        if (depositAmount == null) {
            throw new IllegalArgumentException("Kwota wpłaty nie może być pusta");
        }

        if (depositAmount.compareTo(balance) > 0) {
            throw new IllegalStateException("Niewystarczające środki na koncie");
        }

        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Kwota wypłaty musi być większa od zera");
        }

        this.balance = this.balance.subtract(depositAmount);
    }
}