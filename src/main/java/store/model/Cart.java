package store.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> totalItems = new ArrayList<>(); // 모든 구매 상품
    private final List<Product> promotionalItems = new ArrayList<>(); // 프로모션 적용된 상품
    private final List<Product> freeItems = new ArrayList<>(); // 프로모션으로 얻은 상품

    public List<Product> getTotalItems() {
        return totalItems;
    }

    public List<Product> getFreeItems() {
        return freeItems;
    }

    public void addAllNormalCart(List<Product> products) {
        totalItems.addAll(products);
    }

    public void addAllPromotionalItems(List<Product> products) {
        promotionalItems.addAll(products);
    }

    public void addAllFreeItems(List<Product> products) {
        freeItems.addAll(products);
    }

    public List<Product> getPromotionProductList() {
        return totalItems.stream()
                .filter(Product::isPromotion)
                .toList();
    }

    public boolean hasNormalProduct(String name) {
        return getProductInCart(name).stream().anyMatch(product -> !product.isPromotion());
    }

    public int getNormalProductCount(String name) {
        for (Product product : totalItems) {
            if (product.getName().equals(name) && !product.isPromotion()) {
                return product.getCount();
            }
        }
        return 0;
    }

    public void decreaseProductCountInCart(String name, int promotionCount, int normalCount) {
        getProductInCart(name).forEach(product -> {
            if (product.isPromotion()) {
                product.setCount(product.getCount() - promotionCount);
                return;
            }
            product.setCount(product.getCount() - normalCount);
        });
    }

    public void increaseProductCountInCart(String name, int promotionCount, int normalCount) {
        getProductInCart(name).forEach(product -> {
            if (product.isPromotion()) {
                product.setCount(product.getCount() + promotionCount);
                return;
            }
            product.setCount(product.getCount() + normalCount);
        });
    }

    private List<Product> getProductInCart(String name) {
        return totalItems.stream().filter(product -> product.getName().equals(name)).toList();
    }

    public int getTotalPrice() {
        return totalItems.stream()
                .map(product -> product.getCount() * product.getPrice())
                .reduce(0, Integer::sum);
    }

    public int getPromotionalItemsPrice() {
        return promotionalItems.stream()
                .map(product -> product.getCount() * product.getPrice())
                .reduce(0, Integer::sum);
    }

    public int getPromotionDiscountPrice() {
        return freeItems.stream()
                .map(product -> product.getCount() * product.getPrice())
                .reduce(0, Integer::sum);
    }

    public int getSpecificProductPrice(String productName) {
        for (Product product : totalItems) {
            if (product.getName().equals(productName)) {
                return product.getPrice();
            }
        }
        throw new IllegalArgumentException("Product not found");
    }
}