package graded.unit.lostmypetrestapi.aspect;

import graded.unit.lostmypetrestapi.controller.NotificationController;
import graded.unit.lostmypetrestapi.entity.posts.Announcement;
import graded.unit.lostmypetrestapi.entity.posts.Found;
import graded.unit.lostmypetrestapi.entity.posts.Lost;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Aspect class that implement {@link NotificationController#insertNotification(Announcement)}
 * method to be injected after adding a new lost/found announcement is saved to a database.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Aspect
@Component //To be detected as Spring bean
public class NotificationAspect {

    private final NotificationController notificationController;

    @Autowired
    public NotificationAspect(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    /**
     * Run after the method returned a result and inject add user notification method.
     *
     * @param response Intercept the returned ResponseEntity {@link Lost} object.
     */
    @AfterReturning(
            pointcut = "execution(* setLostPostEnabled(..))",
            returning = "response")
    public void afterAddLostPost(ResponseEntity<Lost> response) {
        Lost lostPost = response.getBody();
        notificationController.insertNotification(lostPost);
    }

    /**
     * Run after the method returned a result and inject add user notification method.
     *
     * @param response Intercept the returned ResponseEntity {@link Found} object.
     */
    @AfterReturning(
            pointcut = "execution(* setFoundPostEnabled(..))",
            returning = "response")
    public void afterAddFoundPost(ResponseEntity<Found> response) {
        Found foundPost = response.getBody();
        notificationController.insertNotification(foundPost);
    }
}
