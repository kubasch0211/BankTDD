package polsl.wtto.banktdd.service.validation;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberValidator {
    public boolean isValid(String accountNumber) {
        if (accountNumber == null || accountNumber.length() != 10) return false;
        int controlSum = 0;
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(accountNumber.charAt(i))) return false;
            controlSum += Character.getNumericValue(accountNumber.charAt(i));
        }

        if (!Character.isDigit(accountNumber.charAt(9))) return false;
        return Character.getNumericValue(accountNumber.charAt(9)) == controlSum % 10;


    }

}
