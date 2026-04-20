package polsl.wtto.banktdd.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AccountNumberValidatorTest {

    private final AccountNumberValidator validator = new AccountNumberValidator();

    @Test
    void shouldReturnTrueForValidAccountNumber() {
        // given
        String validAccountNumber = "1111111119";

        // when
        boolean isValid = validator.isValid(validAccountNumber);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseForInvalidControlDigit() {
        // given
        String invalidAccountNumber = "1111111115";

        // when
        boolean isValid = validator.isValid(invalidAccountNumber);

        // then
        assertThat(isValid).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "11111111111", "", "abcdefghij", "11111 1119"})
    void shouldReturnFalseForInvalidFormat(String invalidFormatNumber) {
        // when
        boolean isValid = validator.isValid(invalidFormatNumber);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalseForNull() {
        // when
        boolean isValid = validator.isValid(null);

        // then
        assertThat(isValid).isFalse();
    }
}