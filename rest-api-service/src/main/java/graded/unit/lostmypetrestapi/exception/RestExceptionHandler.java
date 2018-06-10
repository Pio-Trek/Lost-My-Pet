package graded.unit.lostmypetrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

/**
 * Custom Error Message class for REST API. Handling all logic
 * and validation errors in Spring with use of {@link ControllerAdvice} annotation.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Custom 404 response to throw when [OBJECT] request is not found.
     *
     * @param ex This is the new {@link Exception} object passed as an argument with its detail message.
     * @return HTTP custom response with status code and message.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse> handleNotFoundException(Exception ex) {
        CustomResponse error = new CustomResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Custom 409 response to throw when user's email address is already reserved in the database.
     *
     * @return HTTP custom response with status code and message.
     */
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<CustomResponse> handleNotUniqueEmailException() {
        CustomResponse error = new CustomResponse(
                new Date(),
                HttpStatus.CONFLICT.value(),
                "This email address is already reserved, specify a different one");

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Custom 400 bad request response to throw when the part of request
     * is not found, missing parameter or when a client sent an invalid request.
     *
     * @return HTTP custom response with status code and message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleBadRequestException() {
        CustomResponse error = new CustomResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "Your request has issued a malformed or illegal request");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}