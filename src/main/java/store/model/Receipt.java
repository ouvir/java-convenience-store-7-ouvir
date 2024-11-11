package store.model;

import store.dto.ProductDTO;
import store.dto.ReceiptDTO;

import java.util.*;

public class Receipt {
    private Cart cart;
    private int membershipDiscount;


    public Receipt(final Cart cart, final int membershipDiscount) {
        this.cart = cart;
        this.membershipDiscount = membershipDiscount;
    }

    public int getAmountToPay() {
        // 총 구매액 - 프로모션할인 - 멤버십 할인
        int totalCount = cart.getTotalPrice();
        int promotionDiscount = cart.getPromotionDiscountPrice();
        return totalCount - (promotionDiscount + membershipDiscount);
    }

    public ReceiptDTO convertToDTO() {
        List<ProductDTO> combinedTotalItems = combineProducts();
        return new ReceiptDTO(
                combinedTotalItems,
                cart.getFreeItems().stream().map(Product::convertToDTO).toList(),
                combinedTotalItems.stream().map(ProductDTO::getProductCount).reduce(0, Integer::sum),
                cart.getTotalPrice(),
                cart.getPromotionDiscountPrice(),
                membershipDiscount,
                getAmountToPay()
        );
    }

    private List<ProductDTO> combineProducts() {
        // 프로모션과 일반제품을 모아 하나의 상품으로 합쳐 반환
        Map<String, Integer> items = mappingProducts();
        List<ProductDTO> dtos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String productName = entry.getKey();
            int productCount = entry.getValue();
            dtos.add(new ProductDTO(productName, cart.getSpecificProductPrice(productName), productCount));
        }
        return dtos;
    }

    private Map<String, Integer> mappingProducts() {
        Map<String, Integer> items = new LinkedHashMap<>();
        for (Product product : cart.getTotalItems()) {
            String productName = product.getName();
            if (items.containsKey(productName)) {
                items.put(productName, items.get(productName) + product.getCount());
                continue;
            }
            items.put(productName, product.getCount());
        }
        return items;
    }
}
