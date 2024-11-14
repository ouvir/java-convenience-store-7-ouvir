package store.utils;

import store.exception.InputDataException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static store.exception.InputException.PRODUCT_NAME_AND_COUNT_ERROR;

public class Separator {
    private static final String COMMA = ",";
    private static final String HYPHEN = "-";

    public static List<String> split(final String data) {
        return Arrays.stream(data.split(COMMA)).toList();
    }

    public static Map<String, Integer> separateProductNameAndCount(final List<String> productNameAndCount) {
        Map<String, Integer> orderItems = new HashMap<>();
        try {
            productNameAndCount.stream()
                    .map(product -> product.substring(1, product.length() - 1).split(HYPHEN))
                    .forEach(product -> orderItems.put(product[0].trim(), Integer.parseInt(product[1])));
            return orderItems;
        } catch (NumberFormatException e) {
            throw new InputDataException(PRODUCT_NAME_AND_COUNT_ERROR);
        }
    }
}
