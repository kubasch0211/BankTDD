package polsl.wtto.banktdd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import polsl.wtto.banktdd.domain.OperationLog;
import polsl.wtto.banktdd.repository.OperationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final OperationRepository operationRepository;
    public List<OperationLog> getAccountHistory(String accountNumber){
        return operationRepository.findByAccountNumber(accountNumber);
    }
}
