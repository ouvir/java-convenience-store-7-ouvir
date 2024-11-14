package store.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @ParameterizedTest
    @DisplayName("구매 상품과 수량 포맷 확인 테스트")
    @ValueSource(strings = {"[콜라-10],[사이다-3]","[감자칩-10],[사이다-3]","[오렌지주스-10],[컵라면-3]","[a-10],[b-3]"})
    void validateProductNameAndCountFormat(String productNameAndCount) {
        assertTrue(Validator.validateProductNameAndCountFormat(productNameAndCount));
    }

    @ParameterizedTest
    @DisplayName("구매 상품과 수량 포맷 확인 예외 테스트")
    @ValueSource(strings = {"[콜라- 10],[사이다-3]","[콜라-10],[사 이다-3]","콜라-10],[사이다-3]","[a-a],[b--3]"})
    void validateProductNameAndCountFormat_X(String productNameAndCount) {
        assertFalse(Validator.validateProductNameAndCountFormat(productNameAndCount));
    }
}