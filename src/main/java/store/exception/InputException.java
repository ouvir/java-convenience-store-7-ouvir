package store.exception;

public enum InputException {
    PRODUCT_NAME_AND_COUNT_ERROR("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    NO_EXIST_PRODUCT_ERROR("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    OUT_OF_COUNT_ERROR("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT_ERROR("잘못된 입력입니다. 다시 입력해 주세요.");
    
    private String errorMessage;

    InputException(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
