package store;

import store.controller.ConvenienceStoreController;
import store.service.*;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        ConvenienceStoreController controller = makeConvenienceStoreController();
        controller.run();
    }

    private static ConvenienceStoreController makeConvenienceStoreController() {
        return new ConvenienceStoreController(
                new InputView(),
                new OutputView(),
                new InventoryService(),
                new ProductService(),
                new PromotionService(),
                new MembershipService(),
                new ReceiptService(),
                new CartService()
        );
    }
}
