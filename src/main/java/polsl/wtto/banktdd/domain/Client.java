package polsl.wtto.banktdd.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String pesel;
    private String accountNumber;


    public Client(String firstName, String lastName, String pesel, String accountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.accountNumber=accountNumber;
    }

    public void changeLastName(String newLastName) {
        if (newLastName == null || newLastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwisko nie może być puste");
        }
        this.lastName = newLastName;
    }
}