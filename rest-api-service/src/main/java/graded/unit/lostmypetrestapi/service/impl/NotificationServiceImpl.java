package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.notifications.Notification;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.repository.NotificationRepository;
import graded.unit.lostmypetrestapi.service.NotificationService;
import graded.unit.lostmypetrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link NotificationService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final UserService userService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Optional<Notification> getUserNotificationById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Notification> getUserNotificationsByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public ResponseEntity<CustomResponse> addUserNotification(Notification notification) {
        this.repository.save(notification);
        return new ResponseEntity<>(
                new CustomResponse(
                        new Date(),
                        HttpStatus.OK.value(),
                        "Notification for user with ID: " + notification.getUser().getId() + " added."), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponse> deleteUserNotification(Long id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>(
                new CustomResponse(new Date(), HttpStatus.OK.value(),
                        "User notification deleted. (ID: " + id + ")"), HttpStatus.OK);
    }

}
