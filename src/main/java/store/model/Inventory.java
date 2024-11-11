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

    private void separateNormalAndPromotion(final Product product) {
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

    private void addMissingNormalProducts(final String productName) {
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
        totalCount += getPromitionProductsCount(itemName);
        totalCount += normalProducts.get(itemName).getCount();
        return totalCount >= count;
    }

    public boolean checkProductCountFromPromotionProducts(final String itemName, final int count) {
        int promotionCount = getPromitionProductsCount(itemName);
        return promotionCount >= count;
    }

    public List<Product> getProduct(final String itemName, final int count) {
        if (checkProductCountFromPromotionProducts(itemName, count)) {
            return getProductFromPromotionProducts(itemName, count);
        }
        int promotionProductCount = getPromitionProductsCount(itemName);
        return getProductFromPromotionProductsAndNormalProducts(itemName, promotionProductCount, count);
    }

    private int getPromitionProductsCount(final String itemName) {
        if (promotionProducts.containsKey(itemName)) {
            return promotionProducts.get(itemName).getCount();
        }
        return 0;
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
        if (promotionProductCount > 0) {
            Product promotionProduct = promotionProducts.get(itemName).extractProductUnits(promotionProductCount);
            products.add(promotionProduct);
        }
        Product normalProduct = normalProducts.get(itemName).extractProductUnits(count - promotionProductCount);
        products.add(normalProduct);
        return products;
    }

    public void returnProduct(final String itemName, final int promotionCount, final int normalCount) {
        if (promotionCount > 0) {
            Product promotionProduct = promotionProducts.get(itemName);
            promotionProduct.setCount(promotionProduct.getCount() + promotionCount);
        }
        if (normalCount > 0) {
            Product normalProduct = normalProducts.get(itemName);
            normalProduct.setCount(normalProduct.getCount() + normalCount);
        }
    }

    public void decreaseProductCount(final String itemName, final int promotionCount, final int normalCount) {
        if (promotionCount > 0) {
            Product promotionProduct = promotionProducts.get(itemName);
            promotionProduct.setCount(promotionProduct.getCount() - promotionCount);
        }
        if (normalCount > 0) {
            Product normalProduct = normalProducts.get(itemName);
            normalProduct.setCount(normalProduct.getCount() - normalCount);
        }
    }
}
