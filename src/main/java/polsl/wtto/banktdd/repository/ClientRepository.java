package polsl.wtto.banktdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import polsl.wtto.banktdd.domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByPesel(String pesel);
}