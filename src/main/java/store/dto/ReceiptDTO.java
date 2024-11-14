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
            final List<ProductDTO> products,
            final List<ProductDTO> promotionalProducts,
            final int totalCount,
            final int totalPrice,
            final int promotionDiscount,
            final int membershipDiscount,
            final int amountToPay
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
