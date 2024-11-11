package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String REQUEST_PRODUCT_NAME_AND_COUNT_MESSAGE =
            "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String REQUEST_RETRY_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String ASK_PURCHASE_AT_REGULAR_PRICE_MESSAGE =
            "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String ASK_ADD_PROMOTION_PRODUCT_MESSAGE =
            "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String ASK_APPLY_MEMBERSHIP_DISCOUNT_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";

    public String requestProductNameAndCount() {
        printBlankLine();
        System.out.println(REQUEST_PRODUCT_NAME_AND_COUNT_MESSAGE);
        return Console.readLine();
    }

    public String requestRetry() {
        printBlankLine();
        System.out.println(REQUEST_RETRY_MESSAGE);
        return Console.readLine();
    }

    public String askPurchaseAtRegularPrice(String productName, int count) {
        printBlankLine();
        System.out.printf(ASK_PURCHASE_AT_REGULAR_PRICE_MESSAGE + "%n", productName, count);
        return Console.readLine();
    }

    public String askAddPromotionProduct(String productName) {
        printBlankLine();
        System.out.printf(ASK_ADD_PROMOTION_PRODUCT_MESSAGE + "%n", productName);
        return Console.readLine();
    }

    public String askApplyMembershipDiscount() {
        printBlankLine();
        System.out.println(ASK_APPLY_MEMBERSHIP_DISCOUNT_MESSAGE);
        return Console.readLine();
    }

    private void printBlankLine() {
        System.out.println();
    }
}
