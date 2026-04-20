package polsl.wtto.banktdd.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Getter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Baza sama będzie nadawać ID (1, 2, 3...)
    private Long id;

    private String firstName;
    private String lastName;
    private String pesel;


    public Client(String firstName, String lastName, String pesel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
    }
}