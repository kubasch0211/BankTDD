package polsl.wtto.banktdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import polsl.wtto.banktdd.domain.OperationLog;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<OperationLog, Long> {
    List<OperationLog> findByAccountNumber(String accountNumber);
}
