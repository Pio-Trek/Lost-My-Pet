package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link User} domain
 * object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface UserDao {

    /**
     * Fetch a list of members.
     *
     * @return List of all {@link User} objects.
     */
    ResponseEntity<List<User>> fetchAllUsers();

    /**
     * Fetch a single member by email.
     *
     * @param email This is an email address of the {@link User} object to be fetched.
     * @return Optional value of {@link User} object.
     */
    ResponseEntity<User> fetchUserByEmail(String email);

    /**
     * Fetch a single member by confirmation token.
     *
     * @param confirmationToken This is the conformation token o the {@link User#confirmationToken}
     *                          object to be fetched.
     * @return Optional value of {@link User} object.
     */
    ResponseEntity<User> findByConfirmationToken(String confirmationToken);

    /**
     * Checks if user email address already exists in the system.
     *
     * @param email This is an email address of the {@link User} object to be checked.
     * @return Boolean value - TRUE if exists or FALSE if not exists.
     */
    ResponseEntity<Boolean> findIsUserExistsWithEmail(String email);

    /**
     * Set a new confirmation token for the user to reset the password.
     *
     * @param email This is an email address of the {@link User} object where confirmation token will be sent.
     */
    void setMemberResetPasswordByEmail(String email);

    /**
     * Insert a new member or update the exists if {@link User#id} parameter is not empty.
     *
     * @param member This is the {@link User} member object that will be saved.
     * @return HTTP response with {@link User} object
     */
    ResponseEntity<User> insertOrUpdateMember(User member);

    /**
     * Insert a new admin or update the exists if {@link User#id} parameter is not empty.
     *
     * @param admin This is the {@link User} admin object that will be saved.
     * @return HTTP response with {@link User} object
     */
    ResponseEntity<User> insertOrUpdateAdmin(User admin);

    /**
     * Delete existing member by id
     *
     * @param id This is an id number of the {@link User} object to be delete.
     */
    void removeUser(String id);

}
