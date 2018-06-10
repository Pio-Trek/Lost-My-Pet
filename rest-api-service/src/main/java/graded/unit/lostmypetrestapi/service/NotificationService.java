package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.notifications.Notification;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link Notification} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface NotificationService {

    /**
     * Fetch a single notification by id
     *
     * @param id of the {@link Notification} object to be fetched
     * @return Optional value of {@link Notification} object
     */
    Optional<Notification> getUserNotificationById(Long id);

    /**
     * Fetch all user notifications by user id
     *
     * @param userId of the {@link User} object that is associated with {@link Notification} objects to be fetched
     * @return List of all user {@link Notification} objects
     */
    List<Notification> getUserNotificationsByUserId(String userId);

    /**
     * Insert notification about new lost announcement to all members with
     * the same location as announcement
     *
     * @param notification new object of {@link Notification} that is posting by a user to be insert
     */
    ResponseEntity<CustomResponse> addUserNotification(Notification notification);

    /**
     * Delete existing user notification
     *
     * @param id of the {@link Notification} object to be delete
     * @return HTTP response with confirmation message
     */
    ResponseEntity<CustomResponse> deleteUserNotification(Long id);
}
