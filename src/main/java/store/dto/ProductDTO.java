package store.dto;

public class ProductDTO {
    private String productName;
    private int productPrice;
    private int productCount;
    private String promotionName;

    public ProductDTO(String productName, int productPrice, int productCount, String promotionName) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCount = productCount;
        this.promotionName = promotionName;
    }

    public ProductDTO(String productName, int productPrice, int productCount) {
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
