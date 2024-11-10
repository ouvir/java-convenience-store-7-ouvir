package store;

import store.controller.ConvenienceStoreController;
import store.service.InventoryService;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        InventoryService inventoryService = new InventoryService();
        ProductService productService = new ProductService();

        ConvenienceStoreController controller = new ConvenienceStoreController(
                inputView,
                outputView,
                inventoryService,
                productService
        );

        controller.run();
    }
}
