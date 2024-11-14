package store.service;

import store.model.Cart;
import store.model.Product;
import store.model.Promotion;
import store.model.PromotionCatalog;
import store.utils.Reader;

import java.util.List;

public class PromotionService {
    public List<Promotion> readPromotionsFile() {
        List<List<String>> promotions = Reader.readFile("promotions.md");
        if (promotions == null) {
            return null;
        }
        return promotions.stream().map(Promotion::new).toList();
    }

    public int countNotApplyPromotionProduct(final PromotionCatalog promotionCatalog, final Product product) {
        Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
        return product.getCount() % (promotion.getBuy() + promotion.getGet());
    }

    public List<Product> filterValidPromotion(
            final PromotionCatalog promotionCatalog,
            final List<Product> productsInCart
    ) {
        return productsInCart.stream()
                .filter(product -> promotionCatalog.isValidPromotion(product.getPromotionName()))
                .toList();
    }

    public boolean canPromotionCount(final PromotionCatalog promotionCatalog, final Product product) {
        Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
        int totalCount = product.getCount();
        int get = promotion.getGet();
        int buy = promotion.getBuy();
        return totalCount % (get + buy) == buy;
    }

    public int calculateFreeItemCount(final Promotion promotion, final Product product) {
        int buy = promotion.getBuy();
        int get = promotion.getGet();
        return product.getCount() / (buy + get) * get;
    }

    public int calculatePromotionCount(final Promotion promotion, final Product product) {
        int notApplyPromotionCount = product.getCount() % (promotion.getBuy() + promotion.getGet());
        int totalItemCount = product.getCount();
        return totalItemCount - notApplyPromotionCount;
    }
}
