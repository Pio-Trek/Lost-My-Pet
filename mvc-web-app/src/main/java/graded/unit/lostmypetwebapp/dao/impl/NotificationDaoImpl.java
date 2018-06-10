package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.NotificationDao;
import graded.unit.lostmypetwebapp.model.notifications.Notification;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link NotificationDao} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Repository
public class NotificationDaoImpl implements NotificationDao {

    private final String NOTIFICATION_SERVICE_URL = "/users/notifications/";

    private final WebClient client;

    public NotificationDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<List<Notification>> fetchUserNotificationsByUserId(String id) {
        return client.getRestTemplate()
                .exchange(
                        NOTIFICATION_SERVICE_URL + "user/" + id,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Notification>>() {
                        });
    }

    @Override
    public void removeUserNotification(Long id) {
        client.getRestTemplate()
                .exchange(NOTIFICATION_SERVICE_URL + id, HttpMethod.DELETE, null, Notification.class);
    }

    @Override
    public String countAnnouncementsByEnabledFalse() {
        return client.getRestTemplate()
                .getForEntity(NOTIFICATION_SERVICE_URL + "/count/enabledFalse", String.class).getBody();
    }
}
