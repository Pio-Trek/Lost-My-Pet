package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link Found} domain
 * object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface FoundPostDao {

    /**
     * Fetch a list of found announcements.
     *
     * @return HTTP response with {@link Found} object
     */
    ResponseEntity<List<Found>> fetchAllFoundPosts();

    /**
     * Fetch a list of found announcements by member id number.
     *
     * @param memberId This is an id number of the {@link User} object by which found
     *                 announcements will be fetched.
     * @return HTTP response with list of {@link Found} object
     */
    ResponseEntity<List<Found>> fetchAllFoundPostsByMemberId(String memberId);

    /**
     * Fetch found dogs announcements.
     *
     * @return HTTP response with list of {@link Found} object
     */
    ResponseEntity<List<Found>> fetchFoundDogsPosts();

    /**
     * Fetch lost cats announcements.
     *
     * @return HTTP response with list of {@link Found} object
     */
    ResponseEntity<List<Found>> fetchFoundCatsPosts();

    /**
     * Fetch found announcement by id.
     *
     * @param id This is an id number of the {@link Found} announcement object to be fetched.
     * @return HTTP response with {@link Found} object
     */
    ResponseEntity<Found> fetchFoundPostById(Long id);

    /**
     * This method will set found announcement to enabled = true, which will make the post
     * moderated and visible to other users.
     *
     * @param id This is an id number of the {@link Found} announcement object to be enabled.
     */
    void setFoundPostEnabled(Long id);

    /**
     * Insert a new found announcement or update the exists if {@link Found#id} parameter is not empty.
     *
     * @param found This is the {@link User} member object that will be saved.
     */
    void insertOrUpdateFoundPost(Found found);

    /**
     * Delete lost announcement.
     *
     * @param id This is an id number of the {@link Found} announcement object to be deleted.
     */
    void removeFoundPost(Long id);
}
