package polsl.wtto.banktdd.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanRepaymentRequest {
    private String accountNumber;
    private BigDecimal amount;
}