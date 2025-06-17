package exceptions;

/**
 * This class represents an exception that is thrown when an error occurs in JavaUtilities.
 *
 * @author Umut Ay Bora
 * @version 0.0.1 (Documented in 0.0.1, derived from another (Java-Utilities) library)
 */
public class WasapiException extends RuntimeException {

    /**
     * Constructs a JavaUtilitiesException with the specified error message.
     * @param errorMessage The error message to be associated with this exception.
     */
    public WasapiException(String errorMessage) {super(errorMessage);}

    public WasapiException(RuntimeException errorMessage) {super(errorMessage);}
}
