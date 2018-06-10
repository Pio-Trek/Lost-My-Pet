package graded.unit.lostmypetrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Custom exception message class when an HTTP 4xx is received.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public class ClientException extends HttpClientErrorException {

    /**
     * Custom error message.
     */
    private String errorMessage;

    public ClientException(HttpStatus statusCode) {
        super(statusCode);
    }

}
