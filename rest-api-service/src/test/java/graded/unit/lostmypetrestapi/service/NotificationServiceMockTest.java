package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.notifications.Notification;
import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.entity.posts.Found;
import graded.unit.lostmypetrestapi.entity.posts.Lost;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.repository.NotificationRepository;
import graded.unit.lostmypetrestapi.service.impl.NotificationServiceImpl;
import graded.unit.lostmypetrestapi.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
 * MOCK UNIT TEST FOR USER NOTIFICATION SERVICE LAYER
 */
public class NotificationServiceMockTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private NotificationServiceImpl service;

    private User member = new User("member-id", "password", "member@email.com", "Test", "Member", new Location("West Vinewood"));

    //create Lost Announcement for tests
    private Lost lost = new Lost(new Location("West Vinewood"), member, null, new Date());

    //create Found Announcement for tests
    private Found found = new Found(new Location("West Vinewood"), null, null, new Date());

    private Notification notificationLost = new Notification(member, lost);

    private Notification notificationFound = new Notification(member, found);

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //set IDs for test objects
        notificationLost.setId(1L);
        notificationFound.setId(2L);
    }

    @Test
    public void testGetUserNotificationById() {
        //given
        given(repository.findById(notificationFound.getId())).willReturn(Optional.ofNullable(notificationFound));

        //calling method under the test
        Optional<Notification> optNotification = service.getUserNotificationById(notificationFound.getId());

        //assert
        assertThat(optNotification.isPresent()).isTrue();

        //assert
        assertNotificationFields(optNotification.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findById(notificationFound.getId());
    }

    @Test
    public void testGetNotificationsByUserId() {
        //given
        given(repository.findAllByUserId(member.getId())).willReturn(Arrays.asList(notificationLost, notificationFound));

        //calling method under the test
        List<Notification> allNotifications = service.getUserNotificationsByUserId(member.getId());

        //assert respond has 2 objects
        assertThat(allNotifications).hasSize(2);

        //assert
        assertNotificationFields(allNotifications.get(0)); // notification for lost
        assertNotificationFields(allNotifications.get(1)); // notification for found

        //verify that repository was called
        verify(repository, times(1)).findAllByUserId(member.getId());
    }

    @Test
    public void testAddUserNotification() {
        //given
        given(repository.save(notificationLost)).willReturn(notificationLost);

        //calling method under the test
        ResponseEntity<CustomResponse> response = service.addUserNotification(notificationLost);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //verify that repository was called
        verify(repository, times(1)).save(notificationLost);
    }

    @Test
    public void testDeleteNotification() {
        //calling method under the test
        ResponseEntity<?> response = service.deleteUserNotification(notificationFound.getId());

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //verify that repository was called
        verify(repository, times(1)).deleteById(notificationFound.getId());
    }

    private void assertNotificationFields(Notification notification) {
        assertThat(notification.getId()).isNotNull();
        assertThat(notification.getId()).isInstanceOf(Long.class);
        assertThat(notification.getUser()).isEqualTo(member);
        assertThat(notification.getAnnouncement()).isNotNull();
        assertThat(notification.getAnnouncement()).isInstanceOf(Announcement.class);
    }
}