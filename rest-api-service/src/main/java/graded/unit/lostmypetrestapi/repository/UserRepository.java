package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link User} user object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmationToken(String confirmationToken);

}
