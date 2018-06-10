package graded.unit.lostmypetwebapp.controller;

import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.messages.Conversation;
import graded.unit.lostmypetwebapp.model.messages.Message;
import graded.unit.lostmypetwebapp.model.notifications.Notification;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.model.users.PasswordReset;
import graded.unit.lostmypetwebapp.model.users.User;
import graded.unit.lostmypetwebapp.service.*;
import org.apache.logging.log4j.util.Strings;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * This controller is mapping all user's account HTTP requests.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final LocationService locationService;
    private final LostPostService lostPostService;
    private final FoundPostService foundPostService;
    private final NotificationService notificationService;
    private final MessageService messageService;

    private User currentUser;
    private List<Notification> notifications;

    @Autowired
    public AccountController(BCryptPasswordEncoder passwordEncoder, UserService userService, LocationService locationService, LostPostService lostPostService, FoundPostService foundPostService, NotificationService notificationService, MessageService messageService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.locationService = locationService;
        this.lostPostService = lostPostService;
        this.foundPostService = foundPostService;
        this.notificationService = notificationService;
        this.messageService = messageService;
    }

    /**
     * Display member's dashboard main page which contains account details, messages, notification
     * and the list of lost and/or found pets.
     *
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @param model   This model is to supply attributes used for rendering view.
     * @return View with dashboard page.
     */
    @GetMapping("/dashboard")
    public String showDashboardPage(@ModelAttribute("message") String message, Model model) {
        User currentUser = userService.getCurrentUser();

        List<Lost> lostPostsCurrentMember = lostPostService
                .getAllLostPostsByMemberId(currentUser.getId());

        List<Found> foundPostsCurrentMember = foundPostService.getAllFoundPostsByMemberId(currentUser.getId());

        notifications = notificationService.getNotificationsByUserId(currentUser.getId());

        List<Message> allMessagesBySenderId = messageService.getAllMessagesBySenderId(currentUser.getId());
        List<Message> allMessagesByRecipientId = messageService.getAllMessagesByRecipientId(currentUser.getId());

        int numberOfMessages = allMessagesBySenderId.size() + allMessagesByRecipientId.size();

        int conversationsCount = 0;
        for (Message m : allMessagesBySenderId) {
            conversationsCount += messageService.numberOfConversationsByMessageId(m.getId());
        }

        for (Message m : allMessagesByRecipientId) {
            conversationsCount += messageService.numberOfConversationsByMessageId(m.getId());
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("lostList", lostPostsCurrentMember);
        model.addAttribute("foundList", foundPostsCurrentMember);
        model.addAttribute("messagesCount", numberOfMessages);
        model.addAttribute("conversationsCount", conversationsCount);
        model.addAttribute("notificationCount", notifications.size());
        model.addAttribute("headerMessage", "Your account settings");
        model.addAttribute("message", message);
        return "account/dashboard";
    }

    /**
     * Triggle when the member click on the button to delete the account.
     * This method will perform the operations with deleting current logged in member.
     *
     * @param id                 This is an id number of the current {@link User} to be deleted.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the home page and display confirmation message or display access denied page if user is not authorized to delete the account (e.g trying to delete by id of some other user).
     */
    @GetMapping("/delete")
    public String processDeleteMemberAccount(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        String currentUserId = userService.getCurrentUser().getId();

        if (id.equals(currentUserId)) {
            userService.deleteUser(currentUserId);
            SecurityContextHolder.clearContext();
            redirectAttributes.addFlashAttribute("message", "Your account has been deleted with all your announcements.");
            return "redirect:/";
        } else {
            return "redirect:/account/access-denied";

        }

    }

    /**
     * Display a notifications page with missing or found pets in the area where
     * the member live.
     *
     * @param model   This model is to supply attributes used for rendering view.
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @return View with notifications page.
     */
    @GetMapping("/notifications")
    public String showNotificationsPage(Model model, @ModelAttribute("message") String message) {
        currentUser = userService.getCurrentUser();
        notifications = notificationService.getNotificationsByUserId(currentUser.getId());
        model.addAttribute("notificationList", notifications);
        model.addAttribute("headerMessage", "Lost or found pets in your area");
        model.addAttribute("message", message);
        return "account/notifications";
    }

    /**
     * Trigger when member want to delete notification. It uses passed notification id number
     * which will be deleted.
     *
     * @param id                 This is an id number for {@link Notification} object to delete
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Bo back to notification page if there is any notification left or redirect to the
     * user dashboard page.
     */
    @GetMapping("/notifications/delete")
    public String deleteUserNotification(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        notificationService.deleteNotificationById(id);
        redirectAttributes.addFlashAttribute("message", "Notification deleted!");

        notifications = notificationService.getNotificationsByUserId(currentUser.getId());

        if (notifications.size() == 0) {
            return "redirect:/account/dashboard";
        }

        return "redirect:/account/notifications";
    }

    /**
     * Display the login page with input form that contains input for the email address (as a username)
     * ane the password.
     *
     * @param user This is for retrieving current logged in user.
     * @return If user is logged in then redirect to the home page, if not then display the login page.
     */
    @GetMapping("/login")
    public String login(Principal user) {
        return user != null ? "redirect:/" : "account/login";
    }

    /**
     * Triggle when user want to log out.
     *
     * @param request  To provide request information for HTTP servlets.
     * @param response To provide HTTP-specific functionality in sending a response.
     * @return The logout function and redirect to home page.
     */
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        // Check if user is logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/account/login?logout";
    }

    /**
     * Display user updated details with the form.
     *
     * @param error This is error message passed from form validator.
     * @param model This model is to supply attributes used for rendering view and it contains
     *              current user model.
     * @return View with updated details form page.
     */
    @GetMapping("/update")
    public String showUpdateAccountPage(@ModelAttribute("errorMessage") String error, Model model) {
        currentUser = userService.getCurrentUser();

        if (currentUser.getLocation() != null) {
            currentUser.setLocationForm(currentUser.getLocation().getName());
        }

        model.addAttribute("headerMessage", "Update your account details");
        model.addAttribute("user", currentUser);
        model.addAttribute("locationList", locationService.getLocations());
        model.addAttribute("errorMessage", error);

        return "account/user-update";
    }

    /**
     * Triggle when member submit the update account details form.
     * This method will process submit form, make basic validation on if the the email address
     * already exists in the database return the error message.
     * already exists in the database return the error message.
     * If everything is fine then will send the updated data to the endpoint and relogin the user
     * with the current user details.
     *
     * @param user               This is a user model that is passed from the form.
     * @param bindingResult      This is to validate the form input and display error message.
     * @param model              This model is to supply attributes used for rendering view.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Bo back to user update page if there is any problem or redirect to the members dashboard.
     */
    @PostMapping("/update")
    public String processUpdateAccountForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        /*
         * This IF statement will check if provided in the form email
         * address already exists in the database only if a user changes
         * the email address
         */
        if (!currentUser.getEmail().equals(user.getEmail())) {

            // Lookup user exists in database by e-mail
            boolean userExists = userService.existsWithEmail(user.getEmail());

            if (userExists) {
                model.addAttribute("errorMessage", "Oops!  User registered with the email provided!");
                model.addAttribute("locationList", locationService.getLocations());

                return "account/user-update";
            }
        }

        if (bindingResult.hasErrors()) {
            return "user-update";
        } else {
            String locationForm = user.getLocationForm();
            user.setLocation(new Location(locationForm));

            user.setPassword(currentUser.getPassword());
            User responseUser = userService.saveOrUpdateMember(user);
            UserDetailsServiceImpl.authenticateUpdateUser(responseUser);

            redirectAttributes.addFlashAttribute("message", "Your account details have been changed!");
            return "redirect:/account/dashboard";
        }

    }

    /**
     * Display the page with a form with the change password input.
     *
     * @param error This is error message passed from form validator.
     * @param model This model is to supply attributes used for rendering view.
     * @return View with password change form page.
     */
    @GetMapping("/password")
    public String showPasswordUpdatePage(@ModelAttribute("errorMessage") String error, Model model) {
        currentUser = userService.getCurrentUser();
        model.addAttribute("headerMessage", "Change your password");
        model.addAttribute("errorMessage", error);
        return "account/password";
    }

    /**
     * Triggle when member submit the update password form. The method check if
     * new password and the old are matches and if there is no any error start to process the model.
     *
     * @param requestParams      This is the map with all request URL request params.
     * @param redirectAttributes This is a model with a confirmation message.
     * @param model              This model is to supply attributes used for rendering view.
     * @return View with the members' dashboard of if there is any problem in the form then display the error message.
     */
    @PostMapping("/password")
    public String processPasswordChangeForm(@RequestParam Map requestParams, RedirectAttributes redirectAttributes, Model model) {
        boolean passwordMatches = passwordEncoder
                .matches(String.valueOf(requestParams.get("oldPassword")), currentUser.getPassword());

        if (passwordMatches) {
            // Set new password
            currentUser.setPassword(passwordEncoder.encode(String.valueOf(requestParams.get("newPassword"))));

            // Save user
            User responseUser = userService.saveOrUpdateMember(currentUser);

            UserDetailsServiceImpl.authenticateUpdateUser(responseUser);

            redirectAttributes.addFlashAttribute("message", "Your password have been changed!");
            return "redirect:/account/dashboard";
        } else {
            model.addAttribute("errorMessage", "Oops!  Current password is invalid!");
            return "account/password";
        }

    }

    /**
     * Display a view with the reset password form that constants the email address input field.
     *
     * @param model         This model is to supply attributes used for rendering view.
     * @param passwordReset This is a helper model that contains the email address of the user to reset the password.
     * @param user          This is for retrieving current logged in user.
     * @return
     */
    @GetMapping("/reset")
    public String showResetPasswordPage(Model model, PasswordReset passwordReset, Principal user) {
        model.addAttribute("passwordReset", passwordReset);
        return user != null ? "redirect:/" : "account/reset";
    }


    /**
     * Triggle when member submit the email address to reset the account password.
     *
     * @param model              This model is to supply attributes used for rendering view.
     * @param passwordReset      This is a helper model that contains the email address of the user to reset the password.
     * @param bindingResult      This is to validate the form input and display error message.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the home page and display the confirmation message or go back to password reset page and display the error message.
     */
    @PostMapping("/reset")
    public String processResetPasswordForm(Model model, @Validated PasswordReset passwordReset, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String email = passwordReset.getEmail();

        // Lookup user exists in database by e-mail
        boolean userExists = userService.existsWithEmail(email);

        if (!userExists) {
            model.addAttribute("errorMessage", true);
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()) {
            return "account/reset";
        } else {
            userService.setMemberResetPasswordByEmail(email);

            redirectAttributes.addFlashAttribute("message", "Your password reset email have been sent!");
            return "redirect:/";
        }
    }


    /**
     * Display user registration page with the register form.
     *
     * @param model This model is to supply attributes used for rendering view and contains the user model.
     * @param user  This is for retrieving current logged in user.
     * @return Send the registration form model of if the user is already login then redirect to the home page.
     */
    @GetMapping("/register")
    public String showRegistrationPage(Model model, Principal user) {

        model.addAttribute("user", new User());
        model.addAttribute("locationList", locationService.getLocations());

        return user != null ? "redirect:/" : "account/register";

    }

    /**
     * Triggle when member submit the register new account form.
     * This method will process the registration user model and validate it.
     *
     * @param user               This is for retrieving current logged in user.
     * @param bindingResult      This is to validate the form input and display error message.
     * @param model              This model is to supply attributes used for rendering view.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the main page if no error found if there is any problem then go back to register page and display an error message.
     */
    @PostMapping("/register")
    public String processRegistrationForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        // Lookup user exists in database by e-mail
        boolean userExists = userService.existsWithEmail(user.getEmail());

        if (userExists) {
            model.addAttribute("errorMessage", "Oops!  User registered with the email provided.");
            model.addAttribute("locationList", locationService.getLocations());

            return "account/register";
        }

        if (bindingResult.hasErrors()) {
            return "account/register";
        } else {

            String locationForm = user.getLocationForm();
            user.setLocation(new Location(locationForm));

            userService.saveOrUpdateMember(user);

            redirectAttributes.addFlashAttribute("message", "Your account was successfully created. To activate it, please check your email.");

        }

        return "redirect:/";
    }


    /**
     * Display the confirmation page from the link with a token sent to user email address to authenticate
     * the email.
     *
     * @param token This is the generated for user confirmation token from the email message.
     * @param model This model is to supply attributes used for rendering view.
     * @return If token is valid then display the set new password form.
     */
    @GetMapping("/confirm")
    public String showConfirmationPage(@RequestParam("token") String token, Model model) {

        User member = userService.getUserByConfirmationToken(token);

        if (member == null) { // No token found in DB
            model.addAttribute("invalidToken", "Oops!  This is an invalid confirmation link.");
        } else { // Token found
            model.addAttribute("confirmationToken", member.getConfirmationToken());
        }

        return "account/confirm";
    }


    /**
     * Triggle when member submit the set new password input form.
     * This method will assign a new password the the user found by the confirmation token column.
     *
     * @param requestParams      This is the map with all request URL request params.
     * @param request            To provide request information for HTTP servlets.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the home page and display the confirmation message.
     * @throws ServletException When cannot login with the given username and password.
     */
    @PostMapping("/confirm")
    public String processConfirmationForm(
            @RequestParam Map requestParams,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws ServletException {

        // Find the user associated with the reset token
        User member = userService.getUserByConfirmationToken(String.valueOf(requestParams.get("confirmationToken")));

        // Set new password
        member.setPassword(passwordEncoder.encode(String.valueOf(requestParams.get("password"))));

        member.setConfirmationToken(null);

        // Save user
        userService.saveOrUpdateMember(member);

        request.login(member.getEmail(), String.valueOf(requestParams.get("password")));

        redirectAttributes.addFlashAttribute("message", "Your password have been set!");

        return "redirect:/";
    }

    /**
     * Display the page with all current member messages. It display display a list of conversation
     * that user started and/or the list of conversation that some other member sent to current user.
     *
     * @param message This is the confirmation message after deleting selected message.
     * @param model   This model is to supply attributes used for rendering view.
     * @return View with the list of messages.
     */
    @GetMapping("/messages")
    public String showMessages(@ModelAttribute("message") String message, Model model) {
        currentUser = userService.getCurrentUser();
        List<Message> allMessagesBySenderId = messageService.getAllMessagesBySenderId(currentUser.getId());
        List<Message> allMessagesByRecipientId = messageService.getAllMessagesByRecipientId(currentUser.getId());

        model.addAttribute("messagesAsSender", allMessagesBySenderId);
        model.addAttribute("messagesAsRecipient", allMessagesByRecipientId);
        model.addAttribute("headerMessage", "All your messages");
        model.addAttribute("message", message);
        return "account/messages";
    }

    /**
     * Display all the conversations from the given by id message.
     *
     * @param model     This model is to supply attributes used for rendering view.
     * @param messageId This is the message id that we want to display conversations.
     * @return View with the conversation or redirect to access denied page when user is not authorizes to view this message conversation.
     */
    @GetMapping("/messages/view/{id}")
    public String showConversation(Model model, @PathVariable("id") String messageId) {
        if (Strings.isBlank(messageId)) {
            return "account/messages";
        }

        Message message = messageService.getMessageById(messageId);
        User currentUser = userService.getCurrentUser();

        if (message.getSender().getId().equals(currentUser.getId()) ||
                message.getRecipient().getId().equals(currentUser.getId())) {
            model.addAttribute("conversationList", messageService.getConversationsByMessageId(messageId));
            model.addAttribute("conversation", new Conversation());
            model.addAttribute("messageSubject", message.getSubject());
            model.addAttribute("messageId", message.getId());
            model.addAttribute("timeAgo", new PrettyTime());

            String conversationUserName;
            if (message.getSender().getId().equals(currentUser.getId())) {
                conversationUserName = message.getRecipient().getFirstName();
            } else {
                conversationUserName = message.getSender().getFirstName();
            }

            model.addAttribute("headerMessage", "Conversation with " + conversationUserName);
            return "account/conversation";
        }

        return "account/access-denied";
    }

    /**
     * Triggle when member post a new conversation message.
     *
     * @param conversation This is current conversation message send by member.
     * @param messageId    This is the message id of the current conversation.
     * @return Process the post of a new conversation and redirect to current conversation page.
     */
    @PostMapping("/messages/view")
    public String processPostConversation(
            @ModelAttribute("conversation") Conversation conversation,
            @RequestParam("id") String messageId) {

        conversation.setAuthor(userService.getCurrentUser());

        // Set message id to current conversation
        conversation.setMessage(new Message(messageId));

        messageService.saveConversation(conversation);

        return "redirect:/account/messages/view/" + messageId;
    }

    /**
     * Triggle when member press the delete button for the selected message.
     * This method will delete the message.
     *
     * @param messageId          This is message id number of which user want to delete.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Process the delete message and go back to member messages list page or if there is no more message then redirect to the members' dashboard page.
     */
    @GetMapping("/messages/delete")
    public String deleteMessage(
            @RequestParam("id") String messageId, RedirectAttributes redirectAttributes) {
        Message message = messageService.getMessageById(messageId);
        User currentUser = userService.getCurrentUser();


        if (message.getSender().getId().equals(currentUser.getId()) ||
                message.getRecipient().getId().equals(currentUser.getId())) {
            messageService.deleteMessage(messageId);

            redirectAttributes.addFlashAttribute("message", "Message was successfully deleted!");

            List<Message> allMessagesBySenderId = messageService.getAllMessagesBySenderId(currentUser.getId());
            List<Message> allMessagesByRecipientId = messageService.getAllMessagesByRecipientId(currentUser.getId());
            if (allMessagesByRecipientId.size() == 0 && allMessagesBySenderId.size() == 0) {
                return "redirect:/account/dashboard";
            }

            return "redirect:/account/messages";
        }

        return "redirect:/account/access-denied";
    }


    /**
     * Display the acccess denid page if user is not authorized to view current resources.
     *
     * @return View with access denied information page.
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "account/access-denied";
    }
}