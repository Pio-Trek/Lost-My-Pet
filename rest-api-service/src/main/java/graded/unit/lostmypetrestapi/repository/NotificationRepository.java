package graded.unit.lostmypetrestapi.repository;

import graded.unit.lostmypetrestapi.entity.notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository that provides data access layer and query
 * methods for {@link Notification} user object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserId(String userId);

}
