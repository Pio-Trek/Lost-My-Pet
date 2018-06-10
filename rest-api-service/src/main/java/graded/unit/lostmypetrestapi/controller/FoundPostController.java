package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.entity.pets.Cat;
import graded.unit.lostmypetrestapi.entity.pets.Dog;
import graded.unit.lostmypetrestapi.entity.posts.Found;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomException;
import graded.unit.lostmypetrestapi.service.FoundPostService;
import graded.unit.lostmypetrestapi.validation.AnnouncementValidation;
import graded.unit.lostmypetrestapi.validation.UserValidation;
import javassist.NotFoundException;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller layer class for {@link Found} pet announcement which exposes resources,
 * CRUD operations and business logic for the {@link FoundPostService}.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@RestController
@RequestMapping("/api/announcements/found")
@Api(
        name = "Found Pet Announcement API",
        description = "Provides a list of methods that manage found pet announcements.",
        stage = ApiStage.GA)
public class FoundPostController {

    // URI address for testing purpose
    static String URI = "/api/announcements/found/";

    private final FoundPostService foundPostService;
    private final AnnouncementValidation announcementValidation;
    private final UserValidation userValidation;

    @Autowired
    public FoundPostController(FoundPostService foundPostService, AnnouncementValidation announcementValidation, UserValidation userValidation) {
        this.foundPostService = foundPostService;
        this.announcementValidation = announcementValidation;
        this.userValidation = userValidation;
    }

    /**
     * HTTP GET request method.
     * Fetch all found announcement posts from the database.
     *
     * @return List of all {@link Found} announcement objects.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all found announcement posts")
    public List<Found> fetchFoundPosts() {

        return foundPostService.getAllFoundPosts();
    }

    /**
     * HTTP GET request method.
     * Fetch all found announcement posts from the database by member id.
     *
     * @param memberId This is an {@link User#id} which announcement need to be find
     * @return List of Found announcement of the given Member
     * @throws CustomException When {@link User#id} id not valid
     */
    @GetMapping(path = "/member/{id}")
    @ApiMethod(description = "Fetch all found announcement posts by member id")
    public List<Found> fetchFoundPostsByMemberId(@ApiPathParam(description = "The id of the user") @PathVariable("id") String memberId) throws CustomException {
        userValidation.validateById(memberId);
        return foundPostService.getAllFoundPostsByMemberId(memberId);
    }

    /**
     * HTTP GET request method.
     * Fetch all found announcement posts with dogs from the database.
     *
     * @return List of all {@link Found} announcement contain {@link Dog} objects.
     */
    @GetMapping(path = "/dogs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all found announcement posts with dogs")
    public List<Found> fetchFoundDogsPosts() {
        return foundPostService.getAllFoundPosts()
                .stream()
                .filter(pet -> pet.getPet() instanceof Dog)
                .collect(Collectors.toList());
    }

    /**
     * HTTP GET request method.
     * Fetch all found announcement posts with cats from the database.
     *
     * @return List of all {@link Found} announcement contain {@link Cat} objects.
     */
    @GetMapping(path = "/cats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all found announcement posts with cats")
    public List<Found> fetchFoundCatsPosts() {
        return foundPostService.getAllFoundPosts()
                .stream()
                .filter(pet -> pet.getPet() instanceof Cat)
                .collect(Collectors.toList());
    }

    /**
     * HTTP GET request method.
     * Fetch a single found announcement post by id number.
     *
     * @param id This is an id number of {@link Found} announcement object to be fetched.
     * @return A specify by id {@link Found} announcement object.
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch a single found announcement post by id number")
    public Optional<Found> fetchFoundPostById(@ApiPathParam(description = "The id of the found announcements") @PathVariable("id") Long id) throws CustomException {
        return validateFoundPostId(id);
    }


    /**
     * HTTP GET request method.
     * Set {@link Found#enabled} property to true, so the announcement is moderated
     * and can be visible for other users.
     *
     * @param id This is id number of {@link Found} object to be enabled.
     * @return HTTP response with {@link Found} object.
     * @throws CustomException When {@link Found#id} is not valid.
     */
    @GetMapping(
            path = "/enabled/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Set found announcement enabled property to true, so the announcement is moderated and can be visible for other users")
    public ResponseEntity<Found> setFoundPostEnabled(@ApiPathParam(description = "The id of the found announcement") @PathVariable("id") Long id) throws CustomException {
        Optional<Found> found = validateFoundPostId(id);
        found.ifPresent(f -> f.setEnabled(true));
        return foundPostService.updateFoundPost(found.get(), found.get().getAddedDate());
    }

    /**
     * HTTP POST request method.
     * Save a new found announcement post to the database.
     *
     * @param found This is a new object of the {@link Found} announcement to be inserted.
     * @return HTTP response with {@link Found} announcement object.
     * @throws NotFoundException When {@link Found#location} is not valid.
     * @throws CustomException   When {@link Found#id} and {@link Found#pet} is not valid.
     */
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new found announcement post")
    public ResponseEntity<Found> insertFoundPost(@RequestBody @Validated Found found) throws NotFoundException, CustomException {
        // Validate if member exists
        userValidation.validateById(found.getMember().getId())
                .ifPresent(found::setMember);

        found.setEnabled(false);

        // Validate pet type
        found.setPet(announcementValidation.getPetTypeAndBreed(found));

        // Validate location
        announcementValidation.validateLocation(found).ifPresent(found::setLocation);

        // Validate number of pet colours
        if (found.getPet().getColours() != null) {
            announcementValidation.validateNumberOfColours(found);

            // Validate colours names and add to the current found announcement object
            found.getPet().setColours(announcementValidation.getPetColours(found));
        }

        return foundPostService.addFoundPost(found);
    }

    /**
     * HTTP PUT request method.
     * Save (update) an existing found announcement post to the database.
     *
     * @param found This is a new object of {@link Found} announcement to be update.
     * @return HTTP response with {@link Found} announcement object.
     * @throws NotFoundException When {@link Found#location} is not valid.
     * @throws CustomException   When {@link Found#id} and {@link Found#pet} is not valid.
     */
    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save (update) an existing found announcement post")
    public ResponseEntity<Found> updateFoundPost(@RequestBody @Validated Found found) throws NotFoundException, CustomException {
        // Validate if member exists
        userValidation.validateById(found.getMember().getId())
                .ifPresent(found::setMember);

        // Validate found announcement id
        Found dbFound = validateFoundPostId(found.getId()).orElse(null);

        // Get current announcement added date and time
        Date addedDate = dbFound.getAddedDate();

        // Validate pet id
        if (!dbFound.getPet().getId().equals(found.getPet().getId())) {
            throw new CustomException("Pet ID is not valid or found. Updating pet ID: " + dbFound.getPet().getId() + ". Existing pet ID: " + found.getPet().getId() + ".");
        }

        // Validate pet type
        found.setPet(announcementValidation.getPetTypeAndBreed(found));

        // Validate location
        announcementValidation.validateLocation(found).ifPresent(found::setLocation);

        // Validate pet colours
        if (found.getPet().getColours() != null) {
            announcementValidation.validateNumberOfColours(found);

            // Validate colours names and add to the current found announcement object
            found.getPet().setColours(announcementValidation.getPetColours(found));
        }

        return foundPostService.updateFoundPost(found, addedDate);
    }

    /**
     * HTTP DELETE request method.
     * Delete from the database a single found announcement post.
     *
     * @param id This is an id number of the {@link Found} announcement to be delete.
     * @return HTTP response with confirmation message.
     */
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Delete from a single found announcement post")
    public ResponseEntity<?> removeFoundPost(@ApiPathParam(description = "The id of the found announcement") @PathVariable("id") Long id) throws CustomException {
        validateFoundPostId(id);
        return foundPostService.deleteFoundPost(id);
    }

    /**
     * Method responsible for check if ID number exists in the database.
     *
     * @param id This is an id number of the {@link Found} announcement to be validate.
     * @return Optional {@link Found} announcement object.
     */
    private Optional<Found> validateFoundPostId(Long id) throws CustomException {
        return Optional.of(foundPostService.getFoundPostById(id)
                .orElseThrow(() -> new CustomException
                        ("Found pet announcement with ID: '" + id + "' not found")));
    }

}
