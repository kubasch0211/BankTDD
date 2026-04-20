package polsl.wtto.banktdd.dto;

import lombok.Data;

@Data
public class ClientRequest {
    private String firstName;
    private String lastName;
    private String pesel;
    private String accountNumber;
}