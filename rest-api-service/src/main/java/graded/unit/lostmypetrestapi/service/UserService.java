package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link User} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface UserService {

    /**
     * Fetch all members from the database
     *
     * @return List of all {@link User} objects
     */
    List<User> getAllUsers();

    /**
     * Fetch a single member by id
     *
     * @param id This is the id number of the {@link User#id} object to be fetched
     * @return Optional value of {@link User} object
     */
    Optional<User> getUserById(String id);

    /**
     * Fetch a single member by email
     *
     * @param email This is an email address of the {@link User#email} object to be fetched
     * @return Optional value of {@link User} object
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Fetch a single member by confirmation token.
     *
     * @param confirmationToken This is the conformation token o the {@link User#confirmationToken}
     *                          object to be fetched
     * @return Optional value of {@link User} object
     */
    Optional<User> getUserByConfirmationToken(String confirmationToken);

    /**
     * Insert a new member
     *
     * @param member new object of {@link User} to be insert
     * @return HTTP response with {@link User} object
     */
    ResponseEntity<User> addMember(User member);

    /**
     * Insert a new member
     *
     * @param admin new object of {@link User} to be insert
     * @return HTTP response with {@link User} object
     */
    ResponseEntity<User> addAdmin(User admin);

    /**
     * Updated existing member
     *
     * @param member new object of {@link User} to be update
     * @return HTTP response with updated {@link User} object
     */
    ResponseEntity<User> updateMember(User member);

    /**
     * Updated existing member
     *
     * @param admin new object of {@link User} to be update
     * @return HTTP response with updated {@link User} object
     */
    ResponseEntity<User> updateAdmin(User admin);

    /**
     * Delete existing member by id
     *
     * @param id of the {@link User} object to be delete
     * @return HTTP response with confirmation message
     */
    ResponseEntity<CustomResponse> deleteUser(String id);

}
