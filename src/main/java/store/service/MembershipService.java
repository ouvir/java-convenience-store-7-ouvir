package store.service;

import store.model.Cart;
import store.utils.Membership;

public class MembershipService {

    public int applyMembership(final Cart cart) {
        int price = cart.getTotalPrice();
        int appliedPromotionPrice = cart.getPromotionalItemsPrice();

        return Membership.getDiscount(price - appliedPromotionPrice);
    }
}
