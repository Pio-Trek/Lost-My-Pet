package graded.unit.lostmypetwebapp.model.users;

import javax.validation.constraints.Size;

/**
 * This is the data model helper class that holds the information about the new password.
 * This model is used in the update password form, when member wants to change the password.
 */
public class PasswordUpdate {

    /**
     * New member password.
     */
    @Size(min = 6, max = 30)
    private String newPassword;

    // Constructors

    public PasswordUpdate() {
    }

    // Getters and setters

    public PasswordUpdate(@Size(min = 6, max = 30) String newPassword) {
        this.newPassword = newPassword;
    }


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
