package store.view;

import store.dto.ProductDTO;
import store.dto.ReceiptDTO;

import java.util.List;

import static store.utils.ReceiptMessage.*;

public class OutputView {
    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INVENTORY_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_INFO_MESSAGE = "- %s %,d원 %s %s";
    private static final String POSTFIX_PRODUCT_COUNT = "개";
    private static final String NO_PRODUCT_COUNT = "재고 없음";
    private static final String RETURN_PRODUCT_MESSAGE = "%s %d개를 제외하고 계산하겠습니다.";
    private static final String ENTER = "%n";

    public void printGreetingMessage() {
        printBlankLine();
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

    public void printErrorMessage(final String ErrorMessage) {
        printBlankLine();
        System.out.println(ErrorMessage);
    }

    public void printReturnProduct(final String productName, final int productCount) {
        printBlankLine();
        System.out.printf(RETURN_PRODUCT_MESSAGE + "%n", productName, productCount);
    }

    public void printReceipt(ReceiptDTO receiptDTO) {
        printBlankLine();
        System.out.println(RECEIPT_HEADER.getMessage());
        System.out.println(RECEIPT_ITEM_HEADER.getMessage());
        printProductsInfo(receiptDTO);
        System.out.println(RECEIPT_GIFT_HEADER.getMessage());
        printPromotionInfo(receiptDTO);
        System.out.println(RECEIPT_FOOTER.getMessage());
        printPay(receiptDTO);
    }

    private void printProductsInfo(final ReceiptDTO receiptDTO) {
        receiptDTO.getProducts().forEach(p -> {
            int productTotalPrice = p.getProductCount() * p.getProductPrice();
            System.out.printf(
                    RECEIPT_ITEM_FORMAT.getMessage() + ENTER,
                    p.getProductName(),
                    p.getProductCount(),
                    productTotalPrice
            );
        });
    }

    private void printPromotionInfo(final ReceiptDTO receiptDTO) {
        receiptDTO.getPromotionalProducts().forEach(p -> {
            System.out.printf(RECEIPT_GIFT_FORMAT.getMessage() + ENTER, p.getProductName(), p.getProductCount());
        });
    }

    private void printPay(final ReceiptDTO receiptDTO) {
        System.out.printf(
                RECEIPT_TOTAL_FORMAT.getMessage() + ENTER,
                receiptDTO.getTotalCount(),
                receiptDTO.getTotalPrice()
        );
        System.out.printf(RECEIPT_EVENT_DISCOUNT_FORMAT.getMessage() + ENTER, receiptDTO.getPromotionDiscount());
        System.out.printf(RECEIPT_MEMBERSHIP_DISCOUNT_FORMAT.getMessage() + ENTER, receiptDTO.getMembershipDiscount());
        System.out.printf(RECEIPT_PAYMENT_FORMAT.getMessage() + ENTER, receiptDTO.getAmountToPay());
    }
}
