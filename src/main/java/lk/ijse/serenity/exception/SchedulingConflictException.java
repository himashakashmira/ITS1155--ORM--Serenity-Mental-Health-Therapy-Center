package lk.ijse.serenity.exception;

public class SchedulingConflictException extends ServiceException {
    public SchedulingConflictException(String message) {
        super(message);
    }

    public SchedulingConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
