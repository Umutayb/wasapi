package collections;

/**
 * A record that represents a response and its errorBody.
 *
 * @param <Response> expected Response-SuccessModel-
 * @param <ErrorBody> potential error body
 *
 * @author Umut Ay Bora
 * @version 0.0.1 (Documented in 0.0.1, derived from another (Java-Utilities) library)
 */
public record ResponsePair<Response, ErrorBody>(Response response, ErrorBody errorBody) { }
