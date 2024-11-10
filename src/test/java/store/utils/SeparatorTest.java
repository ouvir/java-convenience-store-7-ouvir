package store.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.exception.InputDataException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static store.utils.Separator.separateProductNameAndCount;

class SeparatorTest {

    @Test
    @DisplayName("컴마 구분 테스트")
    void split() {
        List<String> expected = List.of("[콜라-10]","[사이다-3]");
        List<String> actual = Separator.split("[콜라-10],[사이다-3]");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("상품과 수량 구분 테스트")
    void separateProductNameAndCount() {
        Map<String, Integer> expected = Map.of("콜라",10, "사이다", 3);
        List<String> splitItems = Separator.split("[콜라-10],[사이다-3]");
        Map<String, Integer> actual = Separator.separateProductNameAndCount(splitItems);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("상품과 수량 숫자 예외 테스트")
    void separateProductNameAndCountTrim() {
        List<String> splitItems = Separator.split("[콜라 -10 ],[ 사이다-3 ]");
        assertThrows(InputDataException.class, () -> Separator.separateProductNameAndCount(splitItems));
    }
}