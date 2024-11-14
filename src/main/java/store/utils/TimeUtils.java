package store.utils;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;

public class TimeUtils {

    public static LocalDate getNowLocalDate() {
        return DateTimes.now().toLocalDate();
    }
}
