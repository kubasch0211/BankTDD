package polsl.wtto.banktdd.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountName;
    private BigDecimal initialAmount;
    private BigDecimal remainingDebt;

    public Loan(String accountName, BigDecimal initialAmount,BigDecimal remainingDebt ){
        this.accountName=accountName;
        this.initialAmount=initialAmount;
        this.remainingDebt=remainingDebt;
    }

}
