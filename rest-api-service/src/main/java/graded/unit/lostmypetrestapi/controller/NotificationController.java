package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.entity.notifications.Notification;
import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomException;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.service.FoundPostService;
import graded.unit.lostmypetrestapi.service.LostPostService;
import graded.unit.lostmypetrestapi.service.NotificationService;
import graded.unit.lostmypetrestapi.service.UserService;
import graded.unit.lostmypetrestapi.validation.UserValidation;
import javassist.NotFoundException;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller layer class for {@link Notification} which exposes resources,
 * CRUD operations and business logic for the {@link NotificationService}
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@RestController
@RequestMapping("/api/users/notifications")
@Api(
        name = "User Notification API",
        description = "Provides a list of methods that manage users' notifications about new announcements.",
        stage = ApiStage.GA)
public class NotificationController {

    // URI address for testing purpose
    static String URI = "/api/users/notifications/";

    private final NotificationService notificationService;
    private final UserValidation userValidation;
    private final UserService userService;
    private final LostPostService lostPostService;
    private final FoundPostService foundPostService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserValidation userValidation, UserService userService, LostPostService lostPostService, FoundPostService foundPostService) {
        this.notificationService = notificationService;
        this.userValidation = userValidation;
        this.userService = userService;
        this.lostPostService = lostPostService;
        this.foundPostService = foundPostService;
    }

    /**
     * HTTP GET request method.
     * Fetch all user notifications by user id number from the database.
     *
     * @param id This is an id number of {@link User} object
     * @return List of all {@link Notification} announcement objects.
     * @throws CustomException When {@link User#id} is not valid.
     */
    @GetMapping(path = "/user/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all user notifications by user id number.")
    public List<Notification> fetchUserNotificationsByUserId(@ApiPathParam(description = "The id of the user") @PathVariable("id") String id) throws CustomException {
        userValidation.validateById(id);
        return notificationService.getUserNotificationsByUserId(id);
    }

    /**
     * HTTP GET request method.
     * Count all announcements which are not moderated.
     *
     * @return A number of all announcment where {@link Announcement#enabled} is false.
     */
    @GetMapping(path = "/count/enabledFalse",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Count all announcements which are not moderated.")
    public long countAnnouncementsByEnabledFalse() {
        long countLost = lostPostService.countByEnabledFalse();
        long countFound = foundPostService.countByEnabledFalse();
        return countLost + countFound;
    }

    /**
     * HTTP POST request method.
     * Save a new user notification to the database.
     *
     * @param announcement This is the {@link Announcement} object for which notifications will be created.
     * @return HTTP custom response status code and/or message.
     */
    @PostMapping(path = "/member",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new user notification.")
    public ResponseEntity<CustomResponse> insertNotification(Announcement announcement) {
        // Get announcement location id
        Integer announcementLocationId = announcement.getLocation().getId();

        // Find all members with the same location id as announcement
        List<User> members = userService.getAllUsers()
                .stream()
                .filter(member -> member.getLocation() != null) // only members with location
                .filter(member -> !member.getId().equals(announcement.getMember().getId()))
                .filter(member -> member.getLocation().getId().equals(announcementLocationId))
                .collect(Collectors.toList());

        if (!members.isEmpty()) {

            // Add notification to all members with announcement location id and save to database
            for (User member : members) {
                Notification notification = new Notification(member, announcement);
                notificationService.addUserNotification(notification);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(
                new CustomResponse(new Date(), HttpStatus.NO_CONTENT.value(),
                        "No member meets the requirement"), HttpStatus.OK);
    }

    /**
     * HTTP DELETE request method.
     * Delete from the database a single user notification.
     *
     * @param id This is the id number of the {@link Notification} object to be delete.
     * @return HTTP response with confirmation message.
     * @throws NotFoundException when {@link Notification#id} is not valid.
     */
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Delete a single user notification.")
    public ResponseEntity<CustomResponse> removeUserNotification(@ApiPathParam(description = "The id of the notification") @PathVariable("id") Long id) throws NotFoundException {
        validateUserNotificationId(id);
        return notificationService.deleteUserNotification(id);
    }

    /**
     * Method responsible for check if ID number exists in the database.
     *
     * @param id This is the id number of the {@link Notification} announcement to be validate.
     * @throws NotFoundException When {@link Notification#id} number is not found.
     */
    private void validateUserNotificationId(Long id) throws NotFoundException {
        notificationService.getUserNotificationById(id)
                .orElseThrow(() -> new NotFoundException
                        ("User notification with ID: '" + id + "' not found."));
    }
}
