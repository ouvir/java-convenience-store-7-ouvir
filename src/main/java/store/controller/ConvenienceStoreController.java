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
    private static final String YES = "Y";

    private final InputView inputView;
    private final OutputView outputView;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final MembershipService membershipService;
    private final ReceiptService receiptService;
    private final CartService cartService;

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
            ReceiptService receiptService, CartService cartService
    ) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.membershipService = membershipService;
        this.receiptService = receiptService;
        this.cartService = cartService;
    }

    public void run() {
        loadData();
        do {
            cart = new Cart();
            greetingAndPrintInventory();
            addWishListToCart();
            applyPromotion();
            int membershipDiscount = applyMembership();
            printResult(membershipDiscount);
        } while (isRetry());
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

    private void loadData() {
        List<Product> inventoryData = inventoryService.readProductsFile();
        inventory = new Inventory(inventoryData);

        List<Promotion> promotionData = promotionService.readPromotionsFile();
        promotionCatalog = new PromotionCatalog(promotionData);
    }

    private void greetingAndPrintInventory() {
        outputView.printGreetingMessage();
        outputView.printInventory(inventory.convertToDTO().getInventory());
    }

    private void addWishListToCart() {
        handleInputWithRetry(() -> {
            String inputData = inputView.requestProductNameAndCount();
            checkInputFormat(inputData);
            Map<String, Integer> orderItems = parseInputToProducts(inputData);
            List<Product> products = productService.validateProducts(orderItems, inventory);
            cart.addAllNormalCart(products);
            return null;
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

    private boolean isRetry() {
        return handleInputWithRetry(() -> {
            String inputData = inputView.requestRetry();
            checkYorNFormat(inputData);
            return inputData.equals(YES);
        });
    }

    // --------------------------------------------------------------------------------------------

    private void checkInputFormat(final String inputData) {
        if (!validateProductNameAndCountFormat(inputData)) {
            throw new InputDataException(PRODUCT_NAME_AND_COUNT_ERROR);
        }
    }

    private void checkYorNFormat(final String inputData) {
        if (!validateYorN(inputData)) {
            throw new InputDataException(INVALID_INPUT_ERROR);
        }
    }

    private Map<String, Integer> parseInputToProducts(final String inputData) {
        List<String> productNameAndCount = split(inputData);
        return separateProductNameAndCount(productNameAndCount);
    }

    private List<Product> getProductsInPromotionPeriodAtCart() {
        List<Product> productsInCart = cartService.getProductsAtCart(cart);
        return promotionService.filterValidPromotion(promotionCatalog, productsInCart);
    }

    private boolean requestApplyMembership() {
        return handleInputWithRetry(() -> {
            String inputData = inputView.askApplyMembershipDiscount();
            checkYorNFormat(inputData);
            return inputData.equals(YES);
        });
    }

    private void checkExceptionForPromotion(final Product product) {
        // 프로모션 개수 부족 시 (카트에 프로모션 재고와 일반재고가 둘 다 있는 경우)
        if (isLackPromotionCount(product)) {
            guidePromotionProductShortage(product);
            return;
        }
        // 프로모션 상품 추가 가능 시 (프로모션 가능 개수 && 프로모션 재고 남음)
        if (canPromotion(product)) {
            guideAddPromotionProduct(product);
        }
    }

    private boolean isLackPromotionCount(final Product product) {
        return cartService.hasNormalProduct(cart, product.getName());
    }

    private void guidePromotionProductShortage(final Product product) {
        // 프로모션 적용 불가 상품 개수 = 담은 프로모션 상품 개수 % (buy + get) + 일반 상품 개수
        int notApplyPromotionCount = promotionService.countNotApplyPromotionProduct(promotionCatalog, product);
        int normalCount = cartService.getNormalProductCount(cart, product.getName());
        if (!isOkPurchaseAtRegularPrice(product.getName(), notApplyPromotionCount + normalCount)) {
            cartService.takeOutFewProductInCart(cart, product.getName(), notApplyPromotionCount, normalCount);
            inventoryService.returnProduct(inventory, product.getName(), notApplyPromotionCount, normalCount);
            outputView.printReturnProduct(product.getName(), notApplyPromotionCount + normalCount);
        }
    }

    private boolean isOkPurchaseAtRegularPrice(final String productName, final int count) {
        return handleInputWithRetry(() -> {
            String inputData = inputView.askPurchaseAtRegularPrice(productName, count);
            checkYorNFormat(inputData);
            return inputData.equals(YES);
        });
    }

    private boolean canPromotion(final Product product) {
        return promotionService.canPromotionCount(promotionCatalog, product)
                && inventoryService.checkProductCountFromPromotionProducts(inventory, product.getName());
    }

    private void guideAddPromotionProduct(final Product product) {
        if (isOkAddPromotionProduct(product.getName())) {
            inventoryService.takeOutFewProductInInventory(inventory, product.getName(), 1, 0);
            cartService.addProductInCart(cart, product.getName(), 1, 0);
        }
    }

    private boolean isOkAddPromotionProduct(final String name) {
        return handleInputWithRetry(() -> {
            String inputData = inputView.askAddPromotionProduct(name);
            checkYorNFormat(inputData);
            return inputData.equals(YES);
        });
    }

    private void addPromotionGiftToCart(final List<Product> productsInPromotionPeriod) {
        // 증정 상품 목록 구하기
        List<Product> freeProducts = createFreeProducts(productsInPromotionPeriod);
        // 프로모션 적용된 상품 목록 구하기
        List<Product> productsInPromotion = createProductsInPromotion(productsInPromotionPeriod);
        // 카트에 정보 업데이트
        cartService.updatePromotionItems(cart, freeProducts, productsInPromotion);
    }

    private List<Product> createFreeProducts(final List<Product> productsInPromotionPeriod) {
        List<Product> productsInPromotion = new ArrayList<>();
        productsInPromotionPeriod.forEach(product -> {
            Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
            int freeItemCount = promotionService.calculateFreeItemCount(promotion, product);
            if (freeItemCount > 0) {
                productsInPromotion.add(productService.makePromotionGift(product, freeItemCount));
            }
        });
        return productsInPromotion;
    }

    private List<Product> createProductsInPromotion(final List<Product> productsInPromotionPeriod) {
        List<Product> productsInPromotion = new ArrayList<>();
        productsInPromotionPeriod.forEach(product -> {
            Promotion promotion = promotionCatalog.getPromotion(product.getPromotionName());
            int promotionCount = promotionService.calculatePromotionCount(promotion, product);
            if (promotionCount > 0) {
                productsInPromotion.add(productService.makePromotionGift(product, promotionCount));
            }
        });
        return productsInPromotion;
    }
}
