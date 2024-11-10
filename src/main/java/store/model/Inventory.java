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

    public boolean hasProduct(final String productName) {
        if (promotionProducts.containsKey(productName)) {
            return true;
        }
        if (normalProducts.containsKey(productName)) {
            return true;
        }
        return false;
    }

    public boolean hasEnoughProduct(final String itemName, final int count) {
        int totalCount = 0;
        totalCount += promotionProducts.get(itemName).getCount();
        totalCount += normalProducts.get(itemName).getCount();
        return totalCount >= count;
    }

    public List<Product> getProduct(final String itemName, final int count) {
        int promotionProductCount = promotionProducts.get(itemName).getCount();
        if (promotionProductCount >= count) {
            return getProductFromPromotionProducts(itemName, count);
        }
        return getProductFromPromotionProductsAndNormalProducts(itemName, promotionProductCount, count);
    }

    private List<Product> getProductFromPromotionProducts(final String itemName, final int count) {
        List<Product> products = new ArrayList<>();
        Product product = promotionProducts.get(itemName).extractProductUnits(count);
        products.add(product);
        return products;
    }

    private List<Product> getProductFromPromotionProductsAndNormalProducts(
            String itemName,
            int promotionProductCount,
            int count
    ) {
        List<Product> products = new ArrayList<>();
        Product promotionProduct = promotionProducts.get(itemName).extractProductUnits(promotionProductCount);
        Product normalProduct = normalProducts.get(itemName).extractProductUnits(count - promotionProductCount);
        products.add(promotionProduct);
        products.add(normalProduct);
        return products;
    }
}
