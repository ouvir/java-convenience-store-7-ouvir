package store.exception;

public class InputDataException extends IllegalArgumentException {
    private final static String PREFIX_ERROR_MESSAGE = "[ERROR] ";

    public InputDataException(InputException exception) {
        super(PREFIX_ERROR_MESSAGE + exception.getErrorMessage());
    }
}