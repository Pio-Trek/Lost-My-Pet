package graded.unit.lostmypetrestapi.entity.users;

/**
 * Enumeration class with collection of roles available in the system and id numbers
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public enum Role {

    ROLE_USER(1),
    ROLE_ADMIN(2);

    private int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
