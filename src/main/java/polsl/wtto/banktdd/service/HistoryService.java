package polsl.wtto.banktdd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import polsl.wtto.banktdd.domain.OperationLog;
import polsl.wtto.banktdd.domain.OperationType;
import polsl.wtto.banktdd.repository.OperationRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final OperationRepository operationRepository;
    public List<OperationLog> getAccountHistory(String accountNumber){
        return operationRepository.findByAccountNumber(accountNumber);
    }

    @Transactional
    public void logOperation(String accountNumber, BigDecimal amount, OperationType type) {
        OperationLog log = new OperationLog();
        log.setAccountNumber(accountNumber);
        log.setAmount(amount);
        log.setType(type);

        operationRepository.save(log);
    }
}
