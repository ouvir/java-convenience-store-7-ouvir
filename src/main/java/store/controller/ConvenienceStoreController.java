package store.controller;

import store.model.Inventory;
import store.model.Product;
import store.service.InventoryService;
import store.view.OutputView;

import java.util.List;

public class ConvenienceStoreController {
    private final OutputView outputView;
    private final InventoryService inventoryService;

    private Inventory inventory;

    public ConvenienceStoreController(OutputView outputView, InventoryService inventoryService) {
        this.outputView = outputView;
        this.inventoryService = inventoryService;
    }

    public void run() {
//      - 1. 환영인사 및 재고 안내 메시지 출력
        List<Product> inventoryData = inventoryService.readProductsFile();
        Inventory inventory = new Inventory(inventoryData);
        outputView.printGreetingMessage();
        outputView.printInventory(inventory.convertToDTO().getInventory());

//        - 2. 구매할 상품과 수량 입력 받기
//        - 3.구매 상품에 대해 프로모션 적용
//        - 4.프로모션 개수 부족 시, 일부 수량에 대해 정가로 결제할지 여부 메시지(Y,N)
//        - 5.프로모션 상품 추가 안내 메시지(Y, N)
//        - 6.멤버십 할인 적용 여부 입력 받기(Y, N)
//        - 7.영수증 출력
//        - 8.재고 업데이트
//        - 9.추가 구매 여부 입력 받기(Y, N)
    }

    private void loadInventory() {

    }
}
