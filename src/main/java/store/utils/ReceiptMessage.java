package store.utils;

public enum ReceiptMessage {
    RECEIPT_HEADER("==============W 편의점================"),
    RECEIPT_ITEM_HEADER("상품명\t\t\t수량\t\t금액"),
    RECEIPT_ITEM_FORMAT("%s\t\t%d \t%,d"),
    RECEIPT_GIFT_HEADER("=============증   정==============="),
    RECEIPT_GIFT_FORMAT("%s\t\t%d"),
    RECEIPT_FOOTER("===================================="),
    RECEIPT_TOTAL_FORMAT("총구매액\t\t%d\t%,d"),
    RECEIPT_EVENT_DISCOUNT_FORMAT("행사할인\t\t\t-%,d"),
    RECEIPT_MEMBERSHIP_DISCOUNT_FORMAT("멤버십할인\t\t\t-%,d"),
    RECEIPT_PAYMENT_FORMAT("내실돈\t\t\t %,d");

    private final String message;

    ReceiptMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
