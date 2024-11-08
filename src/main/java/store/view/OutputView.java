package store.view;

import store.dto.ProductDTO;

import java.util.List;

public class OutputView {
    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INVENTORY_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_INFO_MESSAGE = "- %s %,d원 %s %s";
    private static final String POSTFIX_PRODUCT_COUNT = "개";
    private static final String NO_PRODUCT_COUNT = "재고 없음";

    public void printGreetingMessage() {
        System.out.println(GREETING_MESSAGE);
    }

    public void printBlankLine() {
        System.out.println();
    }

    public void printInventory(final List<ProductDTO> inventorys) {
        System.out.println(INVENTORY_MESSAGE);
        printBlankLine();
        for (ProductDTO product : inventorys) {
            String productInfo = makeProductInfoMessage(product);
            System.out.println(productInfo);
        }
        printBlankLine();
    }

    private String makeProductInfoMessage(final ProductDTO product) {
        String productCount = checkCountValue(product.getProductCount());
        return String.format(
                PRODUCT_INFO_MESSAGE,
                product.getProductName(),
                product.getProductPrice(),
                productCount,
                product.getPromotionName()
        );
    }

    private String checkCountValue(int productCount) {
        if (productCount == 0) {
            return NO_PRODUCT_COUNT;
        }
        return (productCount) + POSTFIX_PRODUCT_COUNT;
    }
}
