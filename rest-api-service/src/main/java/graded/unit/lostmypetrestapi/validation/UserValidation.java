package graded.unit.lostmypetrestapi.validation;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.ClientException;
import graded.unit.lostmypetrestapi.exception.CustomException;
import graded.unit.lostmypetrestapi.service.LocationService;
import graded.unit.lostmypetrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Member object validation helper class.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class UserValidation {

    private final UserService userService;
    private final LocationService locationService;

    @Autowired
    public UserValidation(UserService userService, LocationService locationService) {
        this.userService = userService;
        this.locationService = locationService;
    }

    /**
     * Method responsible for check if {@link User#id} number exists in the database.
     *
     * @param id This is an id number of {@link User} object to be validated.
     * @return Optional {@link User} object fetch from the database.
     */
    public Optional<User> validateById(String id) throws CustomException {
        return Optional.of(userService.getUserById(id)
                .orElseThrow(() -> new CustomException("User with ID: '" + id + "' not found")));
    }

    /**
     * Method responsible for check if {@link User#email} address exists in the database.
     *
     * @param email This is an email address of {@link User} object to be validated.
     * @return Optional {@link User} object fetch from the database.
     */
    public Optional<User> validateByEmail(String email) throws CustomException {
        return Optional.of(userService.getUserByEmail(email)
                .orElseThrow(() -> new CustomException("User with email address: '" + email + "' not found")));
    }

    /**
     * Method responsible for check if member's email address is unique and
     * if not already exist in the database.
     */
    public void validateIsEmailUnique(User user) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            throw new ClientException(HttpStatus.CONFLICT);
        }
    }

    /**
     * Validate {@link User#location}.
     *
     * @param member This is the {@link User} object to be validated.
     * @return Optional {@link Location} object fetched from the database.
     */
    public Optional<Location> validateLocation(User member) throws CustomException {
        return Optional.of(locationService.getLocationByName(member.getLocation().getName())
                .orElseThrow(() -> new CustomException("Location name: '" + member.getLocation().getName() + "' not found")));

    }
}
