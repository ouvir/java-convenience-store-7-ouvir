package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.exception.InputDataException;
import store.model.Inventory;
import store.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    ProductService productService = new ProductService();
    Inventory inventory;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("콜라", 1000, 5, "탄산2+1"));
        products.add(new Product("콜라", 1000, 5));
        inventory = new Inventory(products);
    }

    @Test
    @DisplayName("받아온 데이터 검증 테스트 - 상품 존재X")
    void noExistProduct() {
        Map<String, Integer> noExistOrderItems = new HashMap<>();
        noExistOrderItems.put("사이다", 5);
        Exception e = assertThrows(InputDataException.class,
                () -> productService.validateProducts(noExistOrderItems, inventory));
        assertThat(e.getMessage()).contains("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    @Test
    @DisplayName("받아온 데이터 검증 테스트 - 상품 개수 부족")
    void outOfCount() {
        Map<String, Integer> outOfCountOrderItems = new HashMap<>();
        outOfCountOrderItems.put("콜라", 11);
        Exception e = assertThrows(InputDataException.class,
                () -> productService.validateProducts(outOfCountOrderItems, inventory));
        assertThat(e.getMessage()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    @Test
    @DisplayName("장바구니 만들기 테스트")
    void addToCart() {
        // given
        Map<String, Integer> orderItems = new HashMap<>();
        orderItems.put("콜라", 7);
        // when
        List<Product> cart = productService.addItems(orderItems, inventory);
        Map<String, Integer> actual = new HashMap<>();
        for (Product product : cart) {
            actual.put(product.getPromotionName(), product.getCount());
        }
        // then
        Map<String, Integer> expected = Map.of("탄산2+1", 5, "", 2);
        assertThat(actual).isEqualTo(expected);
    }
}