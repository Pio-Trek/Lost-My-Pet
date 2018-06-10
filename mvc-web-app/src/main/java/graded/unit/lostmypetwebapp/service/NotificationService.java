package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.NotificationDao;
import graded.unit.lostmypetwebapp.model.notifications.Notification;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link Notification} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class NotificationService {

    private final NotificationDao notificationDao;

    @Autowired
    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    /**
     * Fetch a list of single user notifications.
     *
     * @param id This is an id number of the {@link User} which notifications will be fetched.
     * @return {@link Notification} object.
     */
    public List<Notification> getNotificationsByUserId(String id) {
        return notificationDao.fetchUserNotificationsByUserId(id).getBody();
    }

    /**
     * Delete user notification by id.
     *
     * @param id This is an id number of the {@link Notification} object to be deleted.
     */
    public void deleteNotificationById(Long id) {
        this.notificationDao.removeUserNotification(id);
    }

    /**
     * Count all announcements which are not moderated to be displayed as a notification number.
     *
     * @return Number with not moderated announcements.
     */
    public String showNumberAnnouncementsByEnabledFalse() {
        return notificationDao.countAnnouncementsByEnabledFalse();
    }
}
