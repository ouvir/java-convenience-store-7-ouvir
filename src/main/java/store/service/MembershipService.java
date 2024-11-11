package store.service;

import store.model.Cart;
import store.model.Membership;

public class MembershipService {

    public int applyMembership(Cart cart) {
        int price = cart.getTotalPrice();
        int appliedPromotionPrice = cart.getPromotionalItemsPrice();

        return Membership.getDiscount(price - appliedPromotionPrice);
    }
}
