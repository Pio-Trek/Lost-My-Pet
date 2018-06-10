package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link Lost} domain
 * object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface LostPostDao {

    /**
     * Fetch a list of lost announcements.
     *
     * @return HTTP response with {@link Lost} object
     */
    ResponseEntity<List<Lost>> fetchAllLostPosts();

    /**
     * Fetch a list of lost announcements by member id number.
     *
     * @param memberId This is an id number of the {@link User} object by which lost
     *                 announcements will be fetched.
     * @return HTTP response with list of {@link Lost} object
     */
    ResponseEntity<List<Lost>> fetchAllLostPostsByMemberId(String memberId);

    /**
     * Fetch lost dogs announcements.
     *
     * @return HTTP response with list of {@link Lost} object
     */
    ResponseEntity<List<Lost>> fetchLostDogsPosts();

    /**
     * Fetch lost cats announcements.
     *
     * @return HTTP response with list of {@link Lost} object
     */
    ResponseEntity<List<Lost>> fetchLostCatsPosts();

    /**
     * Fetch lost announcement by id.
     *
     * @param id This is an id number of the {@link Lost} announcement object to be fetched.
     * @return HTTP response with {@link Lost} object
     */
    ResponseEntity<Lost> fetchLostPostById(Long id);

    /**
     * This method will set lost announcement to enabled = true, which will make the post
     * moderated and visible to other users.
     *
     * @param id This is an id number of the {@link Lost} announcement object to be enabled.
     */
    void setLostPostEnabled(Long id);

    /**
     * Insert a new lost announcement or update the exists if {@link Lost#id} parameter is not empty.
     *
     * @param lost This is the {@link User} member object that will be saved.
     */
    void insertOrUpdateLostPost(Lost lost);

    /**
     * Delete lost announcement.
     *
     * @param id This is an id number of the {@link Lost} announcement object to be deleted.
     */
    void removeLostPost(Long id);

}
