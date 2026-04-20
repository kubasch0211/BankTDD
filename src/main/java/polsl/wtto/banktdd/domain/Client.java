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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String pesel;


    public Client(String firstName, String lastName, String pesel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
    }

    public void changeLastName(String newLastName) {
        if (newLastName == null || newLastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwisko nie może być puste");
        }
        this.lastName = newLastName;
    }
}