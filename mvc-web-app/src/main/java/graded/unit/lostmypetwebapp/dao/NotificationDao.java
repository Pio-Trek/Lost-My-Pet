package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.notifications.Notification;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link Notification} domain
 * object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface NotificationDao {

    /**
     * Fetch a list of single user notifications.
     *
     * @param id This is an id number of the {@link User} which notifications will be fetched.
     * @return HTTP response with {@link Notification} object
     */
    ResponseEntity<List<Notification>> fetchUserNotificationsByUserId(String id);

    /**
     * Delete user notification by id.
     *
     * @param id This is an id number of the {@link Notification} object to be deleted.
     */
    void removeUserNotification(Long id);

    /**
     * Count all announcements which are not moderated to be displayed as a notification number.
     *
     * @return Number with not moderated announcements.
     */
    String countAnnouncementsByEnabledFalse();

}
