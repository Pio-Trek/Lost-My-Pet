package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.entity.pets.Cat;
import graded.unit.lostmypetrestapi.entity.pets.Dog;
import graded.unit.lostmypetrestapi.entity.posts.Lost;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.exception.CustomException;
import graded.unit.lostmypetrestapi.service.LostPostService;
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

import static java.util.stream.Collectors.toList;

/**
 * Controller layer class for {@link Lost} pet announcement which exposes resources,
 * CRUD operations and business logic for the {@link LostPostService}.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@RestController
@RequestMapping("/api/announcements/lost")
@Api(
        name = "Lost Pet Announcement API",
        description = "Provides a list of methods that manage lost pet announcements.",
        stage = ApiStage.GA)
public class LostPostController {

    // URI address for testing purpose
    static String URI = "/api/announcements/lost/";

    private final LostPostService lostPostService;
    private final AnnouncementValidation announcementValidation;
    private final UserValidation userValidation;

    @Autowired
    public LostPostController(LostPostService lostPostService, AnnouncementValidation announcementValidation, UserValidation userValidation) {
        this.lostPostService = lostPostService;
        this.announcementValidation = announcementValidation;
        this.userValidation = userValidation;
    }

    /**
     * HTTP GET request method.
     * Fetch all lost announcement posts from the database.
     *
     * @return List of all {@link Lost} announcement objects.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all lost announcement posts")
    public List<Lost> fetchLostPosts() {
        return lostPostService.getAllLostPosts();
    }

    /**
     * HTTP GET request method.
     * Fetch all lost announcement posts from the database by member id.
     *
     * @param memberId This is an {@link User#id} which announcement need to be find
     * @return List of Lost announcement of the given Member
     * @throws CustomException When {@link User#id} id not valid
     */
    @GetMapping(path = "/member/{id}")
    @ApiMethod(description = "Fetch all lost announcement posts by member id")
    public List<Lost> fetchLostPostsByMemberId(@ApiPathParam(description = "The id of the user") @PathVariable("id") String memberId) throws CustomException {
        userValidation.validateById(memberId);
        return lostPostService.getAllLostPostsByMemberId(memberId);
    }

    /**
     * HTTP GET request method.
     * Fetch all lost announcement posts with dogs from the database.
     *
     * @return List of all {@link Lost} announcement contains {@link Dog} objects.
     */
    @GetMapping(path = "/dogs",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all lost announcement posts with dogs")
    public List<Lost> fetchLostDogsPosts() {
        return lostPostService.getAllLostPosts()
                .stream()
                .filter(pet -> pet.getPet() instanceof Dog)
                .collect(toList());
    }

    /**
     * HTTP GET request method
     * Fetch all lost announcement posts with cats from the database.
     *
     * @return List of all {@link Lost} announcement contains {@link Cat} objects.
     */
    @GetMapping(path = "/cats",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch all lost announcement posts with cats")
    public List<Lost> fetchLostCatsPosts() {
        return lostPostService.getAllLostPosts()
                .stream()
                .filter(pet -> pet.getPet() instanceof Cat)
                .collect(toList());
    }

    /**
     * HTTP GET request method.
     * Fetch a single lost announcement post by id number.
     *
     * @param id This is an id number of the {@link Lost} announcement object to be fetched
     * @return A specify by id {@link Lost} announcement object
     */
    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch a single lost announcement post by id number")
    public Optional<Lost> fetchLostPostById(@ApiPathParam(description = "The id of the lost announcement") @PathVariable("id") Long id) throws CustomException {
        return validateLostPostId(id);
    }

    /**
     * HTTP GET request method.
     * Set {@link Lost#enabled} property to true, so the announcement is moderated
     * and can be visible for other users.
     *
     * @param id This is id number of {@link Lost} object to be enabled.
     * @return HTTP response with {@link Lost} object.
     * @throws CustomException When {@link Lost#id} is not valid.
     */
    @GetMapping(
            path = "/enabled/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Set lost announcement enabled property to true, so the announcement is moderated and can be visible for other users")
    public ResponseEntity<Lost> setLostPostEnabled(@ApiPathParam(description = "The id of the lost announcement") @PathVariable("id") Long id) throws CustomException {
        Optional<Lost> lost = validateLostPostId(id);
        lost.ifPresent(f -> f.setEnabled(true));
        return lostPostService.updateLostPost(lost.get(), lost.get().getAddedDate());
    }

    /**
     * HTTP POST request method.
     * Save a new Lost announcement post to the database.
     *
     * @param lost This is a new object of the {@link Lost} announcement to be insert.
     * @return HTTP response with {@link Lost} announcement object
     * @throws NotFoundException When {@link Lost#location} is not valid.
     * @throws CustomException   When {@link Lost#id} and {@link Lost#pet} is not valid.
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save a new lost announcement post")
    public ResponseEntity<Lost> insertLostPost(@RequestBody @Validated Lost lost) throws NotFoundException, CustomException {
        // Validate if member exists
        userValidation.validateById(lost.getMember().getId())
                .ifPresent(lost::setMember);

        lost.setEnabled(false);

        // Validate pet type and breed
        lost.setPet(announcementValidation.getPetTypeAndBreed(lost));

        // Validate location
        announcementValidation.validateLocation(lost).ifPresent(lost::setLocation);

        // Validate pet name
        announcementValidation.validatePetName(lost.getPet().getName());

        if (lost.getPet().getColours() != null) {
            // Validate number of pet colours
            announcementValidation.validateNumberOfColours(lost);

            // Validate colours names and add to current lost announcement object
            lost.getPet().setColours(announcementValidation.getPetColours(lost));
        }

        return lostPostService.addLostPost(lost);
    }


    /**
     * HTTP PUT request method.
     * Save (update) an existing lost announcement post to the database.
     *
     * @param lost This is a new object of the {@link Lost} announcement to be update.
     * @return HTTP response with {@link Lost} announcement object.
     * @throws NotFoundException When {@link Lost#location} is not valid.
     * @throws CustomException   When {@link Lost#id} and {@link Lost#pet} is not valid.
     */
    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Save (update) an existing lost announcement post")
    public ResponseEntity<Lost> updateLostPost(@RequestBody @Validated Lost lost) throws NotFoundException, CustomException {
        // Validate if member exists
        userValidation.validateById(lost.getMember().getId())
                .ifPresent(lost::setMember);

        // Validate lost announcement id
        Lost dbLost = validateLostPostId(lost.getId()).orElse(null);

        // Get current announcement added date and time
        Date addedDate = dbLost.getAddedDate();

        // Validate pet id
        if (!dbLost.getPet().getId().equals(lost.getPet().getId())) {
            throw new CustomException("Pet ID is not valid or found. Updating pet ID: " + dbLost.getPet().getId() + ". Existing pet ID: " + lost.getPet().getId() + ".");
        }

        // Validate pet type
        lost.setPet(announcementValidation.getPetTypeAndBreed(lost));

        // Validate location
        announcementValidation.validateLocation(lost).ifPresent(lost::setLocation);

        // Validate pet colours
        if (lost.getPet().getColours() != null) {
            announcementValidation.validateNumberOfColours(lost);

            // Validate colours names and add to current lost announcement object
            lost.getPet().setColours(announcementValidation.getPetColours(lost));
        }

        return lostPostService.updateLostPost(lost, addedDate);
    }

    /**
     * HTTP DELETE request method.
     * Delete from the database a single lost announcement post.
     *
     * @param id This is an id number of the {@link Lost} announcement to be delete.
     * @return HTTP response with confirmation message.
     * @throws CustomException When {@link Lost#id} is not valid
     */
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Delete from a single lost announcement post")
    public ResponseEntity<?> removeLostPost(@ApiPathParam(description = "The id of the lost announcement") @PathVariable("id") Long id) throws CustomException {
        validateLostPostId(id);
        return lostPostService.deleteLostPost(id);
    }

    /**
     * Method responsible for check if lost post ID number exists in the database.
     *
     * @param id This is an id number of the {@link Lost} announcement to be validate
     * @return Optional instance of {@link Lost} announcement object.
     * @throws CustomException When {@link Lost#id} is not valid.
     */
    private Optional<Lost> validateLostPostId(Long id) throws CustomException {
        return Optional.of(lostPostService.getLostPostById(id)
                .orElseThrow(() -> new CustomException
                        ("Lost pet announcement with ID: '" + id + "' not found")));
    }

}