package store.controller;

import store.dto.ReceiptDTO;
import store.exception.InputDataException;
import store.model.*;
import store.service.*;
import store.view.InputView;
import store.view.OutputView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static store.exception.InputException.*;
import static store.utils.Separator.*;
import static store.utils.Validator.*;


public class ConvenienceStoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final MembershipService membershipService;
    private final ReceiptService receiptService;

    private Inventory inventory;
    private PromotionCatalog promotionCatalog;
    private Cart cart;

    public ConvenienceStoreController(
            InputView inputView,
            OutputView outputView,
            InventoryService inventoryService,
            ProductService productService,
            PromotionService promotionService,
            MembershipService membershipService,
            ReceiptService receiptService
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.membershipService = membershipService;
        this.receiptService = receiptService;
    }

    public void run() {
        loadAllData();
        do {
            greetingAndPrintInventory();
            cart.addAllNormalCart(requestProductNameAndCount());
            applyPromotion();
            int membershipDiscount = applyMembership();
            printResult(membershipDiscount);
        } while (isRetry());
    }

    private void loadAllData() {
        loadInventory();
        loadPromotion();
    }

    private void loadInventory() {
        List<Product> inventoryData = inventoryService.readProductsFile();
        inventory = new Inventory(inventoryData);
    }

    private void loadPromotion() {
        List<Promotion> promotionData = promotionService.readPromotionsFile();
        promotionCatalog = new PromotionCatalog(promotionData);
    }

    private List<Product> requestProductNameAndCount() {
        return handleInputWithRetry(() -> {
            String inputData = inputView.requestProductNameAndCount();
            validateInputFormat(inputData);
            Map<String, Integer> orderItems = parseInputToProducts(inputData);
            productService.validateProducts(orderItems, inventory);
            return productService.addItems(orderItems, inventory);
        });
    }

    private void applyPromotion() {
        List<Product> productsInPromotionPeriod = getProductsInPromotionPeriodAtCart();
        productsInPromotionPeriod.forEach(this::checkExceptionForPromotion);
        addPromotionGiftToCart(productsInPromotionPeriod);
    }

    private int applyMembership() {
        if (requestApplyMembership()) {
            return membershipService.applyMembership(cart);
        }
        return 0;
    }

    private void printResult(int membershipDiscount) {
        Receipt receipt = receiptService.makeReceipt(cart, membershipDiscount);
        ReceiptDTO receiptDTO = receipt.convertToDTO();
        outputView.printReceipt(receiptDTO);
    }

    private boolean requestApplyMembership() {
        return handleInputWithRetry(() -> {
            String inputData = inputView.askApplyMembershipDiscount();
            checkYorNFormat(inputData);
            return inputData.equals("Y");
        });
    }


    private List<Product> getProductsInPromotionPeriodAtCart() {
        return cart.getPromotionProductList().stream()
                .filter(product -> promotionCatalog.isValidPromotion(product.getPromotionName()))
                .toList();
    }

    private void checkExceptionForPromotion(final Product product) {
        // 프로모션 개수 부족 시 (카트에 프로모션 재고와 일반재고가 둘 다 있는 경우)
        if (cart.hasNormalProduct(product.getName())) {
            guidePromotionProductShortage(product);
            return;
        }
        // 프로모션 상품 추가 가능 시
        if (canPromotion(product)) {
            guideAddPromotionProduct(product);
        }
    }

    private void addPromotionGiftToCart(final List<Product> productsInPromotionPeriod) {
        List<Product> freeProducts = createFreeProducts(productsInPromotionPeriod);
        List<Product> productsInPromotion = createProductsInPromotion(productsInPromotionPeriod);
        cart.addAllFreeItems(freeProducts);
        cart.addAllPromotionalItems(productsInPromotion);
    }

    private List<Product> createFreeProducts(final List<Product> productsInPromotionPeriod) {
        List<Product> productsInPromotion = new ArrayList<>();
        productsInPromotionPeriod.forEach(product -> {
            Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
            int freeItemCount = calculateFreeItemCount(product, promotion);
            if (freeItemCount > 0) {
                Product promotionGift = makePromotionGift(product, freeItemCount);
                productsInPromotion.add(promotionGift);
            }
        });
        return productsInPromotion;
    }

    private List<Product> createProductsInPromotion(final List<Product> productsInPromotionPeriod) {
        List<Product> productsInPromotion = new ArrayList<>();
        productsInPromotionPeriod.forEach(product -> {
            Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
            int promotionCount = calculatePromotionCount(product, promotion);
            if (promotionCount > 0) {
                Product promotionGift = makePromotionGift(product, promotionCount);
                productsInPromotion.add(promotionGift);
            }
        });
        return productsInPromotion;
    }

    private int calculateFreeItemCount(final Product product, final Promotion promotion) {
        int buy = promotion.getBuy();
        int get = promotion.getGet();
        return product.getCount() / (buy + get) * get;
    }

    private int calculatePromotionCount(final Product product, final Promotion promotion) {
        int notApplyPromotionCount = product.getCount() % (promotion.getBuy() + promotion.getGet());
        int totalItemCount = product.getCount();
        return totalItemCount - notApplyPromotionCount;
    }

    private Product makePromotionGift(final Product originProduct, int promotionCount) {
        return new Product(
                originProduct.getName(),
                originProduct.getPrice(),
                promotionCount,
                originProduct.getPromotionName()
        );
    }

    private void guideAddPromotionProduct(final Product product) {
        if (isOkAddPromotionProduct(product.getName())) {
            // 상품 추가하기
            inventory.decreaseProductCount(product.getName(), 1, 0);
            cart.increaseProductCountInCart(product.getName(), 1, 0);
        }
    }

    private boolean isOkAddPromotionProduct(final String name) {
        return handleInputWithRetry(() -> {
            String inputData = inputView.askAddPromotionProduct(name);
            checkYorNFormat(inputData);
            return inputData.equals("Y");
        });
    }

    private boolean canPromotion(final Product product) {
        Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
        int totalCount = product.getCount();
        int get = promotion.getGet();
        int buy = promotion.getBuy();
        return buy == totalCount % (get + buy)
                && inventory.checkProductCountFromPromotionProducts(product.getName(), 1);
    }

    private void guidePromotionProductShortage(final Product product) {
        Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
        int notApplyPromotionCount = promotionService.countNotApplyPromotionProduct(promotion, product);
        int normalCount = cart.getNormalProductCount(product.getName());

        if (!isOkPurchaseAtRegularPrice(product.getName(), notApplyPromotionCount + normalCount)) {
            cart.decreaseProductCountInCart(product.getName(), notApplyPromotionCount, normalCount);
            inventory.returnProduct(product.getName(), notApplyPromotionCount, normalCount);
            outputView.printReturnProduct(product.getName(), notApplyPromotionCount + normalCount);
        }
    }

    private boolean isOkPurchaseAtRegularPrice(final String productName, final int count) {
        return handleInputWithRetry(() -> {
            String inputData = inputView.askPurchaseAtRegularPrice(productName, count);
            checkYorNFormat(inputData);
            return inputData.equals("Y");
        });
    }

    private void greetingAndPrintInventory() {
        outputView.printGreetingMessage();
        outputView.printInventory(inventory.convertToDTO().getInventory());
        cart = new Cart();
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

    private void validateInputFormat(final String inputData) {
        if (!validateProductNameAndCountFormat(inputData)) {
            throw new InputDataException(PRODUCT_NAME_AND_COUNT_ERROR);
        }
    }

    private Map<String, Integer> parseInputToProducts(final String inputData) {
        List<String> productNameAndCount = split(inputData);
        return separateProductNameAndCount(productNameAndCount);
    }

    private boolean isRetry() {
        return handleInputWithRetry(() -> {
            String inputData = inputView.requestRetry();
            checkYorNFormat(inputData);
            return inputData.equals("Y");
        });
    }

    private void checkYorNFormat(final String inputData) {
        if (!validateYorN(inputData)) {
            throw new InputDataException(INVALID_INPUT_ERROR);
        }
    }
}
