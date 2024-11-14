package store.service;

import store.model.Cart;
import store.model.Product;

import java.util.List;

public class CartService {

    public List<Product> getProductsAtCart(final Cart cart) {
        return cart.getTotalItems()
                .stream()
                .filter(Product::isPromotion)
                .toList();
    }

    public boolean hasNormalProduct(final Cart cart, final String name) {
        return cart.hasNormalProduct(name);
    }

    public int getNormalProductCount(final Cart cart, final String name) {
        return cart.getNormalProductCount(name);
    }

    public void takeOutFewProductInCart(
            final Cart cart,
            final String name,
            final int notApplyPromotionCount,
            final int normalCount
    ) {
        cart.decreaseProductCountInCart(name, notApplyPromotionCount, normalCount);
    }

    public void addProductInCart(final Cart cart, final String name, final int promotion, final int normal) {
        cart.increaseProductCountInCart(name, promotion, normal);
    }

    public void updatePromotionItems(
            final Cart cart,
            final List<Product> freeProducts,
            final List<Product> productsInPromotion
    ) {
        cart.addAllFreeItems(freeProducts);
        cart.addAllPromotionalItems(productsInPromotion);
    }
}
