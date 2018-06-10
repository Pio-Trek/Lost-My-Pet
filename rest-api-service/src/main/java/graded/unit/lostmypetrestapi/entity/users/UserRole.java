package graded.unit.lostmypetrestapi.entity.users;

import javax.persistence.*;

/**
 * Data model class for User Role object.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Entity(name = "roles")
public class UserRole {

    /**
     * Role id number field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    /**
     * Role name field
     */
    @Column(name = "role")
    private String role;

    // Constructor

    public UserRole() {
    }

    // Getters and setters
    
    public UserRole(String role) {
        this.role = role;
    }

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
