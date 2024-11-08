package store.model;

import store.dto.InventoryDTO;
import store.dto.ProductDTO;

import java.util.*;

public class Inventory {
    private final Map<String, Product> promotionProducts = new LinkedHashMap<>();
    private final Map<String, Product> normalProducts = new LinkedHashMap<>();


    public Inventory(final List<Product> products) {
        for (Product product : products) {
            separateNormalAndPromotion(product);
        }
        addAllMissingNormalProducts();
    }

    private void separateNormalAndPromotion(Product product) {
        if (product.isPromotion()) {
            promotionProducts.put(product.getName(), product);
            return;
        }
        normalProducts.put(product.getName(), product);
    }

    private void addAllMissingNormalProducts() {
        promotionProducts.keySet().stream()
                .filter(productName -> !normalProducts.containsKey(productName))
                .forEach(this::addMissingNormalProducts);
    }

    private void addMissingNormalProducts(String productName) {
        normalProducts.put(productName, promotionProducts.get(productName).makeNormalAndNoCountProduct());
    }

    public InventoryDTO convertToDTO() {
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (String productName : normalProducts.keySet()) {
            if (promotionProducts.containsKey(productName)) {
                productDTOs.add(promotionProducts.get(productName).convertToDTO());
            }
            productDTOs.add(normalProducts.get(productName).convertToDTO());
        }
        InventoryDTO inventoryDTO = new InventoryDTO(productDTOs);
        return inventoryDTO;
    }
}
