package store.dto;

import java.util.List;

public class ReceiptDTO {
    private final List<ProductDTO> products;
    private final List<ProductDTO> promotionalProducts;
    private final int totalCount;
    private final int totalPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int amountToPay;

    public ReceiptDTO(
            List<ProductDTO> products,
            List<ProductDTO> promotionalProducts,
            int totalCount,
            int totalPrice,
            int promotionDiscount,
            int membershipDiscount,
            int amountToPay
    ) {
        this.products = products;
        this.promotionalProducts = promotionalProducts;
        this.totalCount = totalCount;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.amountToPay = amountToPay;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public List<ProductDTO> getPromotionalProducts() {
        return promotionalProducts;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getAmountToPay() {
        return amountToPay;
    }
}
