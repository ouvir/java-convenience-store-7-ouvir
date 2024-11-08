package store.service;

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
}
