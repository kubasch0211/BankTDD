package polsl.wtto.banktdd.domain;

import jakarta.persistence.*; // w starszych Springach to javax.persistence.*
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // 1. Mówimy Springowi: To jest tabela w bazie!
@Table(name = "clients")
@Getter
@NoArgsConstructor // 2. Pusty konstruktor jest wymagany przez technologię JPA
public class Client {

    @Id // 3. Klucz główny
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Baza sama będzie nadawać ID (1, 2, 3...)
    private Long id;

    private String firstName;
    private String lastName;
    private String pesel;

    // 5. Konstruktor dla nas do tworzenia obiektu przed zapisem (baza nada ID po zapisie)
    public Client(String firstName, String lastName, String pesel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
    }
}