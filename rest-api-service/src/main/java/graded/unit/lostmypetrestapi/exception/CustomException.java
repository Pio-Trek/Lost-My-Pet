package graded.unit.lostmypetrestapi.exception;

/**
 * Custom exception message class when initialized by a call to {@link Throwable}.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public class CustomException extends Exception {

    /**
     * Custom error message.
     */
    private String errorMessage;

    public CustomException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}