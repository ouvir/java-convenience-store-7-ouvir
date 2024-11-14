package store.utils;

public class Validator {
    private final static String FORMAT_PRODUCT_NAME_AND_COUNT = "\\[\\p{L}+-\\d+\\](,\\[\\p{L}+-\\d+\\])*";

    public static boolean validateProductNameAndCountFormat(final String text) {
        return text.matches(FORMAT_PRODUCT_NAME_AND_COUNT);
    }

    public static boolean validateYorN(final String text) {
        return text.equals("Y") || text.equals("N");
    }
}
