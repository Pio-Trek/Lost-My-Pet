package graded.unit.lostmypetwebapp.model.users;

/**
 * Enumeration class with collection of roles available in the system and id numbers
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
