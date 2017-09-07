package io.openchannel.sample.exception;

/**
 * ApplicationOperationException.java : Default application exception
 */

public class ApplicationOperationException extends RuntimeException {
    public ApplicationOperationException() {
        super();
    }

    public ApplicationOperationException(String message) {
        super(message);
    }

    public ApplicationOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationOperationException(Throwable cause) {
        super(cause);
    }

    protected ApplicationOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
