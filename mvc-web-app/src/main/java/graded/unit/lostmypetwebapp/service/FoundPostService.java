package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.FoundPostDao;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link Found} pet announcement domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class FoundPostService {

    private final FoundPostDao foundPostDao;

    @Autowired
    public FoundPostService(FoundPostDao foundPostDao) {
        this.foundPostDao = foundPostDao;
    }

    /**
     * Fetch a list of found announcements.
     *
     * @return List of all {@link Found} objects.
     */
    public List<Found> getAllFoundPosts() {
        return foundPostDao.fetchAllFoundPosts().getBody();
    }

    /**
     * Fetch a list of found announcements by member id number.
     *
     * @param memberId This is an id number of the {@link User} object by which found
     *                 announcements will be fetched.
     * @return List of {@link Found} objects.
     */
    public List<Found> getAllFoundPostsByMemberId(String memberId) {
        return foundPostDao.fetchAllFoundPostsByMemberId(memberId).getBody();
    }

    /**
     * Fetch found dogs announcements.
     *
     * @return List of {@link Found} objects.
     */
    public List<Found> getAllFoundDogsPosts() {
        return foundPostDao.fetchFoundDogsPosts().getBody();
    }

    /**
     * Fetch lost cats announcements.
     *
     * @return List of {@link Found} objects.
     */
    public List<Found> getAllFoundCatsPosts() {
        return foundPostDao.fetchFoundCatsPosts().getBody();
    }

    /**
     * Fetch found announcement by id.
     *
     * @param id This is an id number of the {@link Found} announcement object to be fetched.
     * @return {@link Found} object
     */
    public Found getFoundPostById(Long id) {
        return foundPostDao.fetchFoundPostById(id).getBody();
    }

    /**
     * This method will set found announcement to enabled = true, which will make the post
     * moderated and visible to other users.
     *
     * @param id This is an id number of the {@link Found} announcement object to be enabled.
     */
    public void setFoundPostEnabled(Long id) {
        this.foundPostDao.setFoundPostEnabled(id);
    }

    /**
     * Insert a new found announcement or update the exists if {@link Found#id} parameter is not empty.
     *
     * @param found This is the {@link User} member object that will be saved.
     */
    public void saveOrUpdate(Found found) {
        this.foundPostDao.insertOrUpdateFoundPost(found);
    }

    /**
     * Delete lost announcement.
     *
     * @param id This is an id number of the {@link Found} announcement object to be deleted.
     */
    public void deleteFoundPostById(Long id) {
        this.foundPostDao.removeFoundPost(id);
    }
}
