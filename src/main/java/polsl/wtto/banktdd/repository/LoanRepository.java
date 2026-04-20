package polsl.wtto.banktdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import polsl.wtto.banktdd.domain.Account;
import polsl.wtto.banktdd.domain.Loan;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByAccountName(String accountName);
}
