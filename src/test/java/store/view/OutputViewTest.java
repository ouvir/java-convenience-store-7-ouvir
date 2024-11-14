package store.view;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OutputViewTest extends NsTest {
    OutputView outputView = new OutputView();

    @Test
    @DisplayName("재고 출력")
    void printInventory() {
        List<ProductDTO> inventorys = new ArrayList<>();
        ProductDTO product = new ProductDTO("콜라", 1000, 10, "탄산2+1");
        ProductDTO product2 = new ProductDTO("사이다", 1000, 7, "");
        ProductDTO product3 = new ProductDTO("오렌지주스", 1300, 0, "");
        inventorys.add(product);
        inventorys.add(product2);
        inventorys.add(product3);
        outputView.printInventory(inventorys);
        assertThat(output()).contains("현재 보유하고 있는 상품입니다.",
                "- 콜라 1,000원 10개 탄산2+1",
                "- 사이다 1,000원 7개",
                "- 오렌지주스 1,300원 재고 없음"
                );
    }


    @Test
    @DisplayName("영수증 출력")
    void printReceipt() {
    }


    @Override
    protected void runMain() {

    }
}