package store.controller;

import store.exception.InputDataException;
import store.model.Inventory;
import store.model.Product;
import store.service.InventoryService;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static store.exception.InputException.PRODUCT_NAME_AND_COUNT_ERROR;
import static store.utils.Separator.*;
import static store.utils.Validator.*;


public class ConvenienceStoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InventoryService inventoryService;
    private final ProductService productService;

    private Inventory inventory;

    public ConvenienceStoreController(
            InputView inputView,
            OutputView outputView,
            InventoryService inventoryService,
            ProductService productService
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    public void run() {
//      - 1. 환영인사 및 재고 안내 메시지 출력
        List<Product> inventoryData = inventoryService.readProductsFile();
        inventory = new Inventory(inventoryData);
        outputView.printGreetingMessage();
        outputView.printInventory(inventory.convertToDTO().getInventory());

//        - 2. 구매할 상품과 수량 입력 받기 [콜라-10],[사이다-3]
        List<Product> cart = requestProductNameAndCount();

//        - 3.구매 상품에 대해 프로모션 적용
//        - 4.프로모션 개수 부족 시, 일부 수량에 대해 정가로 결제할지 여부 메시지(Y,N)
//        - 5.프로모션 상품 추가 안내 메시지(Y, N)
//        - 6.멤버십 할인 적용 여부 입력 받기(Y, N)
//        - 7.영수증 출력
//        - 8.재고 업데이트
//        - 9.추가 구매 여부 입력 받기(Y, N)
    }

    private <T> T handleInputWithRetry(final Supplier<T> action) {
        while (true) {
            try {
                return action.get();
            } catch (InputDataException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private List<Product> requestProductNameAndCount() {
        return handleInputWithRetry(() -> {
            String inputData = inputView.requestProductNameAndCount();
            validateInputFormat(inputData);
            Map<String, Integer> orderItems = parseInputToProducts(inputData);
            productService.validateProducts(orderItems, inventory);
            return productService.addToCart(orderItems, inventory);
        });
    }

    private void validateInputFormat(String inputData) {
        if (!validateProductNameAndCountFormat(inputData)) {
            throw new InputDataException(PRODUCT_NAME_AND_COUNT_ERROR);
        }
    }

    private Map<String, Integer> parseInputToProducts(final String inputData) {
        List<String> productNameAndCount = split(inputData);
        return separateProductNameAndCount(productNameAndCount);
    }

}
