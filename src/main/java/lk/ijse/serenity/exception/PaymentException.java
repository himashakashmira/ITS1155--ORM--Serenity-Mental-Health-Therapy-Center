package lk.ijse.serenity.exception;

public class PaymentException extends ServiceException {
    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
