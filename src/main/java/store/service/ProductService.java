package store.service;

import store.exception.InputDataException;
import store.model.Inventory;
import store.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static store.exception.InputException.*;

public class ProductService {

    public void validateProducts(final Map<String, Integer> cart, final Inventory inventory) throws InputDataException {
        checkExistItem(cart, inventory);
        checkItemCount(cart, inventory);
    }

    public List<Product> addToCart(final Map<String, Integer> orderItems, final Inventory inventory) {
        List<Product> cart = new ArrayList<>();
        orderItems.forEach((itemName, count) -> cart.addAll(inventory.getProduct(itemName, count)));
        return cart;
    }

    private void checkExistItem(final Map<String, Integer> cart, final Inventory inventory) {
        boolean existItem = cart.keySet().stream()
                .allMatch(itemName -> !itemName.isEmpty() && inventory.hasProduct(itemName));
        if (!existItem) {
            throw new InputDataException(NO_EXIST_PRODUCT_ERROR);
        }
    }

    private void checkItemCount(final Map<String, Integer> cart, final Inventory inventory) {
        boolean validCount = cart.entrySet().stream()
                .allMatch(entry -> inventory.hasEnoughProduct(entry.getKey(), entry.getValue()));
        if (!validCount) {
            throw new InputDataException(OUT_OF_COUNT_ERROR);
        }
    }
}
