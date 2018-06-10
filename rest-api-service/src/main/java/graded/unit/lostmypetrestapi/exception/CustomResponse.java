package graded.unit.lostmypetrestapi.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Response class for sending errors to use when exceptions are thrown from API.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public class CustomResponse {

    /**
     * Date and time when an exception occurs; the date is formatted in the constructor.
     */
    private String date;

    /**
     * HTTP error status code.
     */
    private int status;

    /**
     * Exception error message.
     */
    private String message;


    // Constructors

    public CustomResponse(Date date, int status, String message) {
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        this.status = status;
        this.message = message;
    }


    // Getters and setters

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


}