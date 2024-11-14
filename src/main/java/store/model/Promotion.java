package store.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Promotion {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate start_date;
    private final LocalDate end_date;

    public Promotion(
            final String name,
            final int buy,
            final int get,
            final LocalDate start_date,
            final LocalDate end_date
    ) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Promotion(final List<String> infos) {
        this.name = infos.get(0);
        this.buy = Integer.parseInt(infos.get(1));
        this.get = Integer.parseInt(infos.get(2));
        this.start_date = LocalDate.parse(infos.get(3), DATE_TIME_FORMATTER);
        this.end_date = LocalDate.parse(infos.get(4), DATE_TIME_FORMATTER);
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public boolean isValidDate(final LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(start_date) && date.isBefore(end_date);
    }
}
