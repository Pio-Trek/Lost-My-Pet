package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.posts.Lost;
import graded.unit.lostmypetrestapi.entity.users.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link Lost} pet announcement domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface LostPostService {

    /**
     * Fetch all lost pet announcements from the database
     *
     * @return List of all {@link Lost} objects
     */
    List<Lost> getAllLostPosts();

    /**
     * Fetch all lost pet announcement that belongs to specified member by its id number.
     *
     * @param memberId This is an id number of the {@link User} object.
     * @return List of all {@link Lost} objects.
     */
    List<Lost> getAllLostPostsByMemberId(String memberId);

    /**
     * Fetch a single lost pet announcement by id
     *
     * @param id of {@link Lost} announcement object to be fetched
     * @return Optional value of {@link Lost} announcement object
     */
    Optional<Lost> getLostPostById(Long id);

    /**
     * Insert a new lost pet announcement
     *
     * @param lost new object of {@link Lost} announcement to be insert
     * @return HTTP response with {@link Lost} announcement object
     */
    ResponseEntity<Lost> addLostPost(Lost lost);

    /**
     * Updated existing lost pet announcement
     *
     * @param lost      new object of {@link Lost} announcement to be update
     * @param addedDate to be set in an updated object from old object to keep
     *                  the same date and time when the announcement was added
     * @return HTTP response with {@link Lost} announcement object
     */
    ResponseEntity<Lost> updateLostPost(Lost lost, Date addedDate);

    /**
     * Delete existing lost pet announcement by id
     *
     * @param id of the {@link Lost} announcement to be delete
     * @return HTTP response with confirmation message
     */
    ResponseEntity<?> deleteLostPost(Long id);

    /**
     * Count all enabled (moderated) lost announcements.
     *
     * @return Number of lost objects where {@link Lost#enabled} is true.
     */
    int countByEnabledFalse();
}
