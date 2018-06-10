package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.posts.Found;
import graded.unit.lostmypetrestapi.entity.users.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link Found} pet announcement domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface FoundPostService {

    /**
     * Fetch all found pet announcements from the database
     *
     * @return List of all {@link Found} objects
     */
    List<Found> getAllFoundPosts();

    /**
     * Fet all found pet announcement that belongs to specified member by its id number.
     *
     * @param memberId This is an id number of the {@link User} object.
     * @return List of all {@link Found} objects.
     */
    List<Found> getAllFoundPostsByMemberId(String memberId);

    /**
     * Fetch a single found pet announcement by id
     *
     * @param id of {@link Found} announcement object to be fetched
     * @return Optional value of {@link Found} announcement object
     */
    Optional<Found> getFoundPostById(Long id);

    /**
     * Insert a new found pet announcement
     *
     * @param found new object of {@link Found} announcement to be insert
     * @return HTTP response with {@link Found} announcement object
     */
    ResponseEntity<Found> addFoundPost(Found found);

    /**
     * Updated existing found pet announcement
     *
     * @param found     new object of {@link Found} announcement to be update
     * @param addedDate to be set in an updated object from old object to keep
     *                  the same date and time when the announcement was added
     * @return HTTP response with {@link Found} announcement object
     */
    ResponseEntity<Found> updateFoundPost(Found found, Date addedDate);

    /**
     * Delete existing found pet announcement by id
     *
     * @param id This is an id number of the {@link Found} announcement to be delete
     * @return HTTP response with confirmation message
     */
    ResponseEntity<?> deleteFoundPost(Long id);

    /**
     * Count all enabled (moderated) found announcements.
     *
     * @return Number of announcement objects where {@link Found#enabled} is true.
     */
    int countByEnabledFalse();
}
