package graded.unit.lostmypetwebapp.model.users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * This is the data model helper class that holds the information about the email address on which
 * the reset password confirmation token will be send. It is used in the reset password form view.
 */
public class PasswordReset {

    /**
     * This is the user email address for password reset.
     */
    @Email(message = "Please provide a valid e-mail")
    @NotBlank(message = "Please provide an e-mail")
    private String email;

    // Constructors

    public PasswordReset() {
    }

    // Getters and setters

    public PasswordReset(@Email(message = "Please provide a valid e-mail") @NotBlank(message = "Please provide an e-mail") String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
