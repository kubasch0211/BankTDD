package polsl.wtto.banktdd.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

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