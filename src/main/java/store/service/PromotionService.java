package store.service;

import store.model.Cart;
import store.model.Product;
import store.model.Promotion;
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

    public int countNotApplyPromotionProduct(Promotion promotion, Product product) {
        // 프로모션 적용 불가 상품 개수 = 담은 프로모션 상품 개수 % (buy + get) + 일반 상품 개수
        return product.getCount() % (promotion.getBuy() + promotion.getGet());
    }


}
