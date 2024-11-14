package store.model;

import store.utils.TimeUtils;

import java.util.List;

public class PromotionCatalog {
    private final List<Promotion> promotions;

    public PromotionCatalog(final List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public boolean isValidPromotion(final String promotionName) {
        return promotions.stream()
                .anyMatch(promotion ->
                        promotion.getName().equals(promotionName) &&
                                promotion.isValidDate(TimeUtils.getNowLocalDate())
                );
    }

    public Promotion getPromotion(final String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findFirst()
                .orElse(null);
    }
}
