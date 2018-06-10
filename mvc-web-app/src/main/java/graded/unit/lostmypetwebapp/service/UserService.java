package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.UserDao;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link User} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Fetch a list of members.
     *
     * @return List of all {@link User} objects.
     */
    public List<User> getAllUsers() {
        return userDao.fetchAllUsers().getBody();
    }

    /**
     * Fetch a single member by email.
     *
     * @param email This is an email address of the {@link User} object to be fetched.
     * @return Optional value of {@link User} object.
     */
    public User getUserByEmail(String email) {
        return userDao.fetchUserByEmail(email).getBody();
    }

    /**
     * Checks if user email address already exists in the system.
     *
     * @param email This is an email address of the {@link User} object to be checked.
     * @return Boolean value - TRUE if exists or FALSE if not exists.
     */
    public boolean existsWithEmail(String email) {
        return userDao.findIsUserExistsWithEmail(email).getBody();
    }

    /**
     * Fetch a single member by confirmation token.
     *
     * @param confirmationToken This is the conformation token o the {@link User#confirmationToken}
     *                          object to be fetched.
     * @return Optional value of {@link User} object.
     */
    public User getUserByConfirmationToken(String confirmationToken) {
        return userDao.findByConfirmationToken(confirmationToken).getBody();
    }

    /**
     * Set a new confirmation token for the user to reset the password.
     *
     * @param email This is an email address of the {@link User} object where confirmation token will be sent.
     */
    public void setMemberResetPasswordByEmail(String email) {
        this.userDao.setMemberResetPasswordByEmail(email);
    }

    /**
     * Insert a new member or update the exists if {@link User#id} parameter is not empty.
     *
     * @param member This is the {@link User} member object that will be saved.
     * @return HTTP response with {@link User} object
     */
    public User saveOrUpdateMember(User member) {
        return userDao.insertOrUpdateMember(member).getBody();
    }

    /**
     * Insert a new admin or update the exists if {@link User#id} parameter is not empty.
     *
     * @param admin This is the {@link User} admin object that will be saved.
     * @return HTTP response with {@link User} object
     */
    public User saveOrUpdateAdmin(User admin) {
        return userDao.insertOrUpdateAdmin(admin).getBody();
    }

    /**
     * Delete existing member by id
     *
     * @param id This is an id number of the {@link User} object to be delete.
     */
    public void deleteUser(String id) {
        this.userDao.removeUser(id);
    }

    /**
     * This method will get current logged in username (email address).
     *
     * @return {@link User} object of the current logged in user.
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userDao.fetchUserByEmail(email).getBody();
        }

        return null;
    }
}
