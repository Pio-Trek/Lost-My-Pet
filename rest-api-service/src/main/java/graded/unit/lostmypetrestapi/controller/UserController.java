package graded.unit.lostmypetrestapi.controller;

import com.google.common.base.Strings;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomException;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.service.EmailService;
import graded.unit.lostmypetrestapi.service.UserService;
import graded.unit.lostmypetrestapi.validation.UserValidation;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller layer class for {@link User} which exposes resources,
 * CRUD operations and business logic for the {@link UserService}.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@RestController
@RequestMapping("/api/users")
@Api(
        name = "User API",
        description = "Provides a list of methods that manage members and administrator accounts.",
        stage = ApiStage.GA)
public class UserController {

    // URI address for testing purpose
    static String URI = "/api/users/";

    private final UserService userService;
    private final UserValidation userValidation;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, UserValidation userValidation, EmailService emailService) {
        this.userService = userService;
        this.userValidation = userValidation;
        this.emailService = emailService;
    }

    /**
     * HTTP GET request method.
     * Fetch all users from the database.
     *
     * @return List of all {@link User} objects.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all users.")
    public List<User> fetchUsers() {
        return userService.getAllUsers();
    }


    /**
     * HTTP GET request method.
     * Fetch a single user by email address.
     *
     * @param email This is an email address of the {@link User} object to be fetched.
     * @return Optional value of {@link User} object.
     */
    @GetMapping(params = "email",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch a single user by email address.")
    public Optional<User> fetchUserByEmail(@ApiQueryParam(description = "Email address of the user") @RequestParam(value = "email", required = false) String email) throws CustomException {
        return userValidation.validateByEmail(email);
    }

    /**
     * HTTP GET request method.
     * Check if user email address exists in the database.
     *
     * @param email This is an email address of the {@link User} object to be checked.
     * @return Boolean value.
     */
    @GetMapping(path = "/exists/{email}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Check if user email address exists.")
    public boolean checksIsUserExistsByEmail(@ApiPathParam(description = "Email address of the user") @PathVariable("email") String email) {
        return userService.getUserByEmail(email).isPresent();
    }

    /**
     * HTTP GET request method.
     * Fetch a single user by sent confirmation token.
     *
     * @param token This is the confirmation token sent to user by email.
     * @return Optional value of {@link User} object.
     */
    @GetMapping(params = "token",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch a single user by confirmation token.")
    public Optional<User> fetchUserByConfirmationToken(@ApiQueryParam(description = "Activation token") @RequestParam(value = "token", required = false) String token) {
        return userService.getUserByConfirmationToken(token);
    }

    /**
     * HTTP POST request method.
     * Save a new user with member role to the database and send confirmation email with token.
     *
     * @param member This is a new object of the {@link User} to be save.
     * @return HTTP response with {@link User} member object.
     */
    @PostMapping(path = "/member",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new user with member role and send confirmation email with token.")
    public ResponseEntity<User> insertMember(@RequestBody @Validated User member, HttpServletRequest request) throws CustomException {

        userValidation.validateIsEmailUnique(member);

        // Disable user until they click on confirmation link in email
        member.setEnabled(false);

        if (member.getLocation().getName().isEmpty()) {
            member.setLocation(null);
        } else {
            userValidation.validateLocation(member).ifPresent(member::setLocation);
        }

        String subject = "Registration Confirmation";
        String message = "To confirm your e-mail address, please click the link below:\n";
        sendConfirmationEmail(member, request, subject, message);
        return userService.addMember(member);
    }

    /**
     * HTTP POST request method.
     * Save a new user with admin role to the database.
     *
     * @param admin This is a new object of the {@link User} to be save.
     * @return HTTP response with {@link User} admin object.
     */
    @PostMapping(path = "/admin",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new user with admin role.")
    public ResponseEntity<User> insertAdmin(@RequestBody @Validated User admin) {
        userValidation.validateIsEmailUnique(admin);

        admin.setEnabled(true);

        //prevent from accidental save admin with location value
        admin.setLocation(null);
        return userService.addAdmin(admin);
    }

    /**
     * HTTP PUT request method.
     * Save (update) an existing user with member role to the database.
     *
     * @param member This is a new object of {@link User} member to be update.
     * @return HTTP response with updated {@link User} member object.
     */
    @PutMapping(path = "/member",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save (update) an existing user with member role.")
    public ResponseEntity<User> updateMember(@RequestBody @Validated User member) throws CustomException {
        // Set user to enabled
        member.setEnabled(true);

        Optional<User> optionalUser = userValidation.validateById(member.getId());

        if (member.getLocation() == null || Strings.isNullOrEmpty(member.getLocation().getName())) {
            member.setLocation(null);
        } else {
            userValidation.validateLocation(member).ifPresent(member::setLocation);
        }

        return userService.updateMember(member);
    }

    /**
     * HTTP PUT request method.
     * Save (update) an existing user with admin role to the database.
     *
     * @param admin This is a new object of {@link User} admin to be update.
     * @return HTTP response with updated {@link User} admin object.
     */
    @PutMapping(path = "/admin",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save (update) an existing user with admin role.")
    public ResponseEntity<User> updateAdmin(@RequestBody @Validated User admin) throws CustomException {
        userValidation.validateById(admin.getId());
        admin.setLocation(null);
        return userService.updateAdmin(admin);
    }

    /**
     * HTTP PATCH request method.
     * Set a new confirmation token for the user to reset the password.
     *
     * @param email   This is user's email address for password reset.
     * @param request This is the {@link HttpServletRequest} object passed as an argument to the send confirmation email service method.
     * @return HTTP response with {@link User} object.
     * @throws CustomException When {@link User#email} is not valid.
     */
    @PatchMapping(path = "/member", params = "reset",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Set a new confirmation token for the user to reset the password.")
    public ResponseEntity<User> setMemberTokenToResetPassword(@ApiQueryParam(description = "The email address for password reset") @RequestParam(value = "reset", required = false) String email, HttpServletRequest request) throws CustomException {
        Optional<User> optUser = userValidation.validateByEmail(email);
        String subject = "Password Reset Confirmation";
        String message = "You recently requested a new password.\nPlease click the link below to complete your new password request:\n";
        optUser.ifPresent(user -> sendConfirmationEmail(user, request, subject, message));
        return userService.updateMember(optUser.orElse(null));
    }

    /**
     * HTTP DELETE request method.
     * Delete from the database a single user.
     *
     * @param id This is an id number of the {@link User} object to be delete.
     * @return HTTP response with confirmation message.
     */
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Delete from the database a single user.")
    public ResponseEntity<CustomResponse> removeUser(@ApiPathParam(description = "The id of the user") @PathVariable("id") String id) throws CustomException {
        userValidation.validateById(id);
        return userService.deleteUser(id);
    }

    /**
     * This method is responsible for sending to a user an email with confirmation link and token.
     *
     * @param user    This is the {@link User} object which required to send a token.
     * @param request This is the {@link HttpServletRequest} object passed as an argument with http scheme name and server address.
     * @param subject This is an email subject.
     * @param message This is an email message (body).
     */
    private void sendConfirmationEmail(User user, HttpServletRequest request, String subject, String message) {
        // Generate random UUID string token for confirmation link
        user.setConfirmationToken(UUID.randomUUID().toString().replace("-", ""));

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(user.getEmail());
        registrationEmail.setSubject(subject);
        registrationEmail.setText(message + appUrl + "/account/confirm?token=" + user.getConfirmationToken());
        registrationEmail.setFrom("728c72bb30-57d4a1@inbox.mailtrap.io");

        emailService.sendEmail(registrationEmail);
    }

}
