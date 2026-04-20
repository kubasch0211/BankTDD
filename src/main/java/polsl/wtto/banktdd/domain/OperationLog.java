package polsl.wtto.banktdd.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="operation_logs")
@Getter
@NoArgsConstructor
@Setter
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String accountNumber;
    @Enumerated(EnumType.STRING) OperationType type;
    BigDecimal amount;

    public OperationLog(String accountNumber, OperationType operationType, BigDecimal amount) {
        this.accountNumber=accountNumber;
        this.type=operationType;
        this.amount=amount;
    }
}
