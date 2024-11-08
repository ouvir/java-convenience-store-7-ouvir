package store;

import store.controller.ConvenienceStoreController;
import store.service.InventoryService;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        InventoryService inventoryService = new InventoryService();

        ConvenienceStoreController controller = new ConvenienceStoreController(outputView, inventoryService);
        controller.run();
    }
}
