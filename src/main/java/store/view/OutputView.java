package store.view;

import store.dto.ProductDTO;
import store.dto.ReceiptDTO;

import java.util.List;

public class OutputView {
    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INVENTORY_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_INFO_MESSAGE = "- %s %,d원 %s %s";
    private static final String POSTFIX_PRODUCT_COUNT = "개";
    private static final String NO_PRODUCT_COUNT = "재고 없음";
    private static final String RETURN_PRODUCT_MESSAGE = "%s %d개를 제외하고 계산하겠습니다.";

    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String RECEIPT_ITEM_HEADER = "상품명\t\t\t수량\t\t금액";
    private static final String RECEIPT_ITEM_FORMAT = "%s\t\t%d \t%,d"; // 왼쪽 정렬과 자리 지정
    private static final String RECEIPT_GIFT_HEADER = "=============증   정===============";
    private static final String RECEIPT_GIFT_FORMAT = "%s\t\t%d";
    private static final String RECEIPT_FOOTER = "====================================";
    private static final String RECEIPT_TOTAL_FORMAT = "총구매액\t\t%d\t%,d";
    private static final String RECEIPT_EVENT_DISCOUNT_FORMAT = "행사할인\t\t\t-%,d";
    private static final String RECEIPT_MEMBERSHIP_DISCOUNT_FORMAT = "멤버십할인\t\t\t-%,d";
    private static final String RECEIPT_PAYMENT_FORMAT = "내실돈\t\t\t %,d";

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

    public void printErrorMessage(String ErrorMessage) {
        printBlankLine();
        System.out.println(ErrorMessage);
    }

    public void printReturnProduct(String productName, int productCount) {
        printBlankLine();
        System.out.printf(RETURN_PRODUCT_MESSAGE + "%n", productName, productCount);
    }

    public void printReceipt(ReceiptDTO receiptDTO) {
        printBlankLine();
        System.out.println(RECEIPT_HEADER);
        System.out.println(RECEIPT_ITEM_HEADER);
        printProductsInfo(receiptDTO);
        System.out.println(RECEIPT_GIFT_HEADER);
        printPromotionInfo(receiptDTO);
        System.out.println(RECEIPT_FOOTER);
        printPay(receiptDTO);
    }

    private void printProductsInfo(ReceiptDTO receiptDTO) {
        receiptDTO.getProducts().stream().forEach(p -> {
            int productTotalPrice = p.getProductCount() * p.getProductPrice();
            System.out.printf(RECEIPT_ITEM_FORMAT + "%n", p.getProductName(), p.getProductCount(), productTotalPrice);
        });
    }

    private void printPromotionInfo(ReceiptDTO receiptDTO) {
        receiptDTO.getPromotionalProducts().stream().forEach(p -> {
            System.out.printf(RECEIPT_GIFT_FORMAT + "%n", p.getProductName(), p.getProductCount());
        });
    }

    private void printPay(ReceiptDTO receiptDTO) {
        System.out.printf(RECEIPT_TOTAL_FORMAT + "%n", receiptDTO.getTotalCount(), receiptDTO.getTotalPrice());
        System.out.printf(RECEIPT_EVENT_DISCOUNT_FORMAT + "%n", receiptDTO.getPromotionDiscount());
        System.out.printf(RECEIPT_MEMBERSHIP_DISCOUNT_FORMAT + "%n", receiptDTO.getMembershipDiscount());
        System.out.printf(RECEIPT_PAYMENT_FORMAT + "%n", receiptDTO.getAmountToPay());
    }
}
