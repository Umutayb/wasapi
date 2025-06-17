package exceptions;

/**
 * This class represents an exception that is thrown when a call fails.
 *
 * @author Umut Ay Bora
 * @version 0.0.1 (Documented in 0.0.1, derived from another (Java-Utilities) library)
 */
public class FailedCallException extends RuntimeException {

    /**
     * Constructs a FailedCallException with the specified runtime exception.
     * @param errorMessage The runtime exception to be associated with this exception.
     */
    public FailedCallException(String errorMessage) {super(errorMessage);}

    public FailedCallException(RuntimeException errorMessage) {super(errorMessage);}
}
