package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.service.InventoryService;
import store.view.OutputView;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    private static final OutputView outputView = new OutputView();

    @Test
    @DisplayName("1.재고 안내 메시지 출력")
    void 파일에_있는_상품_목록_출력() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    @DisplayName("0. 환영인사 출력")
    void greeting() {
        outputView.printGreetingMessage();
        assertThat(output()).contains("안녕하세요. W편의점입니다.");
    }

    @Test
    @DisplayName("3.구매 상품에 대해 프로모션 적용")
    void promotion() {
        assertSimpleTest(() -> {
            run("[콜라-9]", "N", "N");
            assertThat(output()).contains(
                    "==============W 편의점================",
                    "상품명\t\t\t수량\t\t금액",
                    "콜라\t\t9 \t9,000",
                    "=============증   정===============",
                    "콜라\t\t3",
                    "====================================",
                    "총구매액\t\t9\t9,000",
                    "행사할인\t\t\t-3,000",
                    "멤버십할인\t\t\t-0",
                    "내실돈\t\t\t 6,000"
            );
        });
    }

    @Test
    @DisplayName("3.구매 상품에 대해 프로모션 적용 예외 - 재고 부족")
    void lackPromotionStock() {
        assertSimpleTest(() -> {
            run("[콜라-12]", "N", "N", "N");
            assertThat(output()).contains(
                    "현재 콜라 3개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",
                    "콜라 3개를 제외하고 계산하겠습니다."
            );
        });
    }

    @Test
    @DisplayName("3.구매 상품에 대해 프로모션 적용 예외 - 프로모션 상품 추가 가능")
    void canAddPromotionStock() {
        assertSimpleTest(() -> {
            run("[콜라-5]", "Y", "N", "N");
            assertThat(output()).contains(
                    "현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)",
                    "콜라\t\t6 \t6,000"
            );
        });
    }



    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
