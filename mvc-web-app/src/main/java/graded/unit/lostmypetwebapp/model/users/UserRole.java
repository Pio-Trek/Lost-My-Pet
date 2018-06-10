package graded.unit.lostmypetwebapp.model.users;

/**
 * This is the data model helper class that holds the information about the user role.
 * It is used for load the user, authenticate id and login process.
 */
public class UserRole {

    /**
     * This is an id number of the role.
     */
    private Integer id;

    /**
     * This is the role name.
     */
    private String role;

    // Constructors

    public UserRole() {
    }

    public UserRole(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
