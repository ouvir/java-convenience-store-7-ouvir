package store.service;

import store.exception.InputDataException;
import store.model.Inventory;
import store.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static store.exception.InputException.*;

public class ProductService {

    public List<Product> validateProducts(final Map<String, Integer> orderItems, final Inventory inventory) throws InputDataException {
        checkExistItem(orderItems, inventory);
        checkItemCount(orderItems, inventory);
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

    public Product makePromotionGift(final Product originProduct, final int promotionCount) {
        return new Product(
                originProduct.getName(),
                originProduct.getPrice(),
                promotionCount,
                originProduct.getPromotionName()
        );
    }
}
