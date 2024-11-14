package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static org.junit.jupiter.api.Assertions.*;
import static store.utils.TimeUtils.getNowLocalDate;

class PromotionTest extends NsTest {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    @DisplayName("날짜 확인 메서드")
    void isValidDate() {
        Promotion promotion = new Promotion(
                "프로모션 테스트",
                2,
                1,
                LocalDate.parse("2024-01-01", DATE_TIME_FORMATTER),
                LocalDate.parse("2024-02-03", DATE_TIME_FORMATTER)
        );

        assertNowTest(() -> {
            LocalDate nowDate = getNowLocalDate();
            assertTrue(promotion.isValidDate(nowDate));
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Override
    protected void runMain() {

    }
}