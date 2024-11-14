package store.service;

import store.model.Cart;
import store.model.Receipt;

public class ReceiptService {

    public Receipt makeReceipt(final Cart cart, final int membershipDiscount) {
        return new Receipt(cart, membershipDiscount);
    }
}
