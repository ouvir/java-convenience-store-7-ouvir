package store.model;

import store.dto.ProductDTO;

import java.util.List;

public class Product {
    private static final String NO_PROMOTION = "";

    private final String name;
    private final Integer price;
    private Integer count;
    private final String promotionName;

    public Product(final String name, final Integer price, final Integer count, final String promotionName) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.promotionName = checkPromotion(promotionName);
    }

    public Product(final String name, final Integer price, final Integer count) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.promotionName = NO_PROMOTION;
    }

    public Product(final List<String> strings) {
        if (strings.size() < 4) {
            throw new IllegalArgumentException("Product must have at least 4 products");
        }
        this.name = strings.get(0);
        this.price = Integer.parseInt(strings.get(1));
        this.count = Integer.parseInt(strings.get(2));
        this.promotionName = checkPromotion(strings.get(3));
    }

    public ProductDTO convertToDTO() {
        return new ProductDTO(name, price, count, promotionName);
    }


    private String checkPromotion(final String promotionName) {
        if (promotionName.equals("null")) {
            return NO_PROMOTION;
        }
        return promotionName;
    }

    public boolean isPromotion() {
        return !promotionName.equals(NO_PROMOTION);
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getPrice() {
        return price;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public Product makeNormalAndNoCountProduct() {
        return new Product(this.name, this.price, 0, NO_PROMOTION);
    }

    public Product extractProductUnits(int count) {
        this.count -= count;
        return new Product(this.name, this.price, count, this.promotionName);
    }

    public void setCount(int count) {
        if (count < 0) {
            count = 0;
        }
        this.count = count;
    }
}
