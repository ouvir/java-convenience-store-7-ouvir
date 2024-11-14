package store.dto;

public class ProductDTO {
    private String productName;
    private int productPrice;
    private int productCount;
    private String promotionName;

    public ProductDTO(
            final String productName,
            final int productPrice,
            final int productCount,
            final String promotionName

    ) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCount = productCount;
        this.promotionName = promotionName;
    }

    public ProductDTO(final String productName, final int productPrice, final int productCount) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCount = productCount;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductCount() {
        return productCount;
    }

    public String getPromotionName() {
        return promotionName;
    }
}
