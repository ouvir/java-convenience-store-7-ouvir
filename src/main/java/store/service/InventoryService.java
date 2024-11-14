package store.service;

import store.model.Inventory;
import store.model.Product;
import store.utils.Reader;

import java.util.List;

public class InventoryService {

    public List<Product> readProductsFile() {
        List<List<String>> products = Reader.readFile("products.md");
        if (products == null) {
            return null;
        }
        return products.stream().map(Product::new).toList();
    }

    public boolean checkProductCountFromPromotionProducts(final Inventory inventory, final String name) {
        return inventory.checkProductCountFromPromotionProducts(name, 1);
    }

    public void returnProduct(
            final Inventory inventory,
            final String name,
            final int notApplyPromotionCount,
            final int normalCount
    ) {
        inventory.increaseProductCount(name, notApplyPromotionCount, normalCount);
    }

    public void takeOutFewProductInInventory(
            final Inventory inventory,
            final String name,
            final int promotion,
            final int normal
    ) {
        inventory.decreaseProductCount(name, promotion, normal);
    }
}
