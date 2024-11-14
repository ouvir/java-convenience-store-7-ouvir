package store.utils;

public class Membership {
    private static final int MAX_DISCOUNT = 8000;
    private static final int DISCOUNT_PERCENT = 30;
    private static final int PERCENT_DIVISOR = 100;

    public static int getDiscount(final int price) {
        int discount = price * DISCOUNT_PERCENT / PERCENT_DIVISOR;
        if (discount > MAX_DISCOUNT) {
            discount = MAX_DISCOUNT;
        }
        return discount;
    }
}
